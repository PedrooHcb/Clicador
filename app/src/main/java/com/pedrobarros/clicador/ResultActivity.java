package com.pedrobarros.clicador;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

    private MyDBHelper dbHelper;
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

        dbHelper = new MyDBHelper(this);
        // Salvar o tempo no banco de dados
        saveTimeToDatabase(timeTaken);

        // Ler o tempo do arquivo
        long storedTime = readTimeFromDatabase();
        if (storedTime > 0) {
            Toast.makeText(this, "Tempo armazenado no banco de dados: " + storedTime, Toast.LENGTH_SHORT).show();
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
    private void saveTimeToDatabase(long timeTaken) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("time", timeTaken);

        long newRowId = db.insert("time_table", null, values);
    }

    private long readTimeFromDatabase() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {"time"};
        Cursor cursor = db.query("time_table", projection, null, null, null, null, null);

        long storedTime = 0;

        if (cursor.moveToNext()) {
            storedTime = cursor.getLong(cursor.getColumnIndexOrThrow("time"));
        }

        cursor.close();
        return storedTime;
    }
}

