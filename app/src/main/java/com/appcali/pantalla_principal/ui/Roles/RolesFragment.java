package com.appcali.pantalla_principal.ui.Roles;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.appcali.pantalla_principal.Adaptadores.ListaRolesAdapter;
import com.appcali.pantalla_principal.BaseFragment;
import com.appcali.pantalla_principal.Connection.RolesBD;
import com.appcali.pantalla_principal.R;
import com.appcali.pantalla_principal.entidades.Roles;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class RolesFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ArrayList<Roles> listaRoles;
    private ListaRolesAdapter adapter;

    public RolesFragment() {}

    public static RolesFragment newInstance(String param1, String param2) {
        RolesFragment fragment = new RolesFragment();
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

        View view = inflater.inflate(R.layout.fragment_roles, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.rv_roles);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        RolesBD rolBD = new RolesBD();
        listaRoles = rolBD.obtenerRoles();

        adapter = new ListaRolesAdapter(listaRoles);
        recyclerView.setAdapter(adapter);

        EditText txtBuscarRol = view.findViewById(R.id.txtbuscarrol);

        txtBuscarRol.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrarRoles(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        Spinner spCantidad = view.findViewById(R.id.spCantidad);

        // Mostrar spinner al presionar el ImageButton (opcional)
        ImageButton ibtnFiltrador = view.findViewById(R.id.ibtn_filtrador);
        ibtnFiltrador.setOnClickListener(v -> {
            spCantidad.performClick(); // Mostrar el spinner
        });

        spCantidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String seleccion = parent.getItemAtPosition(position).toString();
                int cantidad = Integer.parseInt(seleccion);

                filtrarPorCantidad(cantidad); // Esto se activa solo cuando el usuario elige una cantidad
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        adapter.setOnRolesClickListener(new ListaRolesAdapter.OnRolClickListener() {
            @Override
            public void onEditarClick(Roles rol) {
                mostrarDialogoEditarRol(rol);
            }

        });

        ImageButton btnAgregarRol = view.findViewById(R.id.btn_agregar_rol);
        btnAgregarRol.setOnClickListener(v -> mostrarDialogoAgregarRol());


        return view;
    }

    private void mostrarDialogoAgregarRol() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View form = inflater.inflate(R.layout.dialog_forms, null);
        TextView tvTitulo = form.findViewById(R.id.tvTitulo);
        EditText etNombre = form.findViewById(R.id.etNombre);
        Button btnGuardar = form.findViewById(R.id.btnGuardar);

        tvTitulo.setText("Agregar Rol");

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(form)
                .create();

        btnGuardar.setOnClickListener(v -> {
            String nombreRol = etNombre.getText().toString().trim();

            if (nombreRol.isEmpty()) {
                etNombre.setError("Ingrese un nombre");
                return;
            }

            RolesBD bd = new RolesBD();
            boolean agregado = bd.agregarRol(nombreRol, "Activo");

            if (agregado) {
                // Vuelve a cargar la lista completa desde la BD
                listaRoles.clear();
                listaRoles.addAll(bd.obtenerRoles());

                // Mostrar todos los roles sin aplicar filtro
                adapter.actualizarLista(listaRoles);

                Toasty.success(getContext(), "Rol agregado", Toasty.LENGTH_SHORT).show();
            } else {
                Toasty.error(getContext(), "Error al agregar", Toasty.LENGTH_SHORT).show();
            }

            dialog.dismiss();
        });

        dialog.show();
    }


    private void mostrarDialogoEditarRol(Roles rol) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View form = inflater.inflate(R.layout.dialog_forms, null);
        TextView tvTitulo = form.findViewById(R.id.tvTitulo);
        EditText etNombre = form.findViewById(R.id.etNombre);
        Button btnGuardar = form.findViewById(R.id.btnGuardar);

        tvTitulo.setText("Editar Rol");
        etNombre.setText(rol.getNombre());

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(form)
                .create();

        btnGuardar.setOnClickListener(v -> {
            String nuevoNombre = etNombre.getText().toString().trim();

            if (nuevoNombre.isEmpty()) {
                etNombre.setError("Ingrese un nombre");
                return;
            }

            RolesBD bd = new RolesBD();
            boolean actualizado = bd.actualizarRol(rol.getId_roles(), nuevoNombre);

            if (actualizado) {
                rol.setNombre(nuevoNombre);
                adapter.notifyDataSetChanged();
                Toasty.success(getContext(), "Rol actualizado", Toasty.LENGTH_SHORT).show();
            } else {
                Toasty.error(getContext(), "Error al actualizar", Toasty.LENGTH_SHORT).show();
            }

            dialog.dismiss();
        });

        dialog.show();
    }

    private void filtrarRoles(String texto) {
        ArrayList<Roles> listaFiltrada = new ArrayList<>();

        for (Roles rol : listaRoles) {
            if (rol.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                listaFiltrada.add(rol);
            }
        }

        adapter.actualizarLista(listaFiltrada);
    }

    private void filtrarPorCantidad(int cantidad) {
        ArrayList<Roles> listaFiltrada = new ArrayList<>();

        // Asegúrate de tener una lista original con todos los roles
        for (int i = 0; i < Math.min(cantidad, listaRoles.size()); i++) {
            listaFiltrada.add(listaRoles.get(i));
        }

        adapter.actualizarLista(listaFiltrada);
    }



}
