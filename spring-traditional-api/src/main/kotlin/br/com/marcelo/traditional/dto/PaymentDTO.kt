package br.com.marcelo.traditional.dto

data class PaymentDTO(
    val amount: Int? = null,
    val paymentMethod: PaymentMethodDTO? = null,
)
