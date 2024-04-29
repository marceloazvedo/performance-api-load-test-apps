package br.com.marcelo.reactive.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class SellerClient(
    @Value("\${clients.seller}")
    private val sellerUrl: String,
    private val webClientBuilder: WebClient.Builder,
) {
    private val webClient: WebClient = webClientBuilder.baseUrl(sellerUrl).build()

    fun validateToken(
        @RequestParam("authorization-token") authorizationToken: String,
    ): Mono<String> =
        webClient.get().uri("/sellers/authorization")
            .retrieve()
            .bodyToMono(String::class.java)
}
