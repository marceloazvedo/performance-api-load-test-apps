package br.com.marcelo.traditional.dto

data class CustomerDTO(
    val fullName: String? = null,
    val taxId: String? = null,
    val address: AddressDTO? = null,
)
