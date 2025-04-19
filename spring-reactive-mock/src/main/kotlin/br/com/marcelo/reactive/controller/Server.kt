package br.com.marcelo.reactive.controller

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.RequestPredicates.GET
import org.springframework.web.reactive.function.server.RequestPredicates.POST
import org.springframework.web.reactive.function.server.RequestPredicates.accept
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.time.Duration
import java.util.UUID

@Component
class Handlers {
    fun pay(request: ServerRequest): Mono<ServerResponse> {
        val startTime = System.currentTimeMillis()

        return Mono.delay(Duration.ofSeconds(1))
            .then(
                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(
                    """
                    {"code":"${UUID.randomUUID().toString().uppercase()}","message":"SUCCESS"}
                    """.trimIndent(),
                ),
            )
            .doOnSuccess {
                val endTime = System.currentTimeMillis()
                println("[PayHandler][DURATION] Time taken: ${endTime - startTime} ms")
            }
    }

    fun validate(request: ServerRequest): Mono<ServerResponse> {
        val startTime = System.currentTimeMillis()

        return Mono.delay(Duration.ofSeconds(1))
            .then(
                ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(
                        """{"recommendation":"APPROVE"}""".trimIndent(),
                    ),
            )
            .doOnSuccess {
                val endTime = System.currentTimeMillis()
                println("[RiskAnalysisHandler][DURATION] Time taken: ${endTime - startTime} ms")
            }
    }

    fun authorization(serverRequest: ServerRequest): Mono<ServerResponse> {
        val startTime = System.currentTimeMillis()

        return Mono.delay(Duration.ofMillis(200))
            .then(ServerResponse.ok().bodyValue("OK"))
            .doOnSuccess {
                val endTime = System.currentTimeMillis()
                println("[Authorization][DURATION] Time taken: ${endTime - startTime} ms")
            }
    }
}

@Configuration
class Routers {
    @Bean
    fun route(handlers: Handlers): RouterFunction<ServerResponse> {
        return RouterFunctions
            .route(POST("/gateway/pay").and(accept(MediaType.APPLICATION_JSON)), handlers::pay)
            .andRoute(POST("/risk-analysis/validate").and(accept(MediaType.APPLICATION_JSON)), handlers::validate)
            .andRoute(GET("/sellers/authorization").and(accept(MediaType.APPLICATION_JSON)), handlers::authorization)
    }
}
