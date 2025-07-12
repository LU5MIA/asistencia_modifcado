package com.appcali.pantalla_principal.Connection;

import com.appcali.pantalla_principal.entidades.Asistencias;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class AsistenciasBD {

    public ArrayList<Asistencias> mostrarEmpleados() {
        ArrayList<Asistencias> lista = new ArrayList<>();
        ConnectionBD connectionBD = new ConnectionBD();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = connectionBD.connect();
            if (conn != null) {
                String sql = "SELECT a.id_asistencias, a.tipo, a.estado, e.nombres, e.ape_p, e.ape_m " +
                        "FROM asistencias a " +
                        "JOIN empleados e ON a.id_empleados = e.id_empleados";
                stmt = conn.prepareStatement(sql);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    Asistencias asistencia = new Asistencias();
                    asistencia.setId_asistencias(rs.getInt("id_asistencias"));
                    asistencia.setTipo(rs.getString("tipo"));
                    asistencia.setEstado(rs.getString("estado"));

                    // Unir los campos del nombre
                    String nombreCompleto = rs.getString("nombres") + " " +
                            rs.getString("ape_p");

                    asistencia.setNombre_completo(nombreCompleto);

                    lista.add(asistencia);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }

        return lista;
    }

    public ArrayList<Asistencias> mostrarAsistenciasPorEmpleado(int idEmpleado) {
        ArrayList<Asistencias> lista = new ArrayList<>();
        ConnectionBD connectionBD = new ConnectionBD();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = connectionBD.connect();
            if (conn != null) {
                String sql = "SELECT id_asistencias, marcación, tipo, estado FROM asistencias WHERE id_empleados = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, idEmpleado); // 👈 pasamos el ID del empleado
                rs = stmt.executeQuery();

                while (rs.next()) {
                    Asistencias asistencia = new Asistencias();
                    asistencia.setId_asistencias(rs.getInt("id_asistencias"));
                    asistencia.setMarcacion(rs.getString("marcación"));
                    asistencia.setTipo(rs.getString("tipo"));
                    asistencia.setEstado(rs.getString("estado"));
                    lista.add(asistencia);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }

        return lista;
    }

    public boolean insertarAsistencia(Asistencias asistencia) {
        ConnectionBD connectionBD = new ConnectionBD();
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean insertado = false;

        try {
            conn = connectionBD.connect();
            if (conn != null) {
                String sql = "INSERT INTO asistencias (marcación, tipo, estado, id_empleados) VALUES (?, ?, ?, ?)";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, asistencia.getMarcacion()); // Ejemplo: "2025-07-11 08:00:00"
                stmt.setString(2, asistencia.getTipo());      // Ejemplo: "entrada"
                stmt.setString(3, asistencia.getEstado());    // Ejemplo: "válido"
                stmt.setInt(4, asistencia.getId_empleado()); // ID del empleado
                int filas = stmt.executeUpdate();
                insertado = filas > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }

        return insertado;
    }

    public boolean existeAsistencia(String tipo, int idEmpleado, String fecha) {
        ConnectionBD connectionBD = new ConnectionBD();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean existe = false;

        try {
            conn = connectionBD.connect();
            if (conn != null) {
                String sql = "SELECT COUNT(*) FROM asistencias " +
                        "WHERE tipo = ? AND id_empleados = ? AND DATE(marcación) = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, tipo);
                stmt.setInt(2, idEmpleado);
                stmt.setString(3, fecha);

                rs = stmt.executeQuery();
                if (rs.next()) {
                    existe = rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }

        return existe;
    }

    public boolean existeAsistenciaDelDia(int idEmpleado, String tipo, String fechaActual) {
        ConnectionBD connectionBD = new ConnectionBD();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean existe = false;

        try {
            conn = connectionBD.connect();
            if (conn != null) {
                String sql = "SELECT COUNT(*) FROM asistencias WHERE id_empleados = ? AND tipo = ? AND DATE(marcación) = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, idEmpleado);
                stmt.setString(2, tipo);
                stmt.setString(3, fechaActual);

                rs = stmt.executeQuery();
                if (rs.next()) {
                    existe = rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }

        return existe;
    }










}
