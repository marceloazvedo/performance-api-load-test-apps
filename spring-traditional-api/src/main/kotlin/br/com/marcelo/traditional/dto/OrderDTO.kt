package br.com.marcelo.traditional.dto

data class OrderDTO(
    val id: String? = null,
    val referenceId: String? = null,
    val payment: PaymentDTO? = null,
    val items: List<ItemDTO?>? = null,
    val customer: CustomerDTO? = null,
)
