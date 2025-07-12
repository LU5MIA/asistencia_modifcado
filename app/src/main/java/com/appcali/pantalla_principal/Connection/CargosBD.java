package com.appcali.pantalla_principal.Connection;

import com.appcali.pantalla_principal.entidades.Cargos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class CargosBD {

    public ArrayList<Cargos> obtenerCargos() {
        ArrayList<Cargos> lista = new ArrayList<>();
        ConnectionBD connectionBD = new ConnectionBD();

        String sql = "SELECT id_cargos, nombre, estado FROM cargos";

        try (Connection conn = connectionBD.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id_cargos");
                String nombre = rs.getString("nombre");
                String estado = rs.getString("estado");
                lista.add(new Cargos(id, nombre, estado));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public boolean insertarCargo(String nombre) {
        String sql = "INSERT INTO cargos (nombre) VALUES (?)"; // sin estado
        try (Connection conn = new ConnectionBD().connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre);

            int filas = stmt.executeUpdate();
            return filas > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    public boolean actualizarCargo(int idCargo, String nuevoNombre, String nuevoEstado) {
        ConnectionBD connectionBD = new ConnectionBD();
        String sql = "UPDATE cargos SET nombre = ?, estado = ? WHERE id_cargos = ?";

        try (Connection conn = connectionBD.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nuevoNombre);
            stmt.setString(2, nuevoEstado);
            stmt.setInt(3, idCargo);

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Cargos> obtenerCargosActivos() {
        ArrayList<Cargos> lista = new ArrayList<>();
        ConnectionBD connectionBD = new ConnectionBD();

        String sql = "SELECT id_cargos, nombre, estado FROM cargos WHERE estado = 'Activo'";

        try (Connection conn = connectionBD.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id_cargos");
                String nombre = rs.getString("nombre");
                String estado = rs.getString("estado");
                lista.add(new Cargos(id, nombre, estado));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }



    public int contarCargos() {
        ConnectionBD connectionBD = new ConnectionBD();
        String sql = "SELECT COUNT(*) AS total FROM cargos";

        try (Connection conn = connectionBD.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (Exception e) {
            System.err.println("Error al contar cargos: " + e.getMessage());
        }

        return 0;
    }

    public String obtenerNombreCargoDesdeId(int idCargo) {
        String nombre = null;
        ConnectionBD connectionBD = new ConnectionBD();
        String sql = "SELECT nombre FROM cargos WHERE id_cargos = ? AND estado = 'Activo'"; // Solo cargos activos

        try (Connection conn = connectionBD.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCargo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                nombre = rs.getString("nombre");
            }

        } catch (Exception e) {
            System.err.println("Error al obtener nombre del cargo: " + e.getMessage());
        }

        return nombre; // Si no es activo o no existe, retorna null
    }




}
