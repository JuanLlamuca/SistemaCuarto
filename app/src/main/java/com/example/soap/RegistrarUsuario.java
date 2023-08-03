package com.example.soap;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class RegistrarUsuario extends AppCompatActivity {

    EditText cedula,nombres,apellidos,correo,telefono,direccion,clave;
    Button btnGuardar;
    RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);
        cedula=findViewById(R.id.txt_cedula);
        nombres=findViewById(R.id.txt_nombres);
        apellidos=findViewById(R.id.txt_apellidos);
        correo=findViewById(R.id.txt_telefono);
        telefono=findViewById(R.id.txt_correo);
        direccion=findViewById(R.id.txt_direccion);
        clave=findViewById(R.id.txt_clave);
        btnGuardar=findViewById(R.id.btn_guardar);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ejecutarservicio("http://10.0.2.2/pruebasoap/insertar.php");
            }
        });

    }

    private void ejecutarservicio(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                cedula.setText("");
                nombres.setText("");
                apellidos.setText("");
                correo.setText("");
                telefono.setText("");
                direccion.setText("");
                clave.setText("");
                // Código para manejar la respuesta exitosa
                Toast.makeText(getApplicationContext(), "usuario ingresado correctamente", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Imprimir el mensaje de error en el registro (Log)
                Log.e("Error", error.toString());

                // Mostrar el mensaje de error en el TextView
                TextView errorTextView = findViewById(R.id.errorTextView);
                errorTextView.setVisibility(View.VISIBLE);
                errorTextView.setText(error.toString());
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Código para obtener los parámetros de la solicitud, como ya tienes en tu código original
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("cedula",cedula.getText().toString());
                parametros.put("nombres",nombres.getText().toString());
                parametros.put("apellidos",apellidos.getText().toString());
                parametros.put("correo",correo.getText().toString());
                parametros.put("telefono",telefono.getText().toString());
                parametros.put("direccion",direccion.getText().toString());
                parametros.put("clave",clave.getText().toString());

                return parametros;
            }
        };

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    public void finalizar(View view){
        this.finish();
    }
}