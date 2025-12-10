package com.example.sqlitebibliotecacristian.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "biblioteca.db";
    public static final int DB_VERSION = 1;

    // Tabla de CATEGORIAS
    public static final String TABLE_CATEGORIAS =
            "CREATE TABLE categorias (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nombre TEXT NOT NULL, " +
                    "descripcion TEXT, " +
                    "color_hex TEXT DEFAULT '#6200EE')";

    // Tabla de USUARIOS
    public static final String TABLE_USUARIOS =
            "CREATE TABLE usuarios (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nombre TEXT NOT NULL, " +
                    "email TEXT UNIQUE, " +
                    "telefono TEXT, " +
                    "activo INTEGER DEFAULT 1)";

    // Tabla de LIBROS
    public static final String TABLE_LIBROS =
            "CREATE TABLE libros (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "titulo TEXT NOT NULL, " +
                    "autor TEXT NOT NULL, " +
                    "isbn TEXT UNIQUE, " +
                    "categoria_id INTEGER NOT NULL, " +
                    "disponible INTEGER DEFAULT 1, " +
                    "FOREIGN KEY (categoria_id) REFERENCES categorias(id) ON DELETE CASCADE)";

    // Tabla de PRESTAMOS
    public static final String TABLE_PRESTAMOS =
            "CREATE TABLE prestamos (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "libro_id INTEGER NOT NULL, " +
                    "usuario_id INTEGER NOT NULL, " +
                    "fecha_prestamo TEXT NOT NULL DEFAULT CURRENT_DATE, " +
                    "fecha_devolucion TEXT, " +
                    "devuelto INTEGER DEFAULT 0, " +
                    "FOREIGN KEY (libro_id) REFERENCES libros(id) ON DELETE CASCADE, " +
                    "FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE)";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CATEGORIAS);
        db.execSQL(TABLE_USUARIOS);
        db.execSQL(TABLE_LIBROS);
        db.execSQL(TABLE_PRESTAMOS);
        insertarDatosIniciales(db);
    }

    private void insertarDatosIniciales(SQLiteDatabase db) {
        // Insertar categorÃ­as predefinidas
        db.execSQL("INSERT INTO categorias (nombre, descripcion, color_hex) VALUES " +
                "('Ciencia FicciÃ³n', 'Libros de ciencia ficciÃ³n y futurismo', '#2196F3'), " +
                "('FantasÃ­a', 'Mundos imaginarios y seres mÃ¡gicos', '#9C27B0'), " +
                "('Novela HistÃ³rica', 'Historias ambientadas en Ã©pocas pasadas', '#FF9800'), " +
                "('Ensayo', 'Textos acadÃ©micos y reflexiones', '#4CAF50')");

        // Insertar usuarios de ejemplo
        db.execSQL("INSERT INTO usuarios (nombre, email, telefono) VALUES " +
                "('Ana GarcÃ­a', 'ana@example.com', '612345678'), " +
                "('Carlos MartÃ­nez', 'carlos@example.com', '623456789'), " +
                "('Laura SÃ¡nchez', 'laura@example.com', '634567890')");

        // Insertar libros de ejemplo (asumiendo que las categorÃ­as tienen IDs 1,2,3,4)
        db.execSQL("INSERT INTO libros (titulo, autor, isbn, categoria_id) VALUES " +
                "('Dune', 'Frank Herbert', '9788445077631', 1), " +
                "('El Nombre del Viento', 'Patrick Rothfuss', '9788499082689', 2), " +
                "('El Paciente InglÃ©s', 'Michael Ondaatje', '9788439735422', 3), " +
                "('Sapiens', 'Yuval Noah Harari', '9788430619279', 4)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS prestamos");
        db.execSQL("DROP TABLE IF EXISTS libros");
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        db.execSQL("DROP TABLE IF EXISTS categorias");
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }
}