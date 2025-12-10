package com.example.sqlitebibliotecacristian.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.sqlitebibliotecacristian.database.DatabaseHelper;
import com.example.sqlitebibliotecacristian.objetos.Categoria;

import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {
    private DatabaseHelper dbHelper;

    public CategoriaDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public long insertar(Categoria categoria) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", categoria.getNombre());
        values.put("descripcion", categoria.getDescripcion());
        values.put("color_hex", categoria.getColorHex());

        long id = db.insert("categorias", null, values);
        db.close();
        return id;
    }

    public List<Categoria> obtenerTodas() {
        List<Categoria> lista = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM categorias ORDER BY nombre", null);

        if (cursor.moveToFirst()) {
            do {
                Categoria cat = new Categoria();
                cat.setId(cursor.getInt(0));
                cat.setNombre(cursor.getString(1));
                cat.setDescripcion(cursor.getString(2));
                cat.setColorHex(cursor.getString(3));
                lista.add(cat);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return lista;
    }

    public Categoria obtenerPorId(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM categorias WHERE id = ?",
                new String[]{String.valueOf(id)});

        Categoria cat = null;
        if (cursor.moveToFirst()) {
            cat = new Categoria();
            cat.setId(cursor.getInt(0));
            cat.setNombre(cursor.getString(1));
            cat.setDescripcion(cursor.getString(2));
            cat.setColorHex(cursor.getString(3));
        }

        cursor.close();
        db.close();
        return cat;
    }

    public int actualizar(Categoria categoria) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", categoria.getNombre());
        values.put("descripcion", categoria.getDescripcion());
        values.put("color_hex", categoria.getColorHex());

        int filasAfectadas = db.update("categorias", values, "id = ?",
                new String[]{String.valueOf(categoria.getId())});
        db.close();
        return filasAfectadas;
    }

    public int eliminar(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int filasAfectadas = db.delete("categorias", "id = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return filasAfectadas;
    }
}