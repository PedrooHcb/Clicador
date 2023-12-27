package com.pedrobarros.clicador;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView tvResult = findViewById(R.id.tvResult);

        // Recebe o tempo do clique na tela do jogo
        long timeTaken = getIntent().getLongExtra("timeTaken", 0);
        double seconds = (double) timeTaken / 1000; // Convertendo milissegundos para segundos

        String resultText = getString(R.string.result_text, seconds);
        tvResult.setText(resultText);
    }
}

