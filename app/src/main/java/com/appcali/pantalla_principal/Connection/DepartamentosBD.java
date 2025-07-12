package com.appcali.pantalla_principal.Connection;

import com.appcali.pantalla_principal.entidades.Departamentos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DepartamentosBD {

    public ArrayList<Departamentos> obtenerDepartamentos() {
        ArrayList<Departamentos> lista = new ArrayList<>();
        ConnectionBD connectionBD = new ConnectionBD();

        String sql = "SELECT id_departamentos, nombre, estado FROM departamentos";

        try (Connection conn = connectionBD.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id_departamentos");
                String nombre = rs.getString("nombre");
                String estado = rs.getString("estado");
                lista.add(new Departamentos(id, nombre, estado));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public boolean insertarDepartamento(String nombre) {
        String sql = "INSERT INTO departamentos (nombre) VALUES (?)"; // sin estado
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



    public boolean actualizarDepartamento(int idDepartamento, String nuevoNombre, String nuevoEstado) {
        ConnectionBD connectionBD = new ConnectionBD();
        String sql = "UPDATE departamentos SET nombre = ?, estado = ? WHERE id_departamentos = ?";

        try (Connection conn = connectionBD.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nuevoNombre);
            stmt.setString(2, nuevoEstado);
            stmt.setInt(3, idDepartamento);

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Departamentos> obtenerDepartamentosActivos() {
        ArrayList<Departamentos> lista = new ArrayList<>();
        ConnectionBD connectionBD = new ConnectionBD();

        String sql = "SELECT id_departamentos, nombre, estado FROM departamentos WHERE estado = 'Activo'";

        try (Connection conn = connectionBD.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id_departamentos");
                String nombre = rs.getString("nombre");
                String estado = rs.getString("estado");
                lista.add(new Departamentos(id, nombre, estado));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public int contarDepartamentos() {
        ConnectionBD connectionBD = new ConnectionBD();
        String sql = "SELECT COUNT(*) AS total_departamentos FROM departamentos";

        try (Connection conn = connectionBD.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total_departamentos");
            }

        } catch (Exception e) {
            System.err.println("Error al contar departamentos: " + e.getMessage());
        }

        return 0; // En caso de error
    }



}
