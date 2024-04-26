package br.com.marcelo.reactive.controller

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RequestPredicates.POST
import org.springframework.web.reactive.function.server.RequestPredicates.accept
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class OrderRouter {
    @Bean
    fun route(orderHandler: OrderHandler): RouterFunction<ServerResponse> {
        return RouterFunctions
            .route(POST("/orders").and(accept(MediaType.APPLICATION_JSON)), orderHandler::create)
    }
}