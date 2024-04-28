package br.com.marcelo.reactive.client

import br.com.marcelo.reactive.dto.RiskAnalysisRecommendation
import br.com.marcelo.reactive.dto.TransactionDataDTO
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class RiskAnalysisClient(
    @Value("\${clients.risk-analysis}")
    private val riskAnalysisUrl: String,
    private val webClientBuilder: WebClient.Builder
) {
    private val webClient: WebClient = webClientBuilder.baseUrl(riskAnalysisUrl).build()
    fun analyseTransaction(
        @RequestBody transactionData: TransactionDataDTO,
    ): Mono<RiskAnalysisRecommendation>
    = webClient.post()
        .uri("/risk-analysis/validate")
        .body(BodyInserters.fromValue(transactionData))
        .retrieve()
        .bodyToMono(RiskAnalysisRecommendation::class.java)
}
