package sri.example.llmevaluation.model

data class EvaluationResponse(
    val response: String,
    val accuracy: Double,
    val coherence: Double,
    val perplexity: Double,
    val response_time: Double
)