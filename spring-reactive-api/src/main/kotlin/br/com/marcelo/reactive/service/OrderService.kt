package br.com.marcelo.reactive.service

import br.com.marcelo.reactive.client.PaymentGatewayClient
import br.com.marcelo.reactive.client.RiskAnalysisClient
import br.com.marcelo.reactive.client.SellerClient
import br.com.marcelo.reactive.context.OrderContext
import br.com.marcelo.reactive.dto.AnalysisRecommendation
import br.com.marcelo.reactive.dto.OrderDTO
import br.com.marcelo.reactive.entity.CustomerEntity
import br.com.marcelo.reactive.entity.OrderEntity
import br.com.marcelo.reactive.exception.IdempontencyException
import br.com.marcelo.reactive.exception.PaymentDeclinedException
import br.com.marcelo.reactive.exception.RecommendationToDisapproveException
import org.slf4j.LoggerFactory
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerRequest
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

@Service
class OrderService(
    private val sellerClient: SellerClient,
    private val riskAnalysisClient: RiskAnalysisClient,
    private val paymentGatewayClient: PaymentGatewayClient,
    private val databaseClient: DatabaseClient,
) {
    companion object {
        val LOGGER = LoggerFactory.getLogger(OrderService::class.java)
        var count = 1
        val ORDER_NOT_FOUND = "Order not found"
    }

    fun create(serverRequest: ServerRequest): Mono<OrderDTO> {
        LOGGER.info("Creating order: {}", count++)
        val authorization = serverRequest.headers().header("Authorization").first()
        return serverRequest.bodyToMono(OrderDTO::class.java)
            .map { createContext(it, authorization) }
            .doOnNext { LOGGER.info("reference_id={}", it.order!!.referenceId) }
            .flatMap(this::validateRequest)
            .flatMap(this::callGateway)
            .flatMap(this::save)
            .map { it.order!!.copy(id = it.orderEntity!!.id) }
    }

    private fun createContext(orderDTO: OrderDTO, authorization: String): OrderContext =
        OrderContext(
            order = orderDTO,
            sellerAccessToken = authorization
        )

    private fun validateRequest(orderContext: OrderContext): Mono<OrderContext> {
        return verifyIdempotency(orderContext)
            .flatMap(::validateSellerToken)
            .flatMap(::makeRiskAnalysis)
    }

    private fun verifyIdempotency(orderContext: OrderContext): Mono<OrderContext> {
        return databaseClient.sql("SELECT id FROM order_entity WHERE reference_id = :referenceId LIMIT 1")
            .bind("referenceId", orderContext.order!!.referenceId!!)
            .fetch().one()
            .map { it["id"] as String }
            .defaultIfEmpty(ORDER_NOT_FOUND)
            .map {
                if (it != ORDER_NOT_FOUND) throw IdempontencyException()
                orderContext
            }
    }

    private fun validateSellerToken(orderContext: OrderContext): Mono<OrderContext> {
        return sellerClient.validateToken(orderContext.sellerAccessToken!!)
            .map { orderContext }
    }

    private fun makeRiskAnalysis(orderContext: OrderContext): Mono<OrderContext> {
        return riskAnalysisClient.analyseTransaction(orderContext.toTransactionData())
            .map {
                if (it.recommendation == AnalysisRecommendation.APPROVE) orderContext
                else throw RecommendationToDisapproveException()
            }
    }

    private fun callGateway(orderContext: OrderContext): Mono<OrderContext> {
        return paymentGatewayClient.pay(orderContext.order!!.payment!!.paymentMethod!!)
            .map {
                if (it.message == "SUCCESS") orderContext
                else throw PaymentDeclinedException()
            }
    }

    private fun saveCustomer(orderContext: OrderContext): Mono<OrderContext> {
        return databaseClient.sql("INSERT INTO  customer_entity (full_name, tax_id) VALUES (:fullName, :taxId)")
            .filter { statement, _ -> statement.returnGeneratedValues("id").execute() }
            .bind("fullName", orderContext.order!!.customer!!.fullName!!)
            .bind("taxId", orderContext.order.customer!!.taxId!!)
            .fetch()
            .first()
            .map { (it["id"] as Int).toLong() }
            .map {
                orderContext.copy(customerEntity = CustomerEntity(
                    id = it,
                    fullName = orderContext.order.customer.fullName!!,
                    taxId = orderContext.order.customer.taxId!!
                ))
            }
    }

    private fun saveOrder(orderContext: OrderContext): Mono<OrderContext> {
        return databaseClient.sql("INSERT INTO  order_entity (id, reference_id, customer_id) VALUES (:id, :referenceId, :customerId)")
            .filter { statement, _ -> statement.returnGeneratedValues("id", "table_id").execute() }
            .bind("id", "ORDER_${UUID.randomUUID().toString().uppercase()}")
            .bind("referenceId", orderContext.order!!.referenceId!!)
            .bind("customerId", orderContext.customerEntity!!.id)
            .fetch()
            .first()
            .map {
                val tableId = (it["table_id"] as Int).toLong()
                val orderId = it["id"] as String
                Pair(orderId, tableId)
            }
            .map { (id, tableId) ->
                val orderEntity = OrderEntity(
                    id = id,
                    tableId = tableId,
                    items = listOf(),
                    customer = orderContext.customerEntity,
                    referenceId = orderContext.order.referenceId!!,
                )
                orderContext.copy(orderEntity = orderEntity)
            }
    }

    private fun saveItems(orderContext: OrderContext): Mono<OrderContext> {
        return Flux.fromIterable(orderContext.order!!.items!!)
            .flatMap {
                databaseClient.sql("INSERT INTO item_entity (name, value, quantity, of_order) VALUES (:name, :value, :quantity, :ofOrder)")
                    .filter { statement, _ -> statement.returnGeneratedValues("id").execute() }
                    .bind("name", it!!.name!!)
                    .bind("value", it.value!!)
                    .bind("quantity", it.quantity!!)
                    .bind("ofOrder", orderContext.orderEntity!!.tableId)
                    .fetch().first()
                    .map {
                        (it["id"] as Int).toLong()
                    }
            }.collectList()
            .flatMap {
                Mono.just(orderContext)
            }
    }

    private fun save(orderContext: OrderContext): Mono<OrderContext> {
        return saveCustomer(orderContext)
            .flatMap(::saveOrder)
            .flatMap(::saveItems)
    }

}
