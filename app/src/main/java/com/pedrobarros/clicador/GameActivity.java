package com.pedrobarros.clicador;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private View gameView;
    private boolean colorChanged = false;
    private long startTime, endTime;
    private boolean clickBeforeChange = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameView = findViewById(R.id.gameView);
        gameView.setOnClickListener(v -> {
            if (colorChanged) {
                endTime = System.currentTimeMillis();
                long timeTaken = endTime - startTime;
                if (!clickBeforeChange) {
                    Intent intent = new Intent(GameActivity.this, ResultActivity.class);
                    intent.putExtra("timeTaken", timeTaken);
                    startActivity(intent);
                    finish();
                } else {
                    // Se o player clicar antes da cor mudar
                    showConfirmationDialog(timeTaken);
                }
            } else {
                clickBeforeChange = true; // O player clicou antes da mudança de cor
            }
        });

        new Handler().postDelayed(() -> {
            gameView.setBackgroundColor(getColor(R.color.black));
            colorChanged = true;
            startTime = System.currentTimeMillis();
        }, getRandomDelay());
    }

    private void showConfirmationDialog(long timeTaken) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Você clicou antes da mudança de cor. Deseja continuar?")
                .setCancelable(false)
                .setPositiveButton("Sim", (dialog, id) -> {
                    clickBeforeChange = false;
                    restartGame(); // Reiniciar o game no caso de clique antes da hora
                })
                .setNegativeButton("Não", (dialog, id) -> {
                    // Vai para tela de resultados
                    Intent intent = new Intent(GameActivity.this, ResultActivity.class);
                    intent.putExtra("timeTaken", timeTaken);
                    startActivity(intent);
                    finish();
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void restartGame() {
        // Reiniciar a mudança de cor e o contador para a próxima mudança
        gameView.setBackgroundColor(Color.WHITE); // Volta para a cor padrão
        colorChanged = false; // Reseta o estado de mudança de cor
        new Handler().postDelayed(() -> {
            gameView.setBackgroundColor(getRandomColor());
            colorChanged = true;
            startTime = System.currentTimeMillis();
        }, getRandomDelay());
    }

    private int getRandomColor() {
        Random random = new Random();
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        return Color.rgb(r, g, b);
    }

    private long getRandomDelay() {
        Random random = new Random();
        return random.nextInt(4000 - 2000) + 2000;
    }
}

