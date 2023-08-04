package com.example.soap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

    private ListView list;
    Adaptador adaptador;
    public static ArrayList<Computadora>computadoras=new ArrayList<>();
    Button btn_cargar;

    Computadora compu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        list=findViewById(R.id.listView);
        adaptador=new Adaptador(this,computadoras);
        list.setAdapter(adaptador);
        btn_cargar=findViewById(R.id.btn_cargars);
        btn_cargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDatos("https://prestamocomputadora.000webhostapp.com/pruebasoap/mostrar.php");
            }
        });
    }

    public void mostrarDatos(String url){

        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                computadoras.clear();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String succes=jsonObject.getString("succes");

                    JSONArray jsonArray=jsonObject.getJSONArray("datos");

                    if(succes.equals("i")){

                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject object=jsonArray.getJSONObject(i);
                            String com_serie=object.getString("com_serie");
                            String com_marca=object.getString("com_marca");
                            String com_estado=object.getString("com_estado");

                            compu=new  Computadora(com_serie,com_marca,com_estado);
                            computadoras.add(compu);
                            adaptador.notifyDataSetChanged();
                        }
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



}