package com.appcali.pantalla_principal.entidades;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Asistencias {

    private int id_asistencias;
    private int id_empleado;
    private String nombre_completo;
    private String marcacion;
    private String tipo;
    private String estado;
    public int getId_asistencias() {
        return id_asistencias;
    }

    public void setId_asistencias(int id_asistencias) {
        this.id_asistencias = id_asistencias;
    }

    public int getId_empleado() {
        return id_empleado;
    }

    public void setId_empleado(int id_empleado) {
        this.id_empleado = id_empleado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNombre_completo() {
        return nombre_completo;
    }

    public void setNombre_completo(String nombre_completo) {
        this.nombre_completo = nombre_completo;
    }

    public String getMarcacion() {
        return marcacion;
    }

    public void setMarcacion(String marcacion) {
        this.marcacion = marcacion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
