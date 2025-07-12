package com.appcali.pantalla_principal;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.appcali.pantalla_principal.componentes.HeaderView;
import com.appcali.pantalla_principal.databinding.ActivityPantallaOpcionesBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class Pantalla_opciones extends AppCompatActivity {

    private ActivityPantallaOpcionesBinding binding;
    private BottomSheetDialog dialogMas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityPantallaOpcionesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



// Leer preferencias
        SharedPreferences preferences = getSharedPreferences("sesion", MODE_PRIVATE);
        int idRol = preferences.getInt("idRol", -1);
        String modo = preferences.getString("modo", "admin");  // admin o empleado
        boolean esAdmin = (idRol == 1);


        BottomNavigationView navView = findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        navView.setItemActiveIndicatorColor(ColorStateList.valueOf(Color.parseColor("#24ce9e")));


// NAV CONTROLLER
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_pantalla_opciones);

// Mostrar/ocultar ítems y redireccionar según rol y modo
        if (idRol == 1 && modo.equals("admin")) {
            // ADMINISTRADOR - VISTA ADMIN
            setVisibleIfExists(menu, R.id.panel, true);
            setVisibleIfExists(menu, R.id.perfil, false);
            setVisibleIfExists(menu, R.id.mas, true);
            setVisibleIfExists(menu, R.id.registro, false);
            setVisibleIfExists(menu, R.id.mis_asistencias, false);

            navController.navigate(R.id.panel);
            navView.setSelectedItemId(R.id.panel);

        } else {
            // EMPLEADO o ADMIN en modo EMPLEADO
            setVisibleIfExists(menu, R.id.panel, false);
            setVisibleIfExists(menu, R.id.perfil, true);
            setVisibleIfExists(menu, R.id.mas, false);
            setVisibleIfExists(menu, R.id.registro, true);
            setVisibleIfExists(menu, R.id.mis_asistencias, true);

            navController.navigate(R.id.registro);
            navView.setSelectedItemId(R.id.registro);
        }

// Listener para cambiar fragmentos
        navView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.mas) {
                mostrarMenuMas(navController);
                return true;
            } else {
                navController.popBackStack(navController.getGraph().getStartDestinationId(), false);
                navController.navigate(itemId);
                return true;
            }
        });

// Cierra el menú inferior si está abierto
        navController.addOnDestinationChangedListener((controller, destination, args) -> {
            if (dialogMas != null && dialogMas.isShowing()) {
                dialogMas.dismiss();
            }
        });

    }

    private void mostrarMenuMas(NavController navController) {
        dialogMas = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_mas, null);
        dialogMas.setContentView(view);

        // Leer el rol para ocultar si no es admin
        SharedPreferences preferences = getSharedPreferences("sesion", MODE_PRIVATE);
        int idRol = preferences.getInt("idRol", -1);

        // Botones funcionales
        view.findViewById(R.id.btnEmpleados).setOnClickListener(v -> {
            dialogMas.dismiss();
            navController.navigate(R.id.empleados);
        });

        view.findViewById(R.id.btnDepartamentos).setOnClickListener(v -> {
            dialogMas.dismiss();
            navController.navigate(R.id.departamentos);
        });

        view.findViewById(R.id.btnCargos).setOnClickListener(v -> {
            dialogMas.dismiss();
            navController.navigate(R.id.cargos);
        });

        view.findViewById(R.id.btnRoles).setOnClickListener(v -> {
            dialogMas.dismiss();
            navController.navigate(R.id.roles);
        });

        view.findViewById(R.id.btninforme).setOnClickListener(v -> {
            dialogMas.dismiss();
            navController.navigate(R.id.informe_asis);
        });

        // Si no es administrador, ocultar botones
        if (idRol != 1) {
            view.findViewById(R.id.btnEmpleados).setVisibility(View.GONE);
            view.findViewById(R.id.btnDepartamentos).setVisibility(View.GONE);
            view.findViewById(R.id.btnCargos).setVisibility(View.GONE);
            view.findViewById(R.id.btnRoles).setVisibility(View.GONE);
            view.findViewById(R.id.btninforme).setVisibility(View.GONE);
        }

        dialogMas.show();
    }

    // Método auxiliar para evitar NullPointerException
    private void setVisibleIfExists(Menu menu, int itemId, boolean visible) {
        MenuItem item = menu.findItem(itemId);
        if (item != null) {
            item.setVisible(visible);
        }
    }


}
