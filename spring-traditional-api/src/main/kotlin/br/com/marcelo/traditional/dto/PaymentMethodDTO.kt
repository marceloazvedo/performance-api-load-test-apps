package br.com.marcelo.traditional.dto

data class PaymentMethodDTO(
    val type: String? = null,
    val installments: Int? = null,
    val card: CardDTO? = null,
)
