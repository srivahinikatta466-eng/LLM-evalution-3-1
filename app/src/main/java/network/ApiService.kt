package sri.example.llmevaluation.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import sri.example.llmevaluation.model.EvaluationResponse
import sri.example.llmevaluation.model.PromptRequest

interface ApiService {

    @POST("evaluate")
    fun evaluate(
        @Body request: PromptRequest
    ): Call<EvaluationResponse>
}