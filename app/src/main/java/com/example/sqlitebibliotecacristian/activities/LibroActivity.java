package com.example.sqlitebibliotecacristian.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqlitebibliotecacristian.DAO.LibroDAO;
import com.example.sqlitebibliotecacristian.R;
import com.example.sqlitebibliotecacristian.adapters.LibroAdapter;
import com.example.sqlitebibliotecacristian.objetos.Libro;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class LibroActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton fabAdd;
    private LibroDAO libroDAO;
    private List<Libro> listaLibros;
    private LibroAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_libro);

        getSupportActionBar().setTitle("Gestión de Libros");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerViewLibros);
        fabAdd = findViewById(R.id.fabAddLibro);

        libroDAO = new LibroDAO(this);

        configurarRecyclerView();
        cargarDatos();

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(LibroActivity.this, LibroFormActivity.class);
            startActivity(intent);
        });
    }

    private void configurarRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void cargarDatos() {
        listaLibros = libroDAO.obtenerTodos();
        adapter = new LibroAdapter(this, listaLibros);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new LibroAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(int position) {
                Libro libro = listaLibros.get(position);
                Intent intent = new Intent(LibroActivity.this, LibroFormActivity.class);
                intent.putExtra("libro_id", libro.getId());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(int position) {
                mostrarDialogoEliminar(position);
            }
        });
    }

    private void mostrarDialogoEliminar(int position) {
        Libro libro = listaLibros.get(position);

        new AlertDialog.Builder(this)
                .setTitle("Eliminar Libro")
                .setMessage("¿Está seguro de eliminar el libro '" + libro.getTitulo() + "'?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    int resultado = libroDAO.eliminar(libro.getId());
                    if (resultado > 0) {
                        Toast.makeText(this, "Libro eliminado", Toast.LENGTH_SHORT).show();
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