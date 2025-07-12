package com.appcali.pantalla_principal.ui.AsisAdministrador;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appcali.pantalla_principal.Adaptadores.ListaAsisAdministradorAdapter;
import com.appcali.pantalla_principal.BaseFragment;
import com.appcali.pantalla_principal.Connection.AsistenciasBD;
import com.appcali.pantalla_principal.R;
import com.appcali.pantalla_principal.databinding.FragmentAsisAdminBinding;
import com.appcali.pantalla_principal.entidades.Asistencias;

import java.util.ArrayList;

public class AsisAdminFragment extends BaseFragment {

    private ImageButton ibtn_Buscar, ibtn_filtrar;
    private EditText etBuscar;
    private Spinner spCantidad;
    private FragmentAsisAdminBinding binding;
    private RecyclerView recyclerView;
    private ArrayList<Asistencias> listaAsistencias;
    private ListaAsisAdministradorAdapter adapter;
    private ArrayList<Asistencias> listaOriginal; // Para guardar la lista completa


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAsisAdminBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ibtn_Buscar = root.findViewById(R.id.ibtnBuscar);
        etBuscar = root.findViewById(R.id.txtfecha);
        ibtn_filtrar = root.findViewById(R.id.ibtn_filtrador);
        spCantidad = root.findViewById(R.id.spCantidad);
        recyclerView = root.findViewById(R.id.rv_tabla_asistencias);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        cargarAsistencias();


        // Abrir el selector con un solo clic, sin mostrar el Spinner
        ibtn_filtrar.setOnClickListener(v -> spCantidad.performClick());

        // Escuchar selección del usuario
        spCantidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            private boolean isFirstSelection = true;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    int cantidad = Integer.parseInt(parent.getItemAtPosition(position).toString());
                    mostrarCantidadFiltrada(cantidad);

                    // Después de ejecutar por primera vez, marcar como ya iniciada
                    if (isFirstSelection) {
                        isFirstSelection = false;
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nada
            }
        });

        //para la busqueda del empleado

        ibtn_Buscar.setOnClickListener(v -> {
            String textoBusqueda = etBuscar.getText().toString().trim().toLowerCase();
            buscarEmpleado(textoBusqueda);
        });

        //busqueda en tiempo real al empleado

        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                buscarEmpleado(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return root;
    }

    private void cargarAsistencias() {
        AsistenciasBD asistenciasBD = new AsistenciasBD();
        listaAsistencias = asistenciasBD.mostrarEmpleados();
        listaOriginal = new ArrayList<>(listaAsistencias); // Guarda una copia completa
        adapter = new ListaAsisAdministradorAdapter(listaAsistencias);
        recyclerView.setAdapter(adapter);
    }

    private void buscarEmpleado(String texto) {
        ArrayList<Asistencias> listaFiltrada = new ArrayList<>();

        for (Asistencias asistencia : listaAsistencias) {
            if (asistencia.getNombre_completo().toLowerCase().contains(texto.toLowerCase())) {
                listaFiltrada.add(asistencia);
            }
        }

        ListaAsisAdministradorAdapter adapter = new ListaAsisAdministradorAdapter(listaFiltrada);
        recyclerView.setAdapter(adapter);
    }


    private void mostrarCantidadFiltrada(int cantidad) {
        ArrayList<Asistencias> listaFiltrada = new ArrayList<>();

        for (int i = 0; i < cantidad && i < listaOriginal.size(); i++) {
            listaFiltrada.add(listaOriginal.get(i));
        }

        adapter = new ListaAsisAdministradorAdapter(listaFiltrada);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
