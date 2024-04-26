package br.com.marcelo.reactive.dto

data class CustomerDTO(
    val fullName: String? = null,
    val taxId: String? = null,
    val address: AddressDTO? = null,
)
