package com.example.soap;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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


    public String cedulaUsuario;

    EditText et_correo, et_clave;
    Button Btnlog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Btnlog=findViewById(R.id.btn_lg);
        et_correo=findViewById(R.id.txtu);
        et_clave=findViewById(R.id.txtc);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("per_cedula")) {
            cedulaUsuario = extras.getString("per_cedula");
            // Hacer lo que necesites con la cédula del usuario
        }


        Btnlog.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                validarUsuario("https://prestamocomputadora.000webhostapp.com/pruebasoap/login.php");
            }
        });
    }



    private void validarUsuario(String URL){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        // Suponiendo que la clave para la cédula en el JSON de respuesta es "per_cedula"
                        String cedulaUsuario = jsonObject.getString("per_cedula");

                        // Mostrar mensaje Toast con la cédula del usuario
                        Toast.makeText(Login.this, "Cédula del usuario: " + cedulaUsuario, Toast.LENGTH_LONG).show();

                        // Guardar la cédula en SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("MiPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("cedula_usuario", cedulaUsuario);
                        editor.apply();

                        Intent intent = new Intent(Login.this, Inicio.class);
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(Login.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Login.this, "Correo o clave incorrectos", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(Login.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros=new HashMap<String,String>();
                parametros.put("correo",et_correo.getText().toString());
                parametros.put("clave",et_clave.getText().toString());
                return parametros;
            }
        };
        RequestQueue requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }





    public void finalizar(View v){
        this.finish();
    }
}