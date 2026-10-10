package sri.example.llmevaluation

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import sri.example.llmevaluation.model.EvaluationResponse
import sri.example.llmevaluation.model.PromptRequest
import sri.example.llmevaluation.network.RetrofitClient

class MainActivity : AppCompatActivity() {

    private lateinit var etPrompt: EditText
    private lateinit var spModel: Spinner
    private lateinit var btnEvaluate: Button
    private lateinit var tvOutput: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etPrompt = findViewById(R.id.etPrompt)
        spModel = findViewById(R.id.spModel)
        btnEvaluate = findViewById(R.id.btnEvaluate)
        tvOutput = findViewById(R.id.tvOutput)

        val models = arrayOf(
            "Gemini",
            "Groq - Llama 3",
            "OpenAI GPT"
        )

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            models
        )

        spModel.adapter = adapter

        btnEvaluate.setOnClickListener {

            val prompt = etPrompt.text.toString().trim()

            if (prompt.isEmpty()) {
                Toast.makeText(
                    this,
                    "Please enter a prompt",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val model = spModel.selectedItem.toString()

            btnEvaluate.isEnabled = false
            btnEvaluate.text = "EVALUATING..."

            tvOutput.text = "Connecting to backend..."

            val request = PromptRequest(
                prompt = prompt,
                model = model
            )

            RetrofitClient.api.evaluate(request)
                .enqueue(object : Callback<EvaluationResponse> {

                    override fun onResponse(
                        call: Call<EvaluationResponse>,
                        response: Response<EvaluationResponse>
                    ) {

                        btnEvaluate.isEnabled = true
                        btnEvaluate.text = "EVALUATE"

                        if (response.isSuccessful) {

                            val result = response.body()

                            if (result != null) {

                                tvOutput.text = """
                                    Response
                                    
                                    ${result.response}
                                    
                                    ─────────────────────
                                    
                                    Model: $model
                                    
                                    Accuracy: ${result.accuracy}
                                    Coherence: ${result.coherence}
                                    Perplexity: ${result.perplexity}
                                    Response Time: ${"%.3f".format(result.response_time)} sec
                                """.trimIndent()

                            } else {
                                tvOutput.text = "Empty response from server."
                            }

                        } else {

                            tvOutput.text =
                                "Server Error: ${response.code()}"
                        }
                    }

                    override fun onFailure(
                        call: Call<EvaluationResponse>,
                        t: Throwable
                    ) {

                        btnEvaluate.isEnabled = true
                        btnEvaluate.text = "EVALUATE"

                        tvOutput.text =
                            "Connection Failed\n\n${t.message}"
                    }
                })
        }
    }
}