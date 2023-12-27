package com.pedrobarros.clicador;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonLogin = findViewById(R.id.btn_login);

        TextView login = findViewById(R.id.txt_login);
        TextView senha = findViewById(R.id.txt_senha);

        buttonLogin.setOnClickListener(view -> {
            login.setText(getString(R.string.msg_teste));
            senha.setText(getString(R.string.msg_teste));
        });
    }
}