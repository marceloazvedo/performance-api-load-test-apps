package br.com.marcelo.traditional.dto

import br.com.marcelo.traditional.entity.ItemEntity
import br.com.marcelo.traditional.entity.OrderEntity

data class ItemDTO(
    val name: String? = null,
    val quantity: Int? = null,
    val value: Int? = null,
) {
    fun toEntity(orderEntity: OrderEntity) =
        ItemEntity(
            name = this.name!!,
            value = this.value!!,
            quantity = this.quantity!!,
            ofOrder = orderEntity,
        )
}
