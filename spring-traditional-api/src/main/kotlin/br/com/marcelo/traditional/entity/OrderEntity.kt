package br.com.marcelo.traditional.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(
    indexes = [
        Index(columnList = "orderId", unique = true),
        Index(columnList = "referenceId"),
    ],
    name = "ORDER"
)
data class OrderEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val tableId: Long = 0,
    val id: String = "ORDER_${UUID.randomUUID().toString().uppercase()}",
    val referenceId: String,
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    val items: List<ItemEntity>,
    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    val customer: CustomerEntity,
)

@Entity(name = "ITEM")
data class ItemEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String,
    val value: Int,
    val quantity: Int,
)

@Entity(name = "CUSTOMER")
data class CustomerEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val fullName: String,
    val taxId: String,
)
