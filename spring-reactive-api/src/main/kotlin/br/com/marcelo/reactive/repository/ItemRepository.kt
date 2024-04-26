package br.com.marcelo.reactive.repository

import br.com.marcelo.reactive.entity.ItemEntity
import org.springframework.data.r2dbc.repository.R2dbcRepository

interface ItemRepository : R2dbcRepository<ItemEntity, Long>
