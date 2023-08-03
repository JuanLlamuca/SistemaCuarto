package com.example.soap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Principal extends AppCompatActivity {
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);


    }

    private List<Computadora> consumirWebService() {
        String url = "http://10.0.2.2/pruebasoap/buscar.php"; // Reemplaza esta URL con la dirección de tu archivo PHP

        List<Computadora> listaComputadoras = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // Parsear la respuesta JSON a una lista de objetos Computadora
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String com_serie = jsonObject.getString("com_serie");
                        String com_marca = jsonObject.getString("com_marca");
                        String com_so = jsonObject.getString("com_so");
                        listaComputadoras.add(new Computadora(com_serie, com_marca, com_so));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Mostrar el mensaje de error proporcionado por Volley
                String errorMessage = "Error en la solicitud: ";
                if (error.networkResponse != null) {
                    errorMessage += error.networkResponse.statusCode + " " + new String(error.networkResponse.data);
                } else {
                    errorMessage += error.toString();
                }
                Toast.makeText(Principal.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        return listaComputadoras;
    }
}