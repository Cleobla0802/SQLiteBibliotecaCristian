package com.example.sqlitebibliotecacristian.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sqlitebibliotecacristian.R;


public class MainActivity extends AppCompatActivity {

    private Button btnCategorias, btnUsuarios, btnLibros, btnPrestamos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCategorias = findViewById(R.id.btnCategorias);
        btnUsuarios = findViewById(R.id.btnUsuarios);
        btnLibros = findViewById(R.id.btnLibros);
        btnPrestamos = findViewById(R.id.btnPrestamos);

        btnCategorias.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CategoriaActivity.class);
            startActivity(intent);
        });

        btnUsuarios.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, UsuarioActivity.class);
            startActivity(intent);
        });

        btnLibros.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LibroActivity.class);
            startActivity(intent);
        });

        btnPrestamos.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PrestamoActivity.class);
            startActivity(intent);
        });
    }
}