package com.example.sqlitebibliotecacristian.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqlitebibliotecacristian.R;
import com.example.sqlitebibliotecacristian.objetos.Prestamo;

import java.util.List;
public class PrestamoAdapter extends RecyclerView.Adapter<PrestamoAdapter.ViewHolder> {

    private List<Prestamo> listaPrestamos;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onDevolucionClick(int position);
        void onDeleteClick(int position);
    }

    public PrestamoAdapter(Context context, List<Prestamo> listaPrestamos) {
        this.context = context;
        this.listaPrestamos = listaPrestamos;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_prestamo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Prestamo prestamo = listaPrestamos.get(position);
        holder.tvLibro.setText("Libro: " + prestamo.getLibroTitulo());
        holder.tvUsuario.setText("Usuario: " + prestamo.getUsuarioNombre());
        holder.tvFechaPrestamo.setText("Préstamo: " + prestamo.getFechaPrestamo());

        if (prestamo.isDevuelto()) {
            holder.tvEstado.setText("Devuelto");
            holder.tvEstado.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
            holder.tvFechaDevolucion.setText("Devolución: " + prestamo.getFechaDevolucion());
            holder.tvFechaDevolucion.setVisibility(View.VISIBLE);
            holder.btnDevolver.setVisibility(View.GONE);
        } else {
            holder.tvEstado.setText("Activo");
            holder.tvEstado.setTextColor(context.getResources().getColor(android.R.color.holo_orange_dark));
            holder.tvFechaDevolucion.setVisibility(View.GONE);
            holder.btnDevolver.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return listaPrestamos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvLibro, tvUsuario, tvFechaPrestamo, tvFechaDevolucion, tvEstado;
        Button btnDevolver;
        ImageButton btnEliminar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLibro = itemView.findViewById(R.id.tvLibroPrestamo);
            tvUsuario = itemView.findViewById(R.id.tvUsuarioPrestamo);
            tvFechaPrestamo = itemView.findViewById(R.id.tvFechaPrestamo);
            tvFechaDevolucion = itemView.findViewById(R.id.tvFechaDevolucion);
            tvEstado = itemView.findViewById(R.id.tvEstadoPrestamo);
            btnDevolver = itemView.findViewById(R.id.btnDevolverPrestamo);
            btnEliminar = itemView.findViewById(R.id.btnEliminarPrestamo);

            btnDevolver.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onDevolucionClick(position);
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