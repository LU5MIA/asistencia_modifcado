package com.appcali.pantalla_principal.Connection;

import com.appcali.pantalla_principal.entidades.Configuracion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ConfiguracionBD {

    public ArrayList<Configuracion> obtenerConfiguracion() {
        ArrayList<Configuracion> lista = new ArrayList<>();
        ConnectionBD connectionBD = new ConnectionBD();

        String sql = "SELECT nombre_empresa, direccion, apertura_sistema, hora_entrada, cierre_sistema, ubicacion_latitud, ubicacion_longitud, radio FROM configuracion_empresa";

        try (Connection conn = connectionBD.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String nombre_empresa = rs.getString("nombre_empresa");
                String direccion = rs.getString("direccion");
                String apertura_sistema = rs.getString("apertura_sistema");
                String hora_entrada = rs.getString("hora_entrada");
                String cierre_sistema = rs.getString("cierre_sistema");
                double ubicacion_latitud = rs.getDouble("ubicacion_latitud");
                double ubicacion_longitud = rs.getDouble("ubicacion_longitud");
                int radio = rs.getInt("radio");

                lista.add(new Configuracion(
                        nombre_empresa,
                        direccion,
                        apertura_sistema,
                        hora_entrada,
                        cierre_sistema,
                        ubicacion_latitud,
                        ubicacion_longitud,
                        radio
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

}
