package com.appcali.pantalla_principal.ui.Cargos;

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

import com.appcali.pantalla_principal.Adaptadores.ListaCargosAdapter;
import com.appcali.pantalla_principal.BaseFragment;
import com.appcali.pantalla_principal.Connection.CargosBD;
import com.appcali.pantalla_principal.R;
import com.appcali.pantalla_principal.entidades.Cargos;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class CargosFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ArrayList<Cargos> listaCargos;
    private ListaCargosAdapter adapter;

    private String textoBusqueda = "";
    private int cantidadSeleccionada = 10; // Valor por defecto


    public CargosFragment() {
    }
    public static CargosFragment newInstance(String param1, String param2) {
        CargosFragment fragment = new CargosFragment();
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

        View view = inflater.inflate(R.layout.fragment_cargos, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.rv_cargos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        CargosBD cargoBD = new CargosBD();
        listaCargos = cargoBD.obtenerCargos();
        adapter = new ListaCargosAdapter(listaCargos);
        recyclerView.setAdapter(adapter);

        adapter.setOnCargoClickListener(new ListaCargosAdapter.OnCargoClickListener() {
            @Override
            public void onEditarClick(Cargos cargo) {
                mostrarDialogoEditarCargo(cargo);
            }

            @Override
            public void onActivarDesactivarClick(Cargos cargo) {
                mostrarDialogoActivarDesactivarCargo(cargo);
            }
        });

        Spinner spCantidad = view.findViewById(R.id.spCantidad);
        ImageButton ibtnFiltrador = view.findViewById(R.id.ibtn_filtrador);

        ibtnFiltrador.setOnClickListener(v -> spCantidad.performClick());

        spCantidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String opcion = parent.getItemAtPosition(position).toString();
                cantidadSeleccionada = Integer.parseInt(opcion);
                aplicarFiltros();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        EditText txtBuscar = view.findViewById(R.id.txtbuscarcargos);

        txtBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textoBusqueda = s.toString();
                aplicarFiltros();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


        ImageButton btnAgregar = view.findViewById(R.id.btn_agregar);
        btnAgregar.setOnClickListener(v -> mostrarDialogoAgregarCargo());

        return view;
    }

    private void mostrarDialogoAgregarCargo() {
        View formulario = LayoutInflater.from(getContext()).inflate(R.layout.dialog_forms, null);
        EditText etNombreCargo = formulario.findViewById(R.id.etNombre);
        Button btnGuardar = formulario.findViewById(R.id.btnGuardar);

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(formulario)
                .create();

        btnGuardar.setOnClickListener(v -> {
            String nombre = etNombreCargo.getText().toString().trim();

            if (nombre.isEmpty()) {
                etNombreCargo.setError("Ingrese un nombre");
                return;
            }

            CargosBD bd = new CargosBD();
            boolean ok = bd.insertarCargo(nombre);

            if (ok) {
                listaCargos.clear();
                listaCargos.addAll(bd.obtenerCargos());
                adapter.notifyDataSetChanged();
                Toasty.success(getContext(), "✅ Cargo agregado correctamente", Toasty.LENGTH_SHORT, true).show();
            } else {
                Toasty.error(getContext(), "❌ Error al agregar el cargo", Toasty.LENGTH_SHORT, true).show();
            }

            dialog.dismiss();
        });

        dialog.show();
    }


    private void mostrarDialogoEditarCargo(Cargos cargo) {
        View formulario = LayoutInflater.from(getContext()).inflate(R.layout.dialog_forms, null);
        EditText etNombreCargo = formulario.findViewById(R.id.etNombre);
        TextView tvTitulo = formulario.findViewById(R.id.tvTitulo);
        Button btnGuardar = formulario.findViewById(R.id.btnGuardar);

        tvTitulo.setText("Editar Cargo");

        etNombreCargo.setText(cargo.getNombre());

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(formulario)
                .create();

        btnGuardar.setOnClickListener(v -> {
            String nuevoNombre = etNombreCargo.getText().toString().trim();

            if (nuevoNombre.isEmpty()) {
                etNombreCargo.setError("Ingrese un nombre válido");
                return;
            }

            CargosBD bd = new CargosBD();
            boolean ok = bd.actualizarCargo(cargo.getId_cargos(), nuevoNombre, cargo.getEstado());

            if (ok) {
                cargo.setNombre(nuevoNombre);
                adapter.notifyDataSetChanged();
                Toasty.success(getContext(), "✅ Cargo actualizado", Toasty.LENGTH_SHORT, true).show();
            } else {
                Toasty.error(getContext(), "❌ Error al actualizar cargo", Toasty.LENGTH_SHORT, true).show();
            }

            dialog.dismiss();
        });

        dialog.show();
    }

    private void mostrarDialogoActivarDesactivarCargo(Cargos cargo) {
        String nuevoEstado = cargo.getEstado().equalsIgnoreCase("Activo") ? "Inactivo" : "Activo";

        new AlertDialog.Builder(getContext())
                .setTitle("Cambiar estado")
                .setMessage("¿Deseas cambiar el estado a \"" + nuevoEstado + "\"?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    CargosBD bd = new CargosBD();
                    boolean ok = bd.actualizarCargo(cargo.getId_cargos(), cargo.getNombre(), nuevoEstado);

                    if (ok) {
                        cargo.setEstado(nuevoEstado);
                        adapter.notifyDataSetChanged();
                        Toasty.info(getContext(), "Estado cambiado a " + nuevoEstado, Toasty.LENGTH_SHORT, true).show();
                    } else {
                        Toasty.error(getContext(), "Error al cambiar estado", Toasty.LENGTH_SHORT, true).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void filtrarPorTexto(String texto) {
        ArrayList<Cargos> filtrados = new ArrayList<>();
        for (Cargos c : listaCargos) {
            if (c.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                filtrados.add(c);
            }
        }
        adapter.actualizarLista(filtrados);
    }

    private void filtrarPorCantidad(int cantidad) {
        ArrayList<Cargos> filtrados = new ArrayList<>();
        for (int i = 0; i < Math.min(cantidad, listaCargos.size()); i++) {
            filtrados.add(listaCargos.get(i));
        }
        adapter.actualizarLista(filtrados);
    }

    private void aplicarFiltros() {
        ArrayList<Cargos> filtrados = new ArrayList<>();

        for (Cargos c : listaCargos) {
            if (c.getNombre().toLowerCase().contains(textoBusqueda.toLowerCase())) {
                filtrados.add(c);
            }
        }

        // Limitar por cantidad
        ArrayList<Cargos> resultadoFinal = new ArrayList<>();
        for (int i = 0; i < Math.min(cantidadSeleccionada, filtrados.size()); i++) {
            resultadoFinal.add(filtrados.get(i));
        }

        adapter.actualizarLista(resultadoFinal);
    }



}