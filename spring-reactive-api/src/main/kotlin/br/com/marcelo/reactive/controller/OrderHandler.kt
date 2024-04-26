package br.com.marcelo.reactive.controller

import br.com.marcelo.reactive.service.OrderService
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.yaml.snakeyaml.internal.Logger
import reactor.core.publisher.Mono

@Component
class OrderHandler(
    private val orderService: OrderService,
) {

    companion object {
        val LOGGER = LoggerFactory.getLogger(OrderService::class.java)
    }

    fun create(request: ServerRequest): Mono<ServerResponse> {
        return orderService.create(request).flatMap {
            ServerResponse.ok().bodyValue(it)
        }
    }

}