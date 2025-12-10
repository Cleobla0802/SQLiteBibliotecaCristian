package com.example.sqlitebibliotecacristian.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqlitebibliotecacristian.DAO.UsuarioDAO;
import com.example.sqlitebibliotecacristian.R;
import com.example.sqlitebibliotecacristian.adapters.UsuarioAdapter;
import com.example.sqlitebibliotecacristian.objetos.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


public class UsuarioActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton fabAdd;
    private UsuarioDAO usuarioDAO;
    private List<Usuario> listaUsuarios;
    private UsuarioAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        getSupportActionBar().setTitle("Gestión de Usuarios");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerViewUsuarios);
        fabAdd = findViewById(R.id.fabAddUsuario);

        usuarioDAO = new UsuarioDAO(this);

        configurarRecyclerView();
        cargarDatos();

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(UsuarioActivity.this, UsuarioFormActivity.class);
            startActivity(intent);
        });
    }

    private void configurarRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void cargarDatos() {
        listaUsuarios = usuarioDAO.obtenerTodos();
        adapter = new UsuarioAdapter(this, listaUsuarios);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new UsuarioAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(int position) {
                Usuario usuario = listaUsuarios.get(position);
                Intent intent = new Intent(UsuarioActivity.this, UsuarioFormActivity.class);
                intent.putExtra("usuario_id", usuario.getId());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(int position) {
                mostrarDialogoEliminar(position);
            }
        });
    }

    private void mostrarDialogoEliminar(int position) {
        Usuario usuario = listaUsuarios.get(position);

        new AlertDialog.Builder(this)
                .setTitle("Eliminar Usuario")
                .setMessage("¿Está seguro de eliminar al usuario '" + usuario.getNombre() + "'?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    int resultado = usuarioDAO.eliminar(usuario.getId());
                    if (resultado > 0) {
                        Toast.makeText(this, "Usuario eliminado", Toast.LENGTH_SHORT).show();
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