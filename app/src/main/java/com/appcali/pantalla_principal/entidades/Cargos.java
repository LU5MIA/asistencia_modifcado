package com.appcali.pantalla_principal.entidades;

public class Cargos {

    private int id_cargos;
    private String nombre;
    private String estado;

    public Cargos() {
    }

    public Cargos(int id_cargos, String nombre, String estado) {
        this.id_cargos = id_cargos;
        this.nombre = nombre;
        this.estado = estado;
    }

    public int getId_cargos() {
        return id_cargos;
    }

    public void setId_cargos(int id_cargos) {
        this.id_cargos = id_cargos;
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
        return nombre; // Esto hará que se muestre el nombre en el AutoCompleteTextView
    }

}
