package com.example.sqlitebibliotecacristian.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.sqlitebibliotecacristian.database.DatabaseHelper;
import com.example.sqlitebibliotecacristian.objetos.Libro;

import java.util.ArrayList;
import java.util.List;

public class LibroDAO {
    private DatabaseHelper dbHelper;

    public LibroDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public long insertar(Libro libro) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("titulo", libro.getTitulo());
        values.put("autor", libro.getAutor());
        values.put("isbn", libro.getIsbn());
        values.put("categoria_id", libro.getCategoriaId());
        values.put("disponible", libro.isDisponible() ? 1 : 0);

        long id = db.insert("libros", null, values);
        db.close();
        return id;
    }

    public List<Libro> obtenerTodos() {
        List<Libro> lista = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT l.*, c.nombre as categoria_nombre " +
                "FROM libros l " +
                "INNER JOIN categorias c ON l.categoria_id = c.id " +
                "ORDER BY l.titulo";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Libro libro = new Libro();
                libro.setId(cursor.getInt(0));
                libro.setTitulo(cursor.getString(1));
                libro.setAutor(cursor.getString(2));
                libro.setIsbn(cursor.getString(3));
                libro.setCategoriaId(cursor.getInt(4));
                libro.setDisponible(cursor.getInt(5) == 1);
                libro.setCategoriaNombre(cursor.getString(6));
                lista.add(libro);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return lista;
    }

    public List<Libro> obtenerDisponibles() {
        List<Libro> lista = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT l.*, c.nombre as categoria_nombre " +
                "FROM libros l " +
                "INNER JOIN categorias c ON l.categoria_id = c.id " +
                "WHERE l.disponible = 1 " +
                "ORDER BY l.titulo";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Libro libro = new Libro();
                libro.setId(cursor.getInt(0));
                libro.setTitulo(cursor.getString(1));
                libro.setAutor(cursor.getString(2));
                libro.setIsbn(cursor.getString(3));
                libro.setCategoriaId(cursor.getInt(4));
                libro.setDisponible(true);
                libro.setCategoriaNombre(cursor.getString(6));
                lista.add(libro);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return lista;
    }

    public Libro obtenerPorId(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT l.*, c.nombre as categoria_nombre " +
                "FROM libros l " +
                "INNER JOIN categorias c ON l.categoria_id = c.id " +
                "WHERE l.id = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});

        Libro libro = null;
        if (cursor.moveToFirst()) {
            libro = new Libro();
            libro.setId(cursor.getInt(0));
            libro.setTitulo(cursor.getString(1));
            libro.setAutor(cursor.getString(2));
            libro.setIsbn(cursor.getString(3));
            libro.setCategoriaId(cursor.getInt(4));
            libro.setDisponible(cursor.getInt(5) == 1);
            libro.setCategoriaNombre(cursor.getString(6));
        }

        cursor.close();
        db.close();
        return libro;
    }

    public int actualizar(Libro libro) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("titulo", libro.getTitulo());
        values.put("autor", libro.getAutor());
        values.put("isbn", libro.getIsbn());
        values.put("categoria_id", libro.getCategoriaId());
        values.put("disponible", libro.isDisponible() ? 1 : 0);

        int filasAfectadas = db.update("libros", values, "id = ?",
                new String[]{String.valueOf(libro.getId())});
        db.close();
        return filasAfectadas;
    }

    public int eliminar(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int filasAfectadas = db.delete("libros", "id = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return filasAfectadas;
    }
}