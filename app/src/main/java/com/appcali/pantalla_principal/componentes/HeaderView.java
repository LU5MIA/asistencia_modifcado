package com.appcali.pantalla_principal.componentes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appcali.pantalla_principal.Connection.CargosBD;
import com.appcali.pantalla_principal.ui.Login.LoginActivity;
import com.appcali.pantalla_principal.R;

public class HeaderView extends LinearLayout {

    private ImageButton btnSesion, btnModo;

    public HeaderView(Context context) {
        super(context);
        init(context);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.header, this, true);

        btnSesion = findViewById(R.id.ibtn_sesion);
        btnModo = findViewById(R.id.ibtn_Modo);

        btnSesion.setOnClickListener(v -> {
            mostrarDialogoCerrarSesion();
        });
    }

    private void mostrarDialogoCerrarSesion() {
        SharedPreferences preferences = getContext().getSharedPreferences("sesion", Context.MODE_PRIVATE);
        String nombreEmpleado = preferences.getString("nombreEmpleado", "Empleado desconocido");
        int idCargo = preferences.getInt("idCargo", -1);

        // Obtener nombre del cargo desde la BD
        String nombreCargo = "";
        if (idCargo != -1) {
            nombreCargo = new CargosBD().obtenerNombreCargoDesdeId(idCargo);
            if (nombreCargo == null || nombreCargo.trim().isEmpty()) {
                nombreCargo = ""; // no mostrar texto si no existe el cargo
            }
        }

        View vista = LayoutInflater.from(getContext()).inflate(R.layout.dialog_cerrar_sesion, null);
        TextView txtNombre = vista.findViewById(R.id.txt_nombre_empleado);
        TextView txtCargo = vista.findViewById(R.id.txt_cargo_empleado);
        Button btnCerrarSesion = vista.findViewById(R.id.btn_cerrar_sesion);

        txtNombre.setText(nombreEmpleado);
        txtNombre.setText(nombreEmpleado);

        if (nombreCargo.isEmpty()) {
            txtCargo.setVisibility(View.GONE); // Oculta el espacio del cargo
        } else {
            txtCargo.setText(nombreCargo);
            txtCargo.setVisibility(View.VISIBLE); // Asegúrate de mostrarlo si sí hay
        }

        txtCargo.setText(nombreCargo);  // Se mostrará vacío si no hay cargo

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(vista)
                .setCancelable(true)
                .create();

        btnCerrarSesion.setOnClickListener(v -> {
            preferences.edit().clear().apply();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            getContext().startActivity(intent);
            dialog.dismiss();
        });

        dialog.show();
    }

    public void configurarModo(boolean esAdministrador, String modoActual) {
        if (!esAdministrador) {
            btnModo.setVisibility(View.GONE); // Oculta y libera espacio
        } else {
            btnModo.setVisibility(View.VISIBLE);

            if ("admin".equals(modoActual)) {
                btnModo.setImageResource(R.drawable.outline_admin_panel_settings_24);
            } else {
                btnModo.setImageResource(R.drawable.outline_person_24);
            }

            btnModo.setOnClickListener(v -> {
                SharedPreferences preferences = getContext().getSharedPreferences("sesion", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                String nuevoModo = modoActual.equals("admin") ? "empleado" : "admin";
                editor.putString("modo", nuevoModo);
                editor.apply();

                Intent intent = new Intent(getContext(), getContext().getClass());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getContext().startActivity(intent);
            });
        }
    }

}
