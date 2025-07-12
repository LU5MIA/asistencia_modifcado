package com.appcali.pantalla_principal.Connection;

import android.util.Log;

import com.appcali.pantalla_principal.entidades.Empleado;
import com.appcali.pantalla_principal.entidades.Usuarios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UsuariosBD {

    public String obtenerNombreEmpleadoDesdeUsuario(String usuario) {

        String nombre = "";
        ConnectionBD connectionBD = new ConnectionBD();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = connectionBD.connect();
            if (conn != null) {
                String sql = "SELECT e.nombres, e.ape_p, e.ape_m " +
                        "FROM usuarios u " +
                        "JOIN empleados e ON u.id_empleados = e.id_empleados " +
                        "WHERE u.nombre_usuario = ?"; // ← Cambiado aquí

                stmt = conn.prepareStatement(sql);
                stmt.setString(1, usuario);
                Log.d("DEBUG_SQL", "Ejecutando con usuario: " + usuario);

                rs = stmt.executeQuery();

                if (rs.next()) {
                    nombre = rs.getString("nombres") + " " +
                            rs.getString("ape_p") + " " +
                            rs.getString("ape_m");
                    Log.d("DEBUG_SQL", "Nombre encontrado: " + nombre);
                } else {
                    Log.d("DEBUG_SQL", "No se encontró el usuario.");
                }
            }
        } catch (Exception e) {
            Log.e("ERROR_SQL", "Error: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }

        return nombre;
    }

    public Usuarios obtenerDatosUsuario(String nombreUsuarioInput) {
        Usuarios usuario = null;
        ConnectionBD connectionBD = new ConnectionBD();
        Connection connection = connectionBD.connect();

        if (connection != null) {
            try {
                String query = "SELECT u.id_usuarios, u.id_empleados, u.nombre_usuario, u.contraseña, " +
                        "e.dni, e.nombres, e.ape_p, e.ape_m, e.correo_electronico " +
                        "FROM usuarios u " +
                        "INNER JOIN empleados e ON u.id_empleados = e.id_empleados " +
                        "WHERE u.nombre_usuario = ?";

                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, nombreUsuarioInput);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    // Datos del usuario
                    int idUsuarios = resultSet.getInt("id_usuarios");
                    int idEmpleados = resultSet.getInt("id_empleados");
                    String nombreUsuario = resultSet.getString("nombre_usuario");
                    String password = resultSet.getString("contraseña");

                    // Datos del empleado
                    String dni = resultSet.getString("dni");
                    String nombres = resultSet.getString("nombres");
                    String apeP = resultSet.getString("ape_p");
                    String apeM = resultSet.getString("ape_m");
                    String correo = resultSet.getString("correo_electronico");

                    Empleado empleado = new Empleado();
                    empleado.setId_empleados(idEmpleados);
                    empleado.setDni(dni);
                    empleado.setNombres(nombres);
                    empleado.setApe_p(apeP);
                    empleado.setApe_m(apeM);
                    empleado.setCorreo(correo);

                    usuario = new Usuarios(idUsuarios, idEmpleados, nombreUsuario, password, empleado);
                }

                resultSet.close();
                statement.close();
                connection.close();
            } catch (Exception e) {
                Log.e("BD_ERROR", "Error al obtener datos del usuario: " + e.getMessage());
            }
        }

        return usuario;
    }

    public boolean actualizarUsuario(String nombreUsuarioActual, String nuevoNombreUsuario, String nuevaContraseña) {
        boolean resultado = false;
        ConnectionBD connectionBD = new ConnectionBD();
        Connection connection = connectionBD.connect();

        if (connection != null) {
            try {
                String query = "UPDATE usuarios SET nombre_usuario = ?, contraseña = ? WHERE nombre_usuario = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, nuevoNombreUsuario);
                statement.setString(2, nuevaContraseña);
                statement.setString(3, nombreUsuarioActual);

                int filasAfectadas = statement.executeUpdate();
                resultado = filasAfectadas > 0;

                statement.close();
                connection.close();
            } catch (Exception e) {
                Log.e("BD_ERROR", "Error al actualizar usuario: " + e.getMessage());
            }
        }

        return resultado;
    }



}
