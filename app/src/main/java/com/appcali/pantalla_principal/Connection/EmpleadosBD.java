package com.appcali.pantalla_principal.Connection;
import com.appcali.pantalla_principal.entidades.Cargos;
import com.appcali.pantalla_principal.entidades.Departamentos;
import com.appcali.pantalla_principal.entidades.Empleado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class EmpleadosBD {
    public ArrayList<Empleado> mostrarDatosEmpleado() {
        ArrayList<Empleado> listaempleados = new ArrayList<>();
        ConnectionBD connectionBD = new ConnectionBD();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = connectionBD.connect();
            if (conn != null) {
                String sql = "SELECT e.id_empleados, e.dni, e.nombres, e.ape_p, e.ape_m, e.correo_electronico, " +
                        "d.id_departamentos, d.nombre AS nombre_departamento, " +
                        "c.id_cargos, c.nombre AS nombre_cargo, c.estado AS estado_cargo " +
                        "FROM empleados e " +
                        "LEFT JOIN departamentos d ON e.id_departamentos = d.id_departamentos AND d.estado = 'Activo'" +
                        "LEFT JOIN cargos c ON e.id_cargos = c.id_cargos AND c.estado = 'Activo'";


                stmt = conn.prepareStatement(sql);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    Empleado empleado = new Empleado();
                    empleado.setId_empleados(rs.getInt("id_empleados"));
                    empleado.setDni(rs.getString("dni"));
                    empleado.setNombres(rs.getString("nombres"));
                    empleado.setApe_p(rs.getString("ape_p"));
                    empleado.setApe_m(rs.getString("ape_m"));
                    empleado.setCorreo(rs.getString("correo_electronico"));

                    Departamentos dpto = new Departamentos();
                    dpto.setId_departamentos(rs.getInt("id_departamentos"));
                    dpto.setNombre(rs.getString("nombre_departamento"));
                    empleado.setDpto(dpto);

                    // Cargo (puede ser null si no tiene asignado)
                    Cargos cargo = new Cargos();
                    cargo.setId_cargos(rs.getInt("id_cargos"));
                    cargo.setNombre(rs.getString("nombre_cargo"));
                    cargo.setEstado(rs.getString("estado_cargo"));
                    empleado.setCargo(cargo);

                    listaempleados.add(empleado);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }

        return listaempleados;
    }

    public int insertarEmpleado(Empleado empleado) {
        ConnectionBD connectionBD = new ConnectionBD();
        String sql = "INSERT INTO empleados (dni, nombres, ape_p, ape_m, correo_electronico, id_departamentos, id_cargos) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = connectionBD.connect();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, empleado.getDni());
            stmt.setString(2, empleado.getNombres());
            stmt.setString(3, empleado.getApe_p());
            stmt.setString(4, empleado.getApe_m());
            stmt.setString(5, empleado.getCorreo());
            stmt.setInt(6, empleado.getDpto().getId_departamentos());
            stmt.setInt(7, empleado.getCargo().getId_cargos());

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1); // ✅ Retorna el ID generado
                }
            }

        } catch (Exception e) {
            System.err.println("Error al insertar empleado: " + e.getMessage());
        }

        return -1; // ❌ si hubo error o no se generó ID
    }


    public boolean actualizarEmpleado(Empleado empleado) {
        ConnectionBD connectionBD = new ConnectionBD();
        String sql = "UPDATE empleados SET dni = ?, nombres = ?, ape_p = ?, ape_m = ?, correo_electronico = ?, id_departamentos = ?, id_cargos = ? WHERE id_empleados = ?";

        try (Connection conn = connectionBD.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, empleado.getDni());
            stmt.setString(2, empleado.getNombres());
            stmt.setString(3, empleado.getApe_p());
            stmt.setString(4, empleado.getApe_m());
            stmt.setString(5, empleado.getCorreo());
            stmt.setInt(6, empleado.getDpto().getId_departamentos());
            stmt.setInt(7, empleado.getCargo().getId_cargos());
            stmt.setInt(8, empleado.getId_empleados()); // importante para identificar al empleado

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (Exception e) {
            System.err.println("Error al actualizar empleado: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarEmpleado(int idEmpleado) {
        ConnectionBD connectionBD = new ConnectionBD();
        String sql = "DELETE FROM empleados WHERE id_empleados = ?";

        try (Connection conn = connectionBD.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idEmpleado);
            int filas = stmt.executeUpdate();
            return filas > 0;

        } catch (Exception e) {
            System.err.println("Error al eliminar empleado: " + e.getMessage());
            return false;
        }
    }

    public int contarEmpleados() {
        ConnectionBD connectionBD = new ConnectionBD();
        String sql = "SELECT COUNT(*) AS total_empleados FROM empleados";

        try (Connection conn = connectionBD.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total_empleados");
            }

        } catch (Exception e) {
            System.err.println("Error al contar empleados: " + e.getMessage());
        }

        return 0;
    }

}
