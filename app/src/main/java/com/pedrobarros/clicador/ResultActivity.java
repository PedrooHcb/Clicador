package com.pedrobarros.clicador;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

public class ResultActivity extends AppCompatActivity {
    private TextView jokeTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView tvResult = findViewById(R.id.tvResult);

        jokeTextView = findViewById(R.id.jokeTextView);
        requestChuckNorrisJoke();


        // Recebe o tempo do clique na tela do jogo
        long timeTaken = getIntent().getLongExtra("timeTaken", 0);
        double seconds = (double) timeTaken / 1000; // Convertendo milissegundos para segundos

        String resultText = getString(R.string.result_text, seconds);
        tvResult.setText(resultText);
    }

    private void requestChuckNorrisJoke() {
        String url = "https://api.chucknorris.io/jokes/random";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    try {
                        String joke = response.getString("value"); // Pega o texto da piada do JSON response

                        jokeTextView.setText(joke); // Mostra a piada
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    Toast.makeText(ResultActivity.this, "Erro ao obter a piada.", Toast.LENGTH_SHORT).show(); // Tratativa de erro
                });

        RequestQueue queue = Volley.newRequestQueue(this); // Add um solicitação a fila
        queue.add(jsonObjectRequest);
    }
}

