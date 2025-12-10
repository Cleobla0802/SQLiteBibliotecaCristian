package com.example.sqlitebibliotecacristian.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqlitebibliotecacristian.DAO.CategoriaDAO;
import com.example.sqlitebibliotecacristian.R;
import com.example.sqlitebibliotecacristian.adapters.CategoriaAdapter;
import com.example.sqlitebibliotecacristian.objetos.Categoria;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class CategoriaActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton fabAdd;
    private CategoriaDAO categoriaDAO;
    private List<Categoria> listaCategorias;
    private CategoriaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);

        getSupportActionBar().setTitle("Gestión de Categorías");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerViewCategorias);
        fabAdd = findViewById(R.id.fabAddCategoria);

        categoriaDAO = new CategoriaDAO(this);

        configurarRecyclerView();
        cargarDatos();

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(CategoriaActivity.this, CategoriaFormActivity.class);
            startActivity(intent);
        });
    }

    private void configurarRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void cargarDatos() {
        listaCategorias = categoriaDAO.obtenerTodas();
        adapter = new CategoriaAdapter(this, listaCategorias);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new CategoriaAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(int position) {
                Categoria categoria = listaCategorias.get(position);
                Intent intent = new Intent(CategoriaActivity.this, CategoriaFormActivity.class);
                intent.putExtra("categoria_id", categoria.getId());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(int position) {
                mostrarDialogoEliminar(position);
            }
        });
    }

    private void mostrarDialogoEliminar(int position) {
        Categoria categoria = listaCategorias.get(position);

        new AlertDialog.Builder(this)
                .setTitle("Eliminar Categoría")
                .setMessage("¿Está seguro de eliminar la categoría '" + categoria.getNombre() + "'?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    int resultado = categoriaDAO.eliminar(categoria.getId());
                    if (resultado > 0) {
                        Toast.makeText(this, "Categoría eliminada", Toast.LENGTH_SHORT).show();
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