package com.example.soap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Inicio extends AppCompatActivity {

    Button btn_pres,btn_dev;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        btn_pres = findViewById(R.id.btnSoli);
        btn_dev = findViewById(R.id.btnDev);


        SharedPreferences sharedPreferences = getSharedPreferences("MiPrefs", Context.MODE_PRIVATE);
        String cedulaUsuario = sharedPreferences.getString("cedula_usuario", "");

        btn_pres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Inicio.this, Principal.class);
                intent.putExtra("cedula_usuario", cedulaUsuario);
                startActivity(intent);
            }
        });

        btn_dev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Inicio.this, Devolucion.class);
                startActivity(intent);
            }
        });
    }


}