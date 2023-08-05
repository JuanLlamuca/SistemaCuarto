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

import org.json.JSONException;
import org.json.JSONObject;

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
        correo=findViewById(R.id.txt_correo);
        telefono=findViewById(R.id.txt_telefono);
        direccion=findViewById(R.id.txt_direccion);
        clave=findViewById(R.id.txt_clave);
        btnGuardar=findViewById(R.id.btn_guardar);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ejecutarservicio("https://prestamocomputadora.000webhostapp.com/pruebasoap/insertar.php");
            }
        });

    }

    private void ejecutarservicio(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // Convertir la respuesta JSON a un objeto JSON
                    JSONObject jsonObject = new JSONObject(response);

                    // Obtener el estado y el mensaje del objeto JSON
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    // Código para manejar la respuesta del servidor
                    if (status.equals("success")) {
                        // Si el estado es "success", entonces la inserción fue exitosa
                        cedula.setText("");
                        nombres.setText("");
                        apellidos.setText("");
                        correo.setText("");
                        telefono.setText("");
                        direccion.setText("");
                        clave.setText("");

                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    } else {
                        // Si el estado es "error", hubo un problema en la inserción
                        Log.e("Error", message);

                        TextView errorTextView = findViewById(R.id.errorTextView);
                        errorTextView.setVisibility(View.VISIBLE);
                        errorTextView.setText(message);
                    }
                } catch (JSONException e) {
                    // Si ocurre un error al analizar la respuesta JSON
                    Log.e("Error", "Error al analizar la respuesta JSON: " + e.getMessage());

                    TextView errorTextView = findViewById(R.id.errorTextView);
                    errorTextView.setVisibility(View.VISIBLE);
                    errorTextView.setText("Error al procesar la respuesta del servidor.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Si ocurre un error en la solicitud HTTP
                Log.e("Error", "Error en la solicitud HTTP: " + error.toString());

                TextView errorTextView = findViewById(R.id.errorTextView);
                errorTextView.setVisibility(View.VISIBLE);
                errorTextView.setText("Error en la conexión con el servidor.");
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Código para obtener los parámetros de la solicitud, como ya tienes en tu código original
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("per_cedula", cedula.getText().toString());
                parametros.put("per_nombres", nombres.getText().toString());
                parametros.put("per_apellidos", apellidos.getText().toString());
                parametros.put("per_correo", correo.getText().toString());
                parametros.put("per_telefono", telefono.getText().toString());
                parametros.put("per_direccion", direccion.getText().toString());
                parametros.put("per_clave", clave.getText().toString());

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