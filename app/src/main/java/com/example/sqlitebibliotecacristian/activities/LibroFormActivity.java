package com.example.sqlitebibliotecacristian.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sqlitebibliotecacristian.DAO.CategoriaDAO;
import com.example.sqlitebibliotecacristian.DAO.LibroDAO;
import com.example.sqlitebibliotecacristian.R;
import com.example.sqlitebibliotecacristian.adapters.CategoriaSpinnerAdapter;
import com.example.sqlitebibliotecacristian.objetos.Categoria;
import com.example.sqlitebibliotecacristian.objetos.Libro;

import java.util.List;

public class LibroFormActivity extends AppCompatActivity {

    private EditText etTitulo, etAutor, etIsbn;
    private Spinner spinnerCategoria;
    private CheckBox cbDisponible;
    private Button btnGuardar, btnCancelar;
    private LibroDAO libroDAO;
    private CategoriaDAO categoriaDAO;
    private List<Categoria> listaCategorias;
    private int libroId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_libro_form);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etTitulo = findViewById(R.id.etTituloLibro);
        etAutor = findViewById(R.id.etAutorLibro);
        etIsbn = findViewById(R.id.etIsbnLibro);
        spinnerCategoria = findViewById(R.id.spinnerCategoriaLibro);
        cbDisponible = findViewById(R.id.cbDisponibleLibro);
        btnGuardar = findViewById(R.id.btnGuardarLibro);
        btnCancelar = findViewById(R.id.btnCancelarLibro);

        libroDAO = new LibroDAO(this);
        categoriaDAO = new CategoriaDAO(this);

        cargarSpinnerCategorias();

        if (getIntent().hasExtra("libro_id")) {
            libroId = getIntent().getIntExtra("libro_id", -1);
            getSupportActionBar().setTitle("Editar Libro");
            cargarDatosEdicion();
        } else {
            getSupportActionBar().setTitle("Nuevo Libro");
            cbDisponible.setChecked(true);
        }

        btnGuardar.setOnClickListener(v -> guardarLibro());
        btnCancelar.setOnClickListener(v -> finish());
    }

    private void cargarSpinnerCategorias() {
        listaCategorias = categoriaDAO.obtenerTodas();
        CategoriaSpinnerAdapter adapter = new CategoriaSpinnerAdapter(this, listaCategorias);
        spinnerCategoria.setAdapter(adapter);
    }

    private void cargarDatosEdicion() {
        Libro libro = libroDAO.obtenerPorId(libroId);
        if (libro != null) {
            etTitulo.setText(libro.getTitulo());
            etAutor.setText(libro.getAutor());
            etIsbn.setText(libro.getIsbn());
            cbDisponible.setChecked(libro.isDisponible());

            // Seleccionar categoría en spinner
            for (int i = 0; i < listaCategorias.size(); i++) {
                if (listaCategorias.get(i).getId() == libro.getCategoriaId()) {
                    spinnerCategoria.setSelection(i);
                    break;
                }
            }
        }
    }

    private void guardarLibro() {
        if (!validarCampos()) {
            return;
        }

        String titulo = etTitulo.getText().toString().trim();
        String autor = etAutor.getText().toString().trim();
        String isbn = etIsbn.getText().toString().trim();
        Categoria categoriaSeleccionada = (Categoria) spinnerCategoria.getSelectedItem();
        boolean disponible = cbDisponible.isChecked();

        Libro libro = new Libro(titulo, autor, isbn, categoriaSeleccionada.getId(), disponible);

        if (libroId == -1) {
            long resultado = libroDAO.insertar(libro);
            if (resultado > 0) {
                Toast.makeText(this, "Libro guardado", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show();
            }
        } else {
            libro.setId(libroId);
            int resultado = libroDAO.actualizar(libro);
            if (resultado > 0) {
                Toast.makeText(this, "Libro actualizado", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validarCampos() {
        if (etTitulo.getText().toString().trim().isEmpty()) {
            etTitulo.setError("Campo requerido");
            return false;
        }
        if (etAutor.getText().toString().trim().isEmpty()) {
            etAutor.setError("Campo requerido");
            return false;
        }
        if (etIsbn.getText().toString().trim().isEmpty()) {
            etIsbn.setError("Campo requerido");
            return false;
        }
        if (spinnerCategoria.getSelectedItem() == null) {
            Toast.makeText(this, "Seleccione una categoría", Toast.LENGTH_SHORT).show();
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