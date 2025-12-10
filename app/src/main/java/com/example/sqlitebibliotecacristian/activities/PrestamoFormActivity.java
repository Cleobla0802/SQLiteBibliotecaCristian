package com.example.sqlitebibliotecacristian.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sqlitebibliotecacristian.DAO.LibroDAO;
import com.example.sqlitebibliotecacristian.DAO.PrestamoDAO;
import com.example.sqlitebibliotecacristian.DAO.UsuarioDAO;
import com.example.sqlitebibliotecacristian.R;
import com.example.sqlitebibliotecacristian.adapters.LibroSpinnerAdapter;
import com.example.sqlitebibliotecacristian.adapters.UsuarioSpinnerAdapter;
import com.example.sqlitebibliotecacristian.objetos.Libro;
import com.example.sqlitebibliotecacristian.objetos.Prestamo;
import com.example.sqlitebibliotecacristian.objetos.Usuario;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PrestamoFormActivity extends AppCompatActivity {

    private Spinner spinnerLibro, spinnerUsuario;
    private EditText etFechaPrestamo;
    private Button btnGuardar, btnCancelar;
    private PrestamoDAO prestamoDAO;
    private LibroDAO libroDAO;
    private UsuarioDAO usuarioDAO;
    private List<Libro> listaLibros;
    private List<Usuario> listaUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prestamo_form);

        getSupportActionBar().setTitle("Nuevo Préstamo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinnerLibro = findViewById(R.id.spinnerLibroPrestamo);
        spinnerUsuario = findViewById(R.id.spinnerUsuarioPrestamo);
        etFechaPrestamo = findViewById(R.id.etFechaPrestamo);
        btnGuardar = findViewById(R.id.btnGuardarPrestamo);
        btnCancelar = findViewById(R.id.btnCancelarPrestamo);

        prestamoDAO = new PrestamoDAO(this);
        libroDAO = new LibroDAO(this);
        usuarioDAO = new UsuarioDAO(this);

        // Establecer fecha actual
        String fechaActual = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(new Date());
        etFechaPrestamo.setText(fechaActual);

        cargarSpinners();

        btnGuardar.setOnClickListener(v -> guardarPrestamo());
        btnCancelar.setOnClickListener(v -> finish());
    }

    private void cargarSpinners() {
        // Solo libros disponibles
        listaLibros = libroDAO.obtenerDisponibles();
        LibroSpinnerAdapter libroAdapter = new LibroSpinnerAdapter(this, listaLibros);
        spinnerLibro.setAdapter(libroAdapter);

        // Solo usuarios activos
        listaUsuarios = usuarioDAO.obtenerActivos();
        UsuarioSpinnerAdapter usuarioAdapter = new UsuarioSpinnerAdapter(this, listaUsuarios);
        spinnerUsuario.setAdapter(usuarioAdapter);
    }

    private void guardarPrestamo() {
        if (!validarCampos()) {
            return;
        }

        Libro libroSeleccionado = (Libro) spinnerLibro.getSelectedItem();
        Usuario usuarioSeleccionado = (Usuario) spinnerUsuario.getSelectedItem();
        String fechaPrestamo = etFechaPrestamo.getText().toString().trim();

        Prestamo prestamo = new Prestamo(
                libroSeleccionado.getId(),
                usuarioSeleccionado.getId(),
                fechaPrestamo,
                null,
                false
        );

        long resultado = prestamoDAO.insertar(prestamo);
        if (resultado > 0) {
            Toast.makeText(this, "Préstamo registrado", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error al registrar préstamo", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validarCampos() {
        if (spinnerLibro.getSelectedItem() == null) {
            Toast.makeText(this, "Seleccione un libro", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (spinnerUsuario.getSelectedItem() == null) {
            Toast.makeText(this, "Seleccione un usuario", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etFechaPrestamo.getText().toString().trim().isEmpty()) {
            etFechaPrestamo.setError("Campo requerido");
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