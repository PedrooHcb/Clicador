package com.pedrobarros.clicador;

import android.content.Context;
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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ResultActivity extends AppCompatActivity {
    private TextView jokeTextView;

    private static final String FILENAME = "time_data.txt";

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

        // Obter o tempo da Intent
        timeTaken = getIntent().getLongExtra("timeTaken", 0);

        // Exibir o tempo na TextView
        TextView timeTextView = findViewById(R.id.tvResult);
        timeTextView.setText("Tempo: " + timeTaken + " milissegundos");

        // Salvar o tempo em um arquivo
        saveTimeToFile(timeTaken);

        // Ler o tempo do arquivo
        String storedTime = readTimeFromFile();
        if (storedTime != null) {
            Toast.makeText(this, "Tempo armazenado: " + storedTime, Toast.LENGTH_SHORT).show();
        }
    }

    // ACESSO A API
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
    // PERSISTÊNCIA
    private void saveTimeToFile(long timeTaken) {
        try {
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            String data = String.valueOf(timeTaken);
            fos.write(data.getBytes());
            fos.close();
            Toast.makeText(this, "Tempo salvo localmente", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao salvar o tempo", Toast.LENGTH_SHORT).show();
        }
    }

    private String readTimeFromFile() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            InputStreamReader inputStreamReader = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            fis.close();
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao ler o tempo", Toast.LENGTH_SHORT).show();
            return null;
        }
    }
}

