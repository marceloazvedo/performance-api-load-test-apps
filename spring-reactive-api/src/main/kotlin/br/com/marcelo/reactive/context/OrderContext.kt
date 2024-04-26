package br.com.marcelo.reactive.context

import br.com.marcelo.reactive.dto.OrderDTO
import br.com.marcelo.reactive.dto.TransactionDataDTO
import br.com.marcelo.reactive.entity.CustomerEntity
import br.com.marcelo.reactive.entity.ItemEntity
import br.com.marcelo.reactive.entity.OrderEntity
import java.util.UUID

data class OrderContext(
    val sellerAccessToken: String? = null,
    val order: OrderDTO? = null,
    val orderEntity: OrderEntity? = null,
    val customerEntity: CustomerEntity? = null,
    val itemsEntity: List<ItemEntity>? = null,
) {
    fun toTransactionData() =
        TransactionDataDTO(
            amount = this.order!!.payment!!.amount,
            customerFullName = this.order.customer!!.fullName,
        )

    fun toEntity() =
        OrderEntity(
            referenceId = UUID.randomUUID().toString(),
            customer =
                CustomerEntity(
                    fullName = this.order!!.customer!!.fullName!!,
                    taxId = this.order.customer!!.taxId!!,
                ),
        )
}
