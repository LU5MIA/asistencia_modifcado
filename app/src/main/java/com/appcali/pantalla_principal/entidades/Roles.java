package com.appcali.pantalla_principal.entidades;

public class Roles {

    private int id_roles;
    private String nombre;
    private String estado;

    public Roles() {
    }

    public Roles(int id_roles, String nombre, String estado) {
        this.id_roles = id_roles;
        this.nombre = nombre;
        this.estado = estado;
    }

    public int getId_roles() {
        return id_roles;
    }

    public void setId_roles(int id_roles) {
        this.id_roles = id_roles;
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
}
