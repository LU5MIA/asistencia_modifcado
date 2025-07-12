package com.appcali.pantalla_principal.entidades;

public class Configuracion {

    private String nombre_empresa;
    private String direccion;
    private String apertura_sistema;
    private String hora_entrada;
    private String cierre_sistema;
    private double ubicacion_latitud;
    private double ubicacion_longitud;
    private int radio;

    public Configuracion() {
    }

    public Configuracion(String nombre_empresa, String direccion, String apertura_sistema, String hora_entrada, String cierre_sistema, double ubicacion_latitud, double ubicacion_longitud, int radio) {
        this.nombre_empresa = nombre_empresa;
        this.direccion = direccion;
        this.apertura_sistema = apertura_sistema;
        this.hora_entrada = hora_entrada;
        this.cierre_sistema = cierre_sistema;
        this.ubicacion_latitud = ubicacion_latitud;
        this.ubicacion_longitud = ubicacion_longitud;
        this.radio = radio;
    }

    public String getNombre_empresa() {
        return nombre_empresa;
    }

    public void setNombre_empresa(String nombre_empresa) {
        this.nombre_empresa = nombre_empresa;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getApertura_sistema() {
        return apertura_sistema;
    }

    public void setApertura_sistema(String apertura_sistema) {
        this.apertura_sistema = apertura_sistema;
    }

    public String getHora_entrada() {
        return hora_entrada;
    }

    public void setHora_entrada(String hora_entrada) {
        this.hora_entrada = hora_entrada;
    }

    public String getCierre_sistema() {
        return cierre_sistema;
    }

    public void setCierre_sistema(String cierre_sistema) {
        this.cierre_sistema = cierre_sistema;
    }

    public double getUbicacion_latitud() {
        return ubicacion_latitud;
    }

    public void setUbicacion_latitud(double ubicacion_latitud) {
        this.ubicacion_latitud = ubicacion_latitud;
    }

    public double getUbicacion_longitud() {
        return ubicacion_longitud;
    }

    public void setUbicacion_longitud(double ubicacion_longitud) {
        this.ubicacion_longitud = ubicacion_longitud;
    }

    public int getRadio() {
        return radio;
    }

    public void setRadio(int radio) {
        this.radio = radio;
    }
}
