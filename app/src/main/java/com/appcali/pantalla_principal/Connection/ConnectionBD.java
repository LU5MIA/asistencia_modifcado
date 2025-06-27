package com.appcali.pantalla_principal.Connection;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionBD {

    private String ip = "192.168.0.149";

    private String puerto = "3306";
    private String usuario = "root";
    private String password = "";
    private String basededatos = "asistencias";


    @SuppressLint("NewApi")
    public Connection connect() {
        Connection connection = null;
        String connectionURL;

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            // Cargar el driver de MySQL
            Class.forName("com.mysql.jdbc.Driver");

            // Construir la URL de conexión
            connectionURL = "jdbc:mysql://" + this.ip + ":" + this.puerto + "/" + this.basededatos +
                    "?user=" + this.usuario + "&password=" + this.password;

            // Establecer la conexión
            connection = DriverManager.getConnection(connectionURL);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error de conexión SQL: ", e.getMessage());
        }

        return connection;
    }



}
