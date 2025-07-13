package com.appcali.pantalla_principal.Connection;
import com.appcali.pantalla_principal.entidades.Roles;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class RolesBD {

    public ArrayList<Roles> obtenerRoles() {
        ArrayList<Roles> lista = new ArrayList<>();
        ConnectionBD connectionBD = new ConnectionBD();

        String sql = "SELECT id_roles, nombre, estado FROM roles";

        try (Connection conn = connectionBD.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id_roles");
                String nombre = rs.getString("nombre");
                String estado = rs.getString("estado");
                lista.add(new Roles(id, nombre, estado));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public boolean agregarRol(String nombre, String estado) {
        ConnectionBD connectionBD = new ConnectionBD();
        String sql = "INSERT INTO roles (nombre, estado) VALUES (?, ?)";

        try (Connection conn = connectionBD.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            stmt.setString(2, estado);

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarRol(int idRoles, String nuevoNombre) {
        ConnectionBD connectionBD = new ConnectionBD();
        String sql = "UPDATE roles SET nombre = ? WHERE id_roles = ?";

        try (Connection conn = connectionBD.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nuevoNombre);
            stmt.setInt(2, idRoles);

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int contarRoles() {
        ConnectionBD connectionBD = new ConnectionBD();
        String sql = "SELECT COUNT(*) AS total FROM roles";

        try (Connection conn = connectionBD.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (Exception e) {
            System.err.println("Error al contar roles: " + e.getMessage());
        }

        return 0;
    }



}
