package br.com.marcelo.traditional.context

import br.com.marcelo.traditional.dto.OrderDTO
import br.com.marcelo.traditional.dto.TransactionDataDTO
import br.com.marcelo.traditional.entity.CustomerEntity
import br.com.marcelo.traditional.entity.ItemEntity
import br.com.marcelo.traditional.entity.OrderEntity
import java.util.UUID

data class OrderContext(
    val sellerAccessToken: String? = null,
    val order: OrderDTO? = null,
) {
    fun toTransactionData() =
        TransactionDataDTO(
            amount = this.order!!.payment!!.amount,
            customerFullName = this.order.customer!!.fullName,
        )

    fun toEntity() =
        OrderEntity(
            referenceId = UUID.randomUUID().toString(),
            items =
                this.order!!.items!!.map {
                    ItemEntity(
                        name = it!!.name!!,
                        value = it.value!!,
                        quantity = it.quantity!!,
                    )
                },
            customer =
                CustomerEntity(
                    fullName = this.order.customer!!.fullName!!,
                    taxId = this.order.customer.taxId!!,
                ),
        )
}
