package br.com.marcelo.reactive.controller

import br.com.marcelo.reactive.service.OrderService
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class OrderHandler(
    private val orderService: OrderService,
) {
    fun create(request: ServerRequest): Mono<ServerResponse> {
        return orderService.create(request).flatMap {
            ServerResponse.ok().bodyValue(it)
        }
    }
}