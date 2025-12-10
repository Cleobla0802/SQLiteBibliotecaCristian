package com.example.sqlitebibliotecacristian.activities;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sqlitebibliotecacristian.DAO.UsuarioDAO;
import com.example.sqlitebibliotecacristian.R;
import com.example.sqlitebibliotecacristian.objetos.Usuario;


public class UsuarioFormActivity extends AppCompatActivity {

    private EditText etNombre, etEmail, etTelefono;
    private CheckBox cbActivo;
    private Button btnGuardar, btnCancelar;
    private UsuarioDAO usuarioDAO;
    private int usuarioId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_form);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etNombre = findViewById(R.id.etNombreUsuario);
        etEmail = findViewById(R.id.etEmailUsuario);
        etTelefono = findViewById(R.id.etTelefonoUsuario);
        cbActivo = findViewById(R.id.cbActivoUsuario);
        btnGuardar = findViewById(R.id.btnGuardarUsuario);
        btnCancelar = findViewById(R.id.btnCancelarUsuario);

        usuarioDAO = new UsuarioDAO(this);

        if (getIntent().hasExtra("usuario_id")) {
            usuarioId = getIntent().getIntExtra("usuario_id", -1);
            getSupportActionBar().setTitle("Editar Usuario");
            cargarDatosEdicion();
        } else {
            getSupportActionBar().setTitle("Nuevo Usuario");
            cbActivo.setChecked(true);
        }

        btnGuardar.setOnClickListener(v -> guardarUsuario());
        btnCancelar.setOnClickListener(v -> finish());
    }

    private void cargarDatosEdicion() {
        Usuario usuario = usuarioDAO.obtenerPorId(usuarioId);
        if (usuario != null) {
            etNombre.setText(usuario.getNombre());
            etEmail.setText(usuario.getEmail());
            etTelefono.setText(usuario.getTelefono());
            cbActivo.setChecked(usuario.isActivo());
        }
    }

    private void guardarUsuario() {
        if (!validarCampos()) {
            return;
        }

        String nombre = etNombre.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();
        boolean activo = cbActivo.isChecked();

        Usuario usuario = new Usuario(nombre, email, telefono, activo);

        if (usuarioId == -1) {
            long resultado = usuarioDAO.insertar(usuario);
            if (resultado > 0) {
                Toast.makeText(this, "Usuario guardado", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show();
            }
        } else {
            usuario.setId(usuarioId);
            int resultado = usuarioDAO.actualizar(usuario);
            if (resultado > 0) {
                Toast.makeText(this, "Usuario actualizado", Toast.LENGTH_SHORT).show();
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

        String email = etEmail.getText().toString().trim();
        if (email.isEmpty()) {
            etEmail.setError("Campo requerido");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email inválido");
            return false;
        }

        if (etTelefono.getText().toString().trim().isEmpty()) {
            etTelefono.setError("Campo requerido");
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