package com.example.soap;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class DetallePrestamo extends AppCompatActivity {

    public String cedulaUsuario, numeroSerie;
    Button btn_pedir, btb_devolver;

    EditText com_serie,com_marca,com_so,com_ram,com_almacenamiento,cdu;
    final Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_prestamo);
        btb_devolver=findViewById(R.id.btnDevolver);
        btn_pedir=findViewById(R.id.btnPedir);
        numeroSerie = getIntent().getStringExtra("NUMERO_SERIE");
        com_serie = findViewById(R.id.txtSerie);
        com_serie.setText(numeroSerie);
        obtenerDatos(numeroSerie);
        cdu=findViewById(R.id.txt_cedulaP);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("cedula_usuario")) {
            cedulaUsuario = extras.getString("cedula_usuario");
            cdu.setText(cedulaUsuario);

        }

        Intent intent = getIntent();

        // Verificar si contiene el extra "cedula_usuario"
        if (intent.hasExtra("cedula_usuario")) {
            // Obtener la cédula del extra
            String cedulaUsuario = intent.getStringExtra("cedula_usuario");

            // Buscar el campo EditText en el diseño y establecer el valor de la cédula
            cdu = findViewById(R.id.txt_cedulaP);
            cdu.setText(cedulaUsuario);
        }



        btn_pedir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cedula = cedulaUsuario;
                String serie = numeroSerie;

                hacerPrestamo(cedula, serie);
            }
        });
    }

    public void obtenerDatos(String numeroSerie){
        String url = "https://prestamocomputadora.000webhostapp.com/pruebasoap/TraerComputadora.php?com_serie=" + numeroSerie;
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);


                            if (jsonObject.getString("success").equals("1")) {
                                JSONObject datosObject = jsonObject.getJSONArray("datos").getJSONObject(0);

                                String marca = datosObject.getString("com_marca");
                                String ram = datosObject.getString("com_ram");
                                String almacenamiento=datosObject.getString("com_almacenamiento");
                                String so = datosObject.getString("com_so");
                                // Encontrar los EditText en el layout y asignarles los valores
                                com_marca = findViewById(R.id.txtMarca);
                                com_ram = findViewById(R.id.txtRam);
                                com_almacenamiento=findViewById(R.id.txtAlmacenamiento);
                                com_so = findViewById(R.id.txtSO);
                                com_marca.setText(marca);
                                com_ram.setText(ram);
                                com_almacenamiento.setText(almacenamiento);
                                com_so.setText(so);
                            } else {
                                Toast.makeText(DetallePrestamo.this, "Error: No se encontraron datos", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DetallePrestamo.this, "Error en la conexión", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    public void realizarPrestamo(String cedula, String serie) {
        String url = "https://prestamocomputadora.000webhostapp.com/pruebasoap/registrarPrestamo.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");


                            if (status.equals("success")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("Felicitaciones");
                                builder.setMessage( "Préstamo realizado con éxito, el tiempo máximo de devolución de la laptop es de 2 días");
                                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        dialogInterface.dismiss();
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            } else {



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

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("cedula", cedula);
                params.put("serie", serie);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    public void hacerPrestamo(String cedula, String serie) {
        realizarPrestamo(cedula, serie);
    }

    public void finalizar(View v){
        this.finish();
    }
}