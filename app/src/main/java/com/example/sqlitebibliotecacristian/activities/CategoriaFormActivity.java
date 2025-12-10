package com.example.sqlitebibliotecacristian.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sqlitebibliotecacristian.DAO.CategoriaDAO;
import com.example.sqlitebibliotecacristian.R;
import com.example.sqlitebibliotecacristian.objetos.Categoria;


public class CategoriaFormActivity extends AppCompatActivity {

    private EditText etNombre, etDescripcion, etColor;
    private Button btnGuardar, btnCancelar;
    private CategoriaDAO categoriaDAO;
    private int categoriaId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria_form);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etNombre = findViewById(R.id.etNombreCategoria);
        etDescripcion = findViewById(R.id.etDescripcionCategoria);
        etColor = findViewById(R.id.etColorCategoria);
        btnGuardar = findViewById(R.id.btnGuardarCategoria);
        btnCancelar = findViewById(R.id.btnCancelarCategoria);

        categoriaDAO = new CategoriaDAO(this);

        // Verificar si es edición
        if (getIntent().hasExtra("categoria_id")) {
            categoriaId = getIntent().getIntExtra("categoria_id", -1);
            getSupportActionBar().setTitle("Editar Categoría");
            cargarDatosEdicion();
        } else {
            getSupportActionBar().setTitle("Nueva Categoría");
        }

        btnGuardar.setOnClickListener(v -> guardarCategoria());
        btnCancelar.setOnClickListener(v -> finish());
    }

    private void cargarDatosEdicion() {
        Categoria categoria = categoriaDAO.obtenerPorId(categoriaId);
        if (categoria != null) {
            etNombre.setText(categoria.getNombre());
            etDescripcion.setText(categoria.getDescripcion());
            etColor.setText(categoria.getColorHex());
        }
    }

    private void guardarCategoria() {
        if (!validarCampos()) {
            return;
        }

        String nombre = etNombre.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();
        String color = etColor.getText().toString().trim();

        Categoria categoria = new Categoria(nombre, descripcion, color);

        if (categoriaId == -1) {
            // Insertar nueva
            long resultado = categoriaDAO.insertar(categoria);
            if (resultado > 0) {
                Toast.makeText(this, "Categoría guardada", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Actualizar existente
            categoria.setId(categoriaId);
            int resultado = categoriaDAO.actualizar(categoria);
            if (resultado > 0) {
                Toast.makeText(this, "Categoría actualizada", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validarCampos() {
        if (etNombre.getText().toString().trim().isEmpty()) {
            etNombre.setError("Campo requerido");
            return false;
        }
        if (etDescripcion.getText().toString().trim().isEmpty()) {
            etDescripcion.setError("Campo requerido");
            return false;
        }
        if (etColor.getText().toString().trim().isEmpty()) {
            etColor.setError("Campo requerido");
            return false;
        }

        // Validar formato de color hexadecimal
        String color = etColor.getText().toString().trim();
        if (!color.matches("^#[0-9A-Fa-f]{6}$")) {
            etColor.setError("Formato inválido. Usar #RRGGBB");
            return false;
        }

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}