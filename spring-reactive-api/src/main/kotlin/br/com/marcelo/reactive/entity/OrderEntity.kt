package br.com.marcelo.reactive.entity

data class OrderEntity(
    val tableId: Long = 0,
    val id: String? = null,
    val referenceId: String,
    val items: List<ItemEntity> = mutableListOf(),
    val customer: CustomerEntity?,
)

data class ItemEntity(
    val id: Long = 0,
    val name: String,
    val value: Int,
    val quantity: Int,
    val ofOrder: OrderEntity?,
)

data class CustomerEntity(
    val id: Long = 0,
    val fullName: String,
    val taxId: String,
)
