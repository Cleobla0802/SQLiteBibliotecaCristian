package com.example.sqlitebibliotecacristian.objetos;

public class Categoria {
    private int id;
    private String nombre;
    private String descripcion;
    private String colorHex;

    // Constructor vacío
    public Categoria() {
    }

    // Constructor completo
    public Categoria(int id, String nombre, String descripcion, String colorHex) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.colorHex = colorHex;
    }

    // Constructor sin ID (para insertar)
    public Categoria(String nombre, String descripcion, String colorHex) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.colorHex = colorHex;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getColorHex() {
        return colorHex;
    }

    public void setColorHex(String colorHex) {
        this.colorHex = colorHex;
    }

    @Override
    public String toString() {
        return nombre;
    }
}