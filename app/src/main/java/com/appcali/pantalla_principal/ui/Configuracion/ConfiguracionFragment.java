package com.appcali.pantalla_principal.ui.Configuracion;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appcali.pantalla_principal.Connection.ConfiguracionBD;
import com.appcali.pantalla_principal.R;
import com.appcali.pantalla_principal.entidades.Configuracion;

import java.util.ArrayList;

public class ConfiguracionFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ConfiguracionFragment() {
    }
    public static ConfiguracionFragment newInstance(String param1, String param2) {
        ConfiguracionFragment fragment = new ConfiguracionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_configuracion, container, false);

        // Referenciar los TextViews
        TextView lblempresa = view.findViewById(R.id.lblempresa);
        TextView lbldireccion = view.findViewById(R.id.lbldireccion);
        TextView lblaper_sis = view.findViewById(R.id.lblaper_sis);
        TextView lbl_hora_entrada = view.findViewById(R.id.lbl_hora_entrada);
        TextView lblcierresis = view.findViewById(R.id.lblcierresis);
        TextView lblubilatitud = view.findViewById(R.id.lblubilatitud);
        TextView lblubilongitud = view.findViewById(R.id.lblubilongitud);
        TextView lblradio = view.findViewById(R.id.lblradio);

        // Obtener datos desde la base de datos
        ConfiguracionBD configuracionBD = new ConfiguracionBD(); // tu clase DAO
        ArrayList<Configuracion> lista = configuracionBD.obtenerConfiguracion();

        if (!lista.isEmpty()) {
            Configuracion config = lista.get(0); // solo tomamos el primero

            lblempresa.setText(config.getNombre_empresa());
            lbldireccion.setText(config.getDireccion());
            lblaper_sis.setText(config.getApertura_sistema());
            lbl_hora_entrada.setText(config.getHora_entrada());
            lblcierresis.setText(config.getCierre_sistema());
            lblubilatitud.setText(String.valueOf(config.getUbicacion_latitud()));
            lblubilongitud.setText(String.valueOf(config.getUbicacion_longitud()));
            lblradio.setText(String.valueOf(config.getRadio()));
        }

        return view;
    }
}