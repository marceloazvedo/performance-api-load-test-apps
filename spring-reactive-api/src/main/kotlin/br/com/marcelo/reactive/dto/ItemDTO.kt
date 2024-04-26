package br.com.marcelo.reactive.dto

import br.com.marcelo.reactive.entity.ItemEntity
import br.com.marcelo.reactive.entity.OrderEntity

data class ItemDTO(
    val name: String? = null,
    val quantity: Int? = null,
    val value: Int? = null,
) {
    fun toEntity(orderEntity: OrderEntity) =
        ItemEntity(
            name = this.name!!,
            quantity = this.quantity!!,
            value = this.value!!,
            ofOrder = orderEntity,
        )
}
