package com.example.sqlitebibliotecacristian.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqlitebibliotecacristian.DAO.PrestamoDAO;
import com.example.sqlitebibliotecacristian.R;
import com.example.sqlitebibliotecacristian.adapters.PrestamoAdapter;
import com.example.sqlitebibliotecacristian.objetos.Prestamo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class PrestamoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton fabAdd;
    private PrestamoDAO prestamoDAO;
    private List<Prestamo> listaPrestamos;
    private PrestamoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prestamo);

        getSupportActionBar().setTitle("Gestión de Préstamos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerViewPrestamos);
        fabAdd = findViewById(R.id.fabAddPrestamo);

        prestamoDAO = new PrestamoDAO(this);

        configurarRecyclerView();
        cargarDatos();

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(PrestamoActivity.this, PrestamoFormActivity.class);
            startActivity(intent);
        });
    }

    private void configurarRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void cargarDatos() {
        listaPrestamos = prestamoDAO.obtenerTodos();
        adapter = new PrestamoAdapter(this, listaPrestamos);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new PrestamoAdapter.OnItemClickListener() {
            @Override
            public void onDevolucionClick(int position) {
                mostrarDialogoDevolucion(position);
            }

            @Override
            public void onDeleteClick(int position) {
                mostrarDialogoEliminar(position);
            }
        });
    }

    private void mostrarDialogoDevolucion(int position) {
        Prestamo prestamo = listaPrestamos.get(position);

        new AlertDialog.Builder(this)
                .setTitle("Registrar Devolución")
                .setMessage("¿Confirmar la devolución del libro?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    String fechaActual = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            .format(new Date());
                    int resultado = prestamoDAO.marcarComoDevuelto(prestamo.getId(), fechaActual);
                    if (resultado > 0) {
                        Toast.makeText(this, "Devolución registrada", Toast.LENGTH_SHORT).show();
                        cargarDatos();
                    } else {
                        Toast.makeText(this, "Error al registrar devolución", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
    private void mostrarDialogoEliminar(int position) {
        Prestamo prestamo = listaPrestamos.get(position);

        new AlertDialog.Builder(this)
                .setTitle("Eliminar Préstamo")
                .setMessage("¿Está seguro de eliminar este préstamo?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    int resultado = prestamoDAO.eliminar(prestamo.getId());
                    if (resultado > 0) {
                        Toast.makeText(this, "Préstamo eliminado", Toast.LENGTH_SHORT).show();
                        cargarDatos();
                    } else {
                        Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarDatos();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}