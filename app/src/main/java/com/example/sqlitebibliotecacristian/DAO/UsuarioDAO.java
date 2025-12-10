package com.example.sqlitebibliotecacristian.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.sqlitebibliotecacristian.database.DatabaseHelper;
import com.example.sqlitebibliotecacristian.objetos.Usuario;

import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    private DatabaseHelper dbHelper;

    public UsuarioDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public long insertar(Usuario usuario) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", usuario.getNombre());
        values.put("email", usuario.getEmail());
        values.put("telefono", usuario.getTelefono());
        values.put("activo", usuario.isActivo() ? 1 : 0);

        long id = db.insert("usuarios", null, values);
        db.close();
        return id;
    }

    public List<Usuario> obtenerTodos() {
        List<Usuario> lista = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM usuarios ORDER BY nombre", null);

        if (cursor.moveToFirst()) {
            do {
                Usuario usuario = new Usuario();
                usuario.setId(cursor.getInt(0));
                usuario.setNombre(cursor.getString(1));
                usuario.setEmail(cursor.getString(2));
                usuario.setTelefono(cursor.getString(3));
                usuario.setActivo(cursor.getInt(4) == 1);
                lista.add(usuario);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return lista;
    }

    public List<Usuario> obtenerActivos() {
        List<Usuario> lista = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM usuarios WHERE activo = 1 ORDER BY nombre", null);

        if (cursor.moveToFirst()) {
            do {
                Usuario usuario = new Usuario();
                usuario.setId(cursor.getInt(0));
                usuario.setNombre(cursor.getString(1));
                usuario.setEmail(cursor.getString(2));
                usuario.setTelefono(cursor.getString(3));
                usuario.setActivo(true);
                lista.add(usuario);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return lista;
    }

    public Usuario obtenerPorId(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM usuarios WHERE id = ?",
                new String[]{String.valueOf(id)});

        Usuario usuario = null;
        if (cursor.moveToFirst()) {
            usuario = new Usuario();
            usuario.setId(cursor.getInt(0));
            usuario.setNombre(cursor.getString(1));
            usuario.setEmail(cursor.getString(2));
            usuario.setTelefono(cursor.getString(3));
            usuario.setActivo(cursor.getInt(4) == 1);
        }

        cursor.close();
        db.close();
        return usuario;
    }

    public int actualizar(Usuario usuario) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", usuario.getNombre());
        values.put("email", usuario.getEmail());
        values.put("telefono", usuario.getTelefono());
        values.put("activo", usuario.isActivo() ? 1 : 0);

        int filasAfectadas = db.update("usuarios", values, "id = ?",
                new String[]{String.valueOf(usuario.getId())});
        db.close();
        return filasAfectadas;
    }

    public int eliminar(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int filasAfectadas = db.delete("usuarios", "id = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return filasAfectadas;
    }
}