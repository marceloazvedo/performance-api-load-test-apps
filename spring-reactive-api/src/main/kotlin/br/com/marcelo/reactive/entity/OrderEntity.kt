package br.com.marcelo.reactive.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.util.UUID

data class OrderEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val tableId: Long = 0,
    val id: String = "ORDER_${UUID.randomUUID().toString().uppercase()}",
    val referenceId: String,
    @OneToMany(mappedBy = "ofOrder", cascade = [CascadeType.ALL], orphanRemoval = true)
    val items: List<ItemEntity> = mutableListOf(),
    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    val customer: CustomerEntity?,
)

@Entity
data class ItemEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String,
    val value: Int,
    val quantity: Int,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "of_order", referencedColumnName = "id")
    val ofOrder: OrderEntity?,
)

@Entity
data class CustomerEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val fullName: String,
    val taxId: String,
)
