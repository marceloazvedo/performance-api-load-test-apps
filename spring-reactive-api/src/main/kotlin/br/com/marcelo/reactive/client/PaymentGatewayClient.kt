package br.com.marcelo.reactive.client

import br.com.marcelo.reactive.dto.PaymentMethodDTO
import br.com.marcelo.reactive.dto.PaymentResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class PaymentGatewayClient(
    @Value("\${clients.payment-gateway}")
    private val gatewayUrl: String,
    private val webClientBuilder: WebClient.Builder,
) {

    private val webClient: WebClient = webClientBuilder.baseUrl(gatewayUrl).build()

    fun pay(paymentMethodDTO: PaymentMethodDTO): Mono<PaymentResponse> =
        webClient.post()
            .uri("/gateway/pay")
            .body(BodyInserters.fromValue(paymentMethodDTO))
            .retrieve()
            .bodyToMono(PaymentResponse::class.java)
}
