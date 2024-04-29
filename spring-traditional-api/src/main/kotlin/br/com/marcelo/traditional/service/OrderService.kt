package br.com.marcelo.traditional.service

import br.com.marcelo.traditional.client.PaymentGatewayClient
import br.com.marcelo.traditional.client.RiskAnalysisClient
import br.com.marcelo.traditional.client.SellerClient
import br.com.marcelo.traditional.context.OrderContext
import br.com.marcelo.traditional.dto.AnalysisRecommendation
import br.com.marcelo.traditional.dto.OrderDTO
import br.com.marcelo.traditional.exception.IdempontencyException
import br.com.marcelo.traditional.exception.PaymentDeclinedException
import br.com.marcelo.traditional.exception.RecommendationToDisapproveException
import br.com.marcelo.traditional.repository.ItemRepository
import br.com.marcelo.traditional.repository.OrderRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class OrderService(
    private val sellerClient: SellerClient,
    private val riskAnalysisClient: RiskAnalysisClient,
    private val paymentGatewayClient: PaymentGatewayClient,
    private val orderRepository: OrderRepository,
    private val itemRepository: ItemRepository,
) {
    companion object {
        val LOGGER = LoggerFactory.getLogger(OrderService::class.java)
        var count = 1
    }

    fun create(context: OrderContext): OrderDTO {
        LOGGER.info("Creating order {}", count++)
        val idempotencyOrder = orderRepository.findOneByReferenceId(UUID.randomUUID().toString()) // Apenas simula uma busca por idmpotencia
        if (idempotencyOrder != null) {
            throw IdempontencyException()
        }
        sellerClient.validateToken(context.sellerAccessToken!!)
        val recommendation = riskAnalysisClient.analyseTransaction(context.toTransactionData())
        if (recommendation!!.recommendation == AnalysisRecommendation.DISAPPROVE) {
            throw RecommendationToDisapproveException()
        }
        val paymentResponse = paymentGatewayClient.pay(context.order!!.payment!!.paymentMethod!!)
        if (paymentResponse!!.message != "SUCCESS") {
            throw PaymentDeclinedException()
        }
        val orderEntity = orderRepository.save(context.toEntity().copy(id = "ORDE_${UUID.randomUUID().toString().uppercase()}"))
        context.order.items!!.forEach { itemRepository.save(it!!.toEntity(orderEntity)) }
        return context.order.copy(id = orderEntity.id)
    }
}
