package br.com.marcelo.traditional.context

import br.com.marcelo.traditional.dto.OrderDTO
import br.com.marcelo.traditional.dto.TransactionDataDTO
import br.com.marcelo.traditional.entity.CustomerEntity
import br.com.marcelo.traditional.entity.OrderEntity

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
            referenceId = this.order!!.referenceId!!,
            items = listOf(),
            customer =
                CustomerEntity(
                    fullName = this.order.customer!!.fullName!!,
                    taxId = this.order.customer.taxId!!,
                ),
        )
}
