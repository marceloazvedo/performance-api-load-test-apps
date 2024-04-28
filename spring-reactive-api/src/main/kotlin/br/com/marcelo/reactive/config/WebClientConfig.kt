package br.com.marcelo.reactive.config

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import reactor.netty.resources.ConnectionProvider

@Component
class WebClientConfig {

    companion object {
        val LOGGER = LoggerFactory.getLogger(WebClientConfig::class.java)
    }

    @Bean
    fun webClientBuilder(): WebClient.Builder {
        LOGGER.info("Create WebClientBuilder")
        val httpClient = HttpClient.create(
            ConnectionProvider.builder("myHttpClient")
                .maxConnections(500000)
                .pendingAcquireMaxCount(500000)
                .build()
        )
        return WebClient.builder()
            .clientConnector(ReactorClientHttpConnector(httpClient))
    }
}