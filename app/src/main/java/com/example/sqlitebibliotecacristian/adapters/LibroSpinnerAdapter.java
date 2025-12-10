package com.example.sqlitebibliotecacristian.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sqlitebibliotecacristian.objetos.Libro;

import java.util.List;

public class LibroSpinnerAdapter extends BaseAdapter {
    private Context context;
    private List<Libro> listaLibros;

    public LibroSpinnerAdapter(Context context, List<Libro> listaLibros) {
        this.context = context;
        this.listaLibros = listaLibros;
    }

    @Override
    public int getCount() {
        return listaLibros.size();
    }

    @Override
    public Libro getItem(int position) {
        return listaLibros.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listaLibros.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    android.R.layout.simple_spinner_item, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(listaLibros.get(position).getTitulo());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(listaLibros.get(position).getTitulo());

        return convertView;
    }
}