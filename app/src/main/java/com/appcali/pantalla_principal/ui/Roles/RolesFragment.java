package com.appcali.pantalla_principal.ui.Roles;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.appcali.pantalla_principal.Adaptadores.ListaRolesAdapter;
import com.appcali.pantalla_principal.BaseFragment;
import com.appcali.pantalla_principal.Connection.RolesBD;
import com.appcali.pantalla_principal.R;
import com.appcali.pantalla_principal.entidades.Roles;

import java.util.ArrayList;

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


        adapter.setOnRolesClickListener(new ListaRolesAdapter.OnRolClickListener() {
            @Override
            public void onEditarClick(Roles rol) {
                mostrarDialogoEditarRol(rol);
            }
        });

        return view;
    }

    private void mostrarDialogoEditarRol(Roles rol) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View form = inflater.inflate(R.layout.dialog_forms, null);

        EditText etNombre = form.findViewById(R.id.etNombre);
        Button btnGuardar = form.findViewById(R.id.btnGuardar);

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
}
