package com.example.soap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Devolucion extends AppCompatActivity {

    Button BtnRegresar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devolucion);
        BtnRegresar=findViewById(R.id.btnRegresarDev);
    }

    public void Finalizar(View v){
        this.finish();
    }


}