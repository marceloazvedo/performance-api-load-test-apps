package br.com.marcelo.reactive.dto

data class RiskAnalysisRecommendation(
    val recommendation: AnalysisRecommendation? = null,
)

enum class AnalysisRecommendation {
    APPROVE,
    DISAPPROVE,
}
