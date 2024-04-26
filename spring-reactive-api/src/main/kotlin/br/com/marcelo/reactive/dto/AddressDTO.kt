package br.com.marcelo.reactive.dto

data class AddressDTO(
    val street: String? = null,
    val number: String? = null,
    val state: String? = null,
    val city: String? = null,
    val locality: String? = null,
    val zipCode: String? = null,
    val complement: String? = null,
)
