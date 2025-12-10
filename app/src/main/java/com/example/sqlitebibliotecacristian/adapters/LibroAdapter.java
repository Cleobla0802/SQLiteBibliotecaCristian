package com.example.sqlitebibliotecacristian.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqlitebibliotecacristian.R;
import com.example.sqlitebibliotecacristian.objetos.Libro;

import java.util.List;

public class LibroAdapter extends RecyclerView.Adapter<LibroAdapter.ViewHolder> {

    private List<Libro> listaLibros;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    public LibroAdapter(Context context, List<Libro> listaLibros) {
        this.context = context;
        this.listaLibros = listaLibros;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_libro, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Libro libro = listaLibros.get(position);
        holder.tvTitulo.setText(libro.getTitulo());
        holder.tvAutor.setText("Autor: " + libro.getAutor());
        holder.tvIsbn.setText("ISBN: " + libro.getIsbn());
        holder.tvCategoria.setText("Categoría: " + libro.getCategoriaNombre());
        holder.tvDisponible.setText(libro.isDisponible() ? "Disponible" : "Prestado");

        // Cambiar color de disponibilidad
        int color = libro.isDisponible() ?
                context.getResources().getColor(android.R.color.holo_green_dark) :
                context.getResources().getColor(android.R.color.holo_orange_dark);
        holder.tvDisponible.setTextColor(color);
    }

    @Override
    public int getItemCount() {
        return listaLibros.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvAutor, tvIsbn, tvCategoria, tvDisponible;
        ImageButton btnEditar, btnEliminar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTituloLibro);
            tvAutor = itemView.findViewById(R.id.tvAutorLibro);
            tvIsbn = itemView.findViewById(R.id.tvIsbnLibro);
            tvCategoria = itemView.findViewById(R.id.tvCategoriaLibro);
            tvDisponible = itemView.findViewById(R.id.tvDisponibleLibro);
            btnEditar = itemView.findViewById(R.id.btnEditarLibro);
            btnEliminar = itemView.findViewById(R.id.btnEliminarLibro);

            btnEditar.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onEditClick(position);
                    }
                }
            });

            btnEliminar.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onDeleteClick(position);
                    }
                }
            });
        }
    }
}