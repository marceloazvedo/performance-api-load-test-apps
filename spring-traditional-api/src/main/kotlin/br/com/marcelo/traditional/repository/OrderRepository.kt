package br.com.marcelo.traditional.repository

import br.com.marcelo.traditional.entity.OrderEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository : JpaRepository<OrderEntity, Long> {
    fun findOneByReferenceId(referenceId: String): OrderEntity?
}
