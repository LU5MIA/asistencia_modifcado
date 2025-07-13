package com.appcali.pantalla_principal.entidades;

public class Departamentos {
    private int id_departamentos;
    private String nombre;
    private String estado;

    public Departamentos() {
    }

    public Departamentos(int id_departamentos, String nombre, String estado) {
        this.id_departamentos = id_departamentos;
        this.nombre = nombre;
        this.estado = estado;
    }

    public int getId_departamentos() {
        return id_departamentos;
    }

    public void setId_departamentos(int id_departamentos) {
        this.id_departamentos = id_departamentos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
