package br.com.marcelo.traditional.client

import br.com.marcelo.traditional.dto.RiskAnalysisRecommendation
import br.com.marcelo.traditional.dto.TransactionDataDTO
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "riskAnalysisClient", url = "\${clients.risk-analysis}")
interface RiskAnalysisClient {
    @PostMapping("/risk-analysis/validate")
    fun analyseTransaction(
        @RequestBody transactionData: TransactionDataDTO,
    ): RiskAnalysisRecommendation?
}
