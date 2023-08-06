package com.example.soap;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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

public class Devolucion extends AppCompatActivity {

    EditText com_serie, cdu,com_serieEditText;
    public String cedulaUsuario;
    Button btnDevo;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devolucion);

        cdu = findViewById(R.id.txtCedulaDev);
        com_serie = findViewById(R.id.txtSerieDev);
        btnDevo=findViewById(R.id.btnDevolverDev);

        // Obtener el valor de la cédula desde el Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            cedulaUsuario = extras.getString("cedula_usuario");
            cdu.setText(cedulaUsuario); // Mostrar la cédula en el EditText

            // Llamar al método para obtener el número de serie
            ObtenerSerie(cedulaUsuario);
        }

        btnDevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serie = com_serie.getText().toString();

                // Llamar a la función Devoluciones pasando la cédula del usuario y el número de serie
                Devoluciones(cedulaUsuario, serie);
            }
        });

    }

    public void ObtenerSerie(String cedulaUsuario) {
        String url = "https://prestamocomputadora.000webhostapp.com/pruebasoap/buscarSerie.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ObtenerSerie", "Respuesta del servidor: " + response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if (status.equals("success")) {
                                String com_serie = jsonObject.getString("com_serie");
                                Log.d("ObtenerSerie", "Número de serie recibido: " + com_serie);

                                // Mostrar el número de serie en el EditText
                                com_serieEditText = findViewById(R.id.txtSerieDev);
                                com_serieEditText.setText(com_serie);
                            } else {
                                Toast.makeText(Devolucion.this, "Error: No se encontraron datos", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Devolucion.this, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Devolucion.this, "Error en la conexión", Toast.LENGTH_SHORT).show();
                        Log.e("ObtenerSerie", "Error de conexión: " + error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("cedula", cedulaUsuario); // Agrega la cédula que recibiste como parámetro a los parámetros de la solicitud POST
                return params;
            }
        };

        // Agregar el tiempo de espera (opcional, pero útil para evitar problemas de tiempo de espera en algunas conexiones)
        request.setRetryPolicy(new DefaultRetryPolicy(
                5000,  // tiempo de espera en milisegundos
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        // Agregar la solicitud a la cola de peticiones de Volley
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    public void Devoluciones(String cedula, String serie) {
        String url = "https://prestamocomputadora.000webhostapp.com/pruebasoap/registrarDevolucion.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    com_serieEditText = findViewById(R.id.txtSerieDev);
                    com_serieEditText.setText("");


                    if (message == null || message.isEmpty()) {
                        message = "El servidor ha retornado un mensaje vacío.";
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Ups!");
                    builder.setMessage(message);

                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                } catch (JSONException e) {

                    Log.e("Error", "Error al analizar la respuesta JSON: " + e.getMessage());


                    Toast.makeText(getApplicationContext(), "Error al procesar la respuesta del servidor.", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Si ocurre un error en la solicitud HTTP
                Log.e("Error", "Error en la solicitud HTTP: " + error.toString());

                // Mostrar el mensaje de error utilizando un Toast
                Toast.makeText(getApplicationContext(), "Error en la conexión con el servidor.", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Código para obtener los parámetros de la solicitud
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("cedula", cedula);
                parametros.put("serie", serie);

                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }



    public void Finalizar(View v) {
        this.finish();
    }
}
