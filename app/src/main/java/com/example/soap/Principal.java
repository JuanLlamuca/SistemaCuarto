package com.example.soap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

public class Principal extends AppCompatActivity {

     ListView listView;
    Adaptador adaptador;
    public static ArrayList<Computadora>computadoras=new ArrayList<>();
    private Handler handler = new Handler();
    private Runnable runnable;

    String url="https://prestamocomputadora.000webhostapp.com/pruebasoap/mostrar.php";

    Computadora compu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        listView=findViewById(R.id.myListView);
        adaptador=new Adaptador(this,computadoras);
        listView.setAdapter(adaptador);
        Log.d("Punto de control", "El método mostrarDatos() se llamó desde onCreate()");

        mostrarDatos();
        actualizarDatosCadaCincoSegundos();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Computadora computadoraSeleccionada = computadoras.get(position);
                String numeroSerie = computadoraSeleccionada.getCom_serie();


                SharedPreferences sharedPreferences = getSharedPreferences("MiPrefs", Context.MODE_PRIVATE);
                String cedulaUsuario = sharedPreferences.getString("cedula_usuario", "");

                Intent intent = new Intent(Principal.this, DetallePrestamo.class);
                intent.putExtra("NUMERO_SERIE", numeroSerie);
                intent.putExtra("cedula_usuario", cedulaUsuario);
                startActivity(intent);
            }
        });



    }

    public void mostrarDatos(){
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                computadoras.clear();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String success=jsonObject.getString("success");

                    JSONArray jsonArray=jsonObject.getJSONArray("datos");

                    if(success.equals("1")){

                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject object=jsonArray.getJSONObject(i);
                            String com_serie=object.getString("com_serie");
                            String com_marca=object.getString("com_marca");
                            String com_estado=object.getString("com_estado");

                            compu=new  Computadora(com_serie,com_marca,com_estado);
                            computadoras.add(compu);
                            adaptador.notifyDataSetChanged();
                        }
                        Log.d("Datos recibidos", "Cantidad de computadoras: " + computadoras.size());
                    }else {

                        // Punto de control 2: Verificar si los datos se recibieron correctamente y la cantidad de computadoras
                        Toast.makeText(Principal.this, "No existen computadoras disponibles.", Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Principal.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
    private void actualizarDatosCadaCincoSegundos() {
        runnable = new Runnable() {
            @Override
            public void run() {
                mostrarDatos();
                handler.postDelayed(this, 5000); // Repetir cada 5 segundos (5000 milisegundos)
            }
        };

        handler.postDelayed(runnable, 5000); // Inicializar la repetición después de 5 segundos
    }



}