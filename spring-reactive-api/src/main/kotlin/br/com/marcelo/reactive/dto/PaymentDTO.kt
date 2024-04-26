package br.com.marcelo.reactive.dto

data class PaymentDTO(
    val amount: Int? = null,
    val paymentMethod: PaymentMethodDTO? = null,
)
