package br.com.marcelo.traditional.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne

@Entity
data class OrderEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val tableId: Long = 0,
    val id: String? = null,
    val referenceId: String,
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, mappedBy = "ofOrder")
    val items: List<ItemEntity>,
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
    @ManyToOne
    @JoinColumn(name = "of_order", referencedColumnName = "tableId")
    val ofOrder: OrderEntity?,
)

@Entity
data class CustomerEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val fullName: String,
    val taxId: String,
)
