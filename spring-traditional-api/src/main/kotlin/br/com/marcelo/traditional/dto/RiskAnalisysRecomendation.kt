package br.com.marcelo.traditional.dto

data class RiskAnalysisRecommendation(
    val recommendation: AnalysisRecommendation? = null,
)

enum class AnalysisRecommendation {
    APPROVE,
    DISAPPROVE,
}
