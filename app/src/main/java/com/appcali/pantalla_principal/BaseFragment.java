package com.appcali.pantalla_principal;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.appcali.pantalla_principal.R;
import com.appcali.pantalla_principal.componentes.HeaderView;

public abstract class BaseFragment extends Fragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        HeaderView header = view.findViewById(R.id.header_view);
        if (header != null) {
            SharedPreferences preferences = requireContext().getSharedPreferences("sesion", Context.MODE_PRIVATE);
            int idRol = preferences.getInt("idRol", -1);
            String modo = preferences.getString("modo", "admin");

            // Solo si es administrador (idRol == 1), se muestra el botón
            header.configurarModo(idRol == 1, modo);
        }
    }
}

