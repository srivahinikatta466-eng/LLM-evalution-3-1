from flask import Flask, request, jsonify
import time

app = Flask(__name__)


@app.route("/evaluate", methods=["POST"])
def evaluate():

    data = request.get_json()

    prompt = data.get("prompt", "")
    model = data.get("model", "Demo Model")

    if not prompt:
        return jsonify({
            "error": "Prompt is empty"
        }), 400

    start_time = time.time()

    # Demo response without any API key
    response = (
        "This is a backend-generated response. "
        "Your prompt was successfully received by the server."
    )

    # Demo evaluation values
    accuracy = 0.90
    coherence = 0.88
    perplexity = 12.5

    response_time = time.time() - start_time

    return jsonify({
        "response": response,
        "accuracy": accuracy,
        "coherence": coherence,
        "perplexity": perplexity,
        "response_time": response_time
    })


if __name__ == "__main__":
    app.run(
        host="0.0.0.0",
        port=5000,
        debug=True
    )