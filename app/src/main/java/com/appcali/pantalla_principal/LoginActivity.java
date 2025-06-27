package com.appcali.pantalla_principal;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.appcali.pantalla_principal.Connection.ConnectionBD;

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
                    String sql = "SELECT * FROM usuarios WHERE nombre_usuario = '" + usuario.getText() + "' AND contraseña = '" + clave.getText() + "'" ;
                    Statement stm = con.createStatement();
                    ResultSet rs = stm.executeQuery(sql);

                    if (rs.next()){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "Acceso Exitoso", Toast.LENGTH_SHORT).show();
                                Intent menu = new Intent(getApplicationContext(), Pantalla_opciones.class);
                                startActivity(menu);
                            }
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

//    public void IrSegundaPantalla (View view){
//        Intent intent = new Intent(this, Pantalla_opciones.class);
//        startActivity(intent);
//    }
}