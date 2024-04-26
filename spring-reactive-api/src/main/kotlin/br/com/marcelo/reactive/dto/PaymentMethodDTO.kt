package br.com.marcelo.reactive.dto

data class PaymentMethodDTO(
    val type: String? = null,
    val installments: Int? = null,
    val card: CardDTO? = null,
)
