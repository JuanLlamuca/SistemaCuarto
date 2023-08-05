package com.example.soap;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class Adaptador extends ArrayAdapter<Computadora> {

    Context context;
    List<Computadora>arrayalistaComputadoras;

    public Adaptador(@NonNull Context context, List<Computadora>arrayalistaComputadoras) {
        super(context,R.layout.my_list_item,arrayalistaComputadoras);

        this.context=context;
        this.arrayalistaComputadoras=arrayalistaComputadoras;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.my_list_item,null,true);
        TextView tvid=view.findViewById(R.id.txt_id);
        TextView tvnombre=view.findViewById(R.id.txt_nombre);

        tvid.setText(arrayalistaComputadoras.get(position).getCom_serie());
        tvnombre.setText(arrayalistaComputadoras.get(position).getCom_marca());
        return view;
    }
}
