package com.appcali.pantalla_principal.ui.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.appcali.pantalla_principal.Connection.ConnectionBD;
import com.appcali.pantalla_principal.Connection.UsuariosBD;
import com.appcali.pantalla_principal.Pantalla_opciones;
import com.appcali.pantalla_principal.R;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity {

    EditText usuario,clave;
    Button btningresar;
    Connection con;


    public LoginActivity(){
        ConnectionBD instanceConnection = new ConnectionBD();
        con = instanceConnection.connect();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login);

        usuario = (EditText) findViewById(R.id.txt_usuario);
        clave = (EditText) findViewById(R.id.txtpass);
        btningresar = (Button) findViewById(R.id.btn_ingresar);

        btningresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Login().execute("");
            }
        });
    }

    public class Login extends AsyncTask<String,String,String>{
        String z = null;
        Boolean exito = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {

            if (con == null){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "Verifique su conexion", Toast.LENGTH_SHORT).show();
                    }
                });
                z = "En conexion";
            }
            else {
                try {
                    String sql = "SELECT u.id_empleados, u.id_roles, e.id_cargos " +
                            "FROM usuarios u " +
                            "JOIN empleados e ON u.id_empleados = e.id_empleados " +
                            "WHERE u.nombre_usuario = '" + usuario.getText() + "' AND u.contraseña = '" + clave.getText() + "'";

                    Statement stm = con.createStatement();
                    ResultSet rs = stm.executeQuery(sql);

                    if (rs.next()) {
                        int idEmpleado = rs.getInt("id_empleados");
                        int idRol = rs.getInt("id_roles");
                        int idCargo = rs.getInt("id_cargos"); // ← Aquí obtienes el id_cargo

                        // Nombre completo del empleado
                        UsuariosBD usuariosBD = new UsuariosBD();
                        String nombreUsuarioInput = usuario.getText().toString();
                        String nombreCompleto = usuariosBD.obtenerNombreEmpleadoDesdeUsuario(nombreUsuarioInput);

                        Log.d("NOMBRE_EMPLEADO", "Nombre completo obtenido: " + nombreCompleto);

                        // Guardar en SharedPreferences
                        SharedPreferences preferences = getSharedPreferences("sesion", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("isLoggedIn", true);
                        editor.putString("nombreEmpleado", nombreCompleto);
                        editor.putInt("idEmpleado", idEmpleado);
                        editor.putString("nombreUsuario", nombreUsuarioInput);
                        editor.putInt("idRol", idRol);
                        editor.putInt("idCargo", idCargo); // ← Aquí lo guardas
                        editor.putString("modo", "admin");
                        editor.apply();

                        // Ir a la pantalla principal
                        runOnUiThread(() -> {
                            Toast.makeText(LoginActivity.this, "Acceso Exitoso", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, Pantalla_opciones.class);
                            intent.putExtra("rol", idRol);
                            startActivity(intent);
                            finish();
                        });

                        usuario.setText("");
                        clave.setText("");
                    }

                    else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "Error en el usuario o contraseña", Toast.LENGTH_SHORT).show();
                            }
                        });
                        usuario.setText("");
                        clave.setText("");
                    }

                }catch (Exception e){
                    exito = false;
                    Log.e("Error de Conexion :", e.getMessage());
                }
            }


            return z;
        }
    }

}