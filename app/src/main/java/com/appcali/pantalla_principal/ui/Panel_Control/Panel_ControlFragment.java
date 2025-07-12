package com.appcali.pantalla_principal.ui.Panel_Control;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appcali.pantalla_principal.ui.Configuracion.ConfiguracionFragment;
import com.appcali.pantalla_principal.Connection.CargosBD;
import com.appcali.pantalla_principal.Connection.DepartamentosBD;
import com.appcali.pantalla_principal.Connection.EmpleadosBD;
import com.appcali.pantalla_principal.Connection.RolesBD;
import com.appcali.pantalla_principal.R;
import com.appcali.pantalla_principal.ui.Nosotros.NosotrosActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Panel_ControlFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Panel_ControlFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Panel_ControlFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Panel_Control.
     */
    // TODO: Rename and change types and number of parameters
    public static Panel_ControlFragment newInstance(String param1, String param2) {
        Panel_ControlFragment fragment = new Panel_ControlFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_panel_control, container, false);

        // Buscar los botones
        View btnNosotros = view.findViewById(R.id.btnnosotros);
        View btnConfiguracion = view.findViewById(R.id.btnconfiguracion);
        btnConfiguracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.configuracion); // El ID del destino en tu nav_graph
            }
        });

        // Listener para abrir NosotrosActivity
        btnNosotros.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), NosotrosActivity.class));
        });

        TextView tvTotalEmpleados = view.findViewById(R.id.tvTotalEmpleados);
        TextView tvTotalDepartamentos = view.findViewById(R.id.tvTotalDepartamentos);
        TextView tvTotalCargos = view.findViewById(R.id.tvTotalCargos);
        TextView tvTotalRoles = view.findViewById(R.id.tvTotalRoles);

        int totalEmpleados = new EmpleadosBD().contarEmpleados();
        int totalDepartamentos = new DepartamentosBD().contarDepartamentos();
        int totalCargos = new CargosBD().contarCargos();
        int totalRoles = new RolesBD().contarRoles();

        tvTotalEmpleados.setText(String.valueOf(totalEmpleados));
        tvTotalDepartamentos.setText(String.valueOf(totalDepartamentos));
        tvTotalCargos.setText(String.valueOf(totalCargos));
        tvTotalRoles.setText(String.valueOf(totalRoles));

        return view;
    }


}