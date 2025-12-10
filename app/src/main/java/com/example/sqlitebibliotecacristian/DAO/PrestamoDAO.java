package com.example.sqlitebibliotecacristian.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.sqlitebibliotecacristian.database.DatabaseHelper;
import com.example.sqlitebibliotecacristian.objetos.Prestamo;

import java.util.ArrayList;
import java.util.List;

public class PrestamoDAO {
    private DatabaseHelper dbHelper;

    public PrestamoDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public long insertar(Prestamo prestamo) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("libro_id", prestamo.getLibroId());
        values.put("usuario_id", prestamo.getUsuarioId());
        values.put("fecha_prestamo", prestamo.getFechaPrestamo());
        values.put("fecha_devolucion", prestamo.getFechaDevolucion());
        values.put("devuelto", prestamo.isDevuelto() ? 1 : 0);

        long id = db.insert("prestamos", null, values);

        // Actualizar disponibilidad del libro
        if (id > 0) {
            ContentValues libroValues = new ContentValues();
            libroValues.put("disponible", 0);
            db.update("libros", libroValues, "id = ?",
                    new String[]{String.valueOf(prestamo.getLibroId())});
        }

        db.close();
        return id;
    }

    public List<Prestamo> obtenerTodos() {
        List<Prestamo> lista = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT p.*, l.titulo as libro_titulo, u.nombre as usuario_nombre " +
                "FROM prestamos p " +
                "INNER JOIN libros l ON p.libro_id = l.id " +
                "INNER JOIN usuarios u ON p.usuario_id = u.id " +
                "ORDER BY p.fecha_prestamo DESC";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Prestamo prestamo = new Prestamo();
                prestamo.setId(cursor.getInt(0));
                prestamo.setLibroId(cursor.getInt(1));
                prestamo.setUsuarioId(cursor.getInt(2));
                prestamo.setFechaPrestamo(cursor.getString(3));
                prestamo.setFechaDevolucion(cursor.getString(4));
                prestamo.setDevuelto(cursor.getInt(5) == 1);
                prestamo.setLibroTitulo(cursor.getString(6));
                prestamo.setUsuarioNombre(cursor.getString(7));
                lista.add(prestamo);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return lista;
    }

    public List<Prestamo> obtenerActivos() {
        List<Prestamo> lista = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT p.*, l.titulo as libro_titulo, u.nombre as usuario_nombre " +
                "FROM prestamos p " +
                "INNER JOIN libros l ON p.libro_id = l.id " +
                "INNER JOIN usuarios u ON p.usuario_id = u.id " +
                "WHERE p.devuelto = 0 " +
                "ORDER BY p.fecha_prestamo DESC";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Prestamo prestamo = new Prestamo();
                prestamo.setId(cursor.getInt(0));
                prestamo.setLibroId(cursor.getInt(1));
                prestamo.setUsuarioId(cursor.getInt(2));
                prestamo.setFechaPrestamo(cursor.getString(3));
                prestamo.setFechaDevolucion(cursor.getString(4));
                prestamo.setDevuelto(false);
                prestamo.setLibroTitulo(cursor.getString(6));
                prestamo.setUsuarioNombre(cursor.getString(7));
                lista.add(prestamo);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return lista;
    }

    public Prestamo obtenerPorId(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT p.*, l.titulo as libro_titulo, u.nombre as usuario_nombre " +
                "FROM prestamos p " +
                "INNER JOIN libros l ON p.libro_id = l.id " +
                "INNER JOIN usuarios u ON p.usuario_id = u.id " +
                "WHERE p.id = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});

        Prestamo prestamo = null;
        if (cursor.moveToFirst()) {
            prestamo = new Prestamo();
            prestamo.setId(cursor.getInt(0));
            prestamo.setLibroId(cursor.getInt(1));
            prestamo.setUsuarioId(cursor.getInt(2));
            prestamo.setFechaPrestamo(cursor.getString(3));
            prestamo.setFechaDevolucion(cursor.getString(4));
            prestamo.setDevuelto(cursor.getInt(5) == 1);
            prestamo.setLibroTitulo(cursor.getString(6));
            prestamo.setUsuarioNombre(cursor.getString(7));
        }

        cursor.close();
        db.close();
        return prestamo;
    }

    public int marcarComoDevuelto(int id, String fechaDevolucion) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Obtener el libro_id del préstamo
        Cursor cursor = db.rawQuery("SELECT libro_id FROM prestamos WHERE id = ?",
                new String[]{String.valueOf(id)});
        int libroId = -1;
        if (cursor.moveToFirst()) {
            libroId = cursor.getInt(0);
        }
        cursor.close();

        // Actualizar préstamo
        ContentValues values = new ContentValues();
        values.put("devuelto", 1);
        values.put("fecha_devolucion", fechaDevolucion);
        int filasAfectadas = db.update("prestamos", values, "id = ?",
                new String[]{String.valueOf(id)});

        // Actualizar disponibilidad del libro
        if (filasAfectadas > 0 && libroId != -1) {
            ContentValues libroValues = new ContentValues();
            libroValues.put("disponible", 1);
            db.update("libros", libroValues, "id = ?",
                    new String[]{String.valueOf(libroId)});
        }

        db.close();
        return filasAfectadas;
    }

    public int actualizar(Prestamo prestamo) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("libro_id", prestamo.getLibroId());
        values.put("usuario_id", prestamo.getUsuarioId());
        values.put("fecha_prestamo", prestamo.getFechaPrestamo());
        values.put("fecha_devolucion", prestamo.getFechaDevolucion());
        values.put("devuelto", prestamo.isDevuelto() ? 1 : 0);

        int filasAfectadas = db.update("prestamos", values, "id = ?",
                new String[]{String.valueOf(prestamo.getId())});
        db.close();
        return filasAfectadas;
    }

    public int eliminar(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int filasAfectadas = db.delete("prestamos", "id = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return filasAfectadas;
    }
}