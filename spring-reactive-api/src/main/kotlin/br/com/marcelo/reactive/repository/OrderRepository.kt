package br.com.marcelo.reactive.repository

import br.com.marcelo.reactive.entity.OrderEntity
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface OrderRepository : R2dbcRepository<OrderEntity, Long> {
    @Query("SELECT table_id FROM order_entity WHERE reference_id = $1")
    fun findFirstByReferenceId(referenceId: String): Mono<Long>
}
