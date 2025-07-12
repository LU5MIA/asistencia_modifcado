package com.appcali.pantalla_principal.entidades;

public class Usuarios {
    private int id_usuarios;
    private int id_empleados;
    private int id_roles;
    private String nombre_usuario;
    private String password;
    private Empleado empleado;

    // Constructor vacío
    public Usuarios() {}

    // Constructor completo
    public Usuarios(int id_usuarios, int id_empleados, String nombre_usuario, String password, Empleado empleado) {
        this.id_usuarios = id_usuarios;
        this.id_empleados = id_empleados;
        this.nombre_usuario = nombre_usuario;
        this.password = password;
        this.empleado = empleado;
    }

    public int getId_empleados() {
        return id_empleados;
    }

    public void setId_empleados(int id_empleados) {
        this.id_empleados = id_empleados;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId_usuarios() {
        return id_usuarios;
    }

    public void setId_usuarios(int id_usuarios) {
        this.id_usuarios = id_usuarios;
    }

    public int getId_roles() {
        return id_roles;
    }

    public void setId_roles(int id_roles) {
        this.id_roles = id_roles;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }
}
