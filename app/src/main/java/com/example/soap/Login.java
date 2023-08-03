package com.example.soap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {



    EditText et_correo, et_clave;
    Button Btnlog;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Btnlog=findViewById(R.id.btn_lg);
        et_correo=findViewById(R.id.txtu);
        et_clave=findViewById(R.id.txtc);


        Btnlog.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                consultaLogin("https://prestamocomputadora.000webhostapp.com/pruebasoap/ajax/login.php?op=log");
            }
        });
    }

    public void consultaLogin(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPONSE", response);

                        try {
                            boolean recibeRespuesta = Boolean.parseBoolean(response);
                            if (recibeRespuesta) {
                                // Inicio de sesión exitoso
                                Toast.makeText(getApplicationContext(), "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                                // Ir al menú
                                Intent intent = new Intent(getApplicationContext(), Principal.class);
                                startActivity(intent);
                            } else {
                                // Inicio de sesión fallido
                                Toast.makeText(getApplicationContext(), "Inicio de sesión fallido", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            // Error al analizar la respuesta JSON
                            Toast.makeText(getApplicationContext(), "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();
                String correo = et_correo.getText().toString();
                String clave = et_clave.getText().toString();
                parametros.put("correo", correo);
                parametros.put("clave", clave);
                return parametros;
            }
        };
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }







    public void finalizar(View v){
        this.finish();
    }
}