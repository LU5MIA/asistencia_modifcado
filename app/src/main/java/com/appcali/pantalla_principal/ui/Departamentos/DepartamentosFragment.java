package com.appcali.pantalla_principal.ui.Departamentos;

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

import com.appcali.pantalla_principal.Adaptadores.ListaDepartamentosAdapter;
import com.appcali.pantalla_principal.BaseFragment;
import com.appcali.pantalla_principal.Connection.DepartamentosBD;
import com.appcali.pantalla_principal.R;
import com.appcali.pantalla_principal.entidades.Departamentos;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class DepartamentosFragment extends BaseFragment {

    private ArrayList<Departamentos> listaDepartamentos;
    private ListaDepartamentosAdapter adapter;

    private String textoBusqueda = "";
    private int cantidadSeleccionada = 10; // Valor por defecto


    public DepartamentosFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_departamentos, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.rv_departamentos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        DepartamentosBD dptoBD = new DepartamentosBD();
        listaDepartamentos = dptoBD.obtenerDepartamentos();
        adapter = new ListaDepartamentosAdapter(listaDepartamentos);
        recyclerView.setAdapter(adapter);


        adapter.setOnDepartamentoClickListener(new ListaDepartamentosAdapter.OnDepartamentoClickListener() {
            @Override
            public void onEditarClick(Departamentos departamento) {
                mostrarDialogoEditarDepartamento(departamento);
            }

            @Override
            public void onActivarDesactivarClick(Departamentos departamento) {
                mostrarDialogoActivarDesactivarDepartamento(departamento);
            }
        });

        EditText txtBuscar = view.findViewById(R.id.txtbuscardpto);

        // Agregar escucha en tiempo real al buscar
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

        Spinner spCantidad = view.findViewById(R.id.spCantidad);
        ImageButton btnFiltrador = view.findViewById(R.id.ibtn_filtrador);

        btnFiltrador.setOnClickListener(v -> spCantidad.performClick());

        spCantidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view1, int position, long id) {
                String opcion = parent.getItemAtPosition(position).toString();
                cantidadSeleccionada = Integer.parseInt(opcion);
                aplicarFiltros();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                cantidadSeleccionada = listaDepartamentos.size();
                aplicarFiltros();
            }
        });

        ImageButton btnAgregar = view.findViewById(R.id.btn_agregar_dpto);
        btnAgregar.setOnClickListener(v -> mostrarDialogoAgregarDepartamento());

        return view;
    }

    private void mostrarDialogoAgregarDepartamento() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View formulario = inflater.inflate(R.layout.dialog_forms, null);
        EditText etNombredpto = formulario.findViewById(R.id.etNombre);
        Button btnGuardar = formulario.findViewById(R.id.btnGuardar);

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(formulario)
                .create();

        btnGuardar.setOnClickListener(v -> {
            String nombredpto = etNombredpto.getText().toString().trim();

            if (nombredpto.isEmpty()) {
                etNombredpto.setError("Ingrese un nombre");
                return;
            }

            DepartamentosBD bdpto = new DepartamentosBD();
            boolean resultado = bdpto.insertarDepartamento(nombredpto);

            if (resultado) {
                listaDepartamentos.clear();
                listaDepartamentos.addAll(bdpto.obtenerDepartamentos());
                adapter.notifyDataSetChanged();

                Toasty.success(getContext(), "✅ Departamento agregado correctamente", Toasty.LENGTH_LONG, true).show();
            } else {
                Toasty.error(getContext(), "❌ Error al agregar el departamento", Toasty.LENGTH_LONG, true).show();
            }

            dialog.dismiss();
        });

        dialog.show();
    }

    private void mostrarDialogoEditarDepartamento(Departamentos dpto) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View formulario = inflater.inflate(R.layout.dialog_forms, null);
        TextView tvTitulo = formulario.findViewById(R.id.tvTitulo);
        EditText etNombre = formulario.findViewById(R.id.etNombre);
        Button btnGuardar = formulario.findViewById(R.id.btnGuardar);

        tvTitulo.setText("Editar Departamento");

        // Prellenar el campo con el nombre actual
        etNombre.setText(dpto.getNombre());

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(formulario)
                .create();

        btnGuardar.setOnClickListener(v -> {
            String nuevoNombre = etNombre.getText().toString().trim();

            if (nuevoNombre.isEmpty()) {
                etNombre.setError("Ingrese un nombre");
                return;
            }

            DepartamentosBD bdpto = new DepartamentosBD();
            boolean actualizado = bdpto.actualizarDepartamento(dpto.getId_departamentos(), nuevoNombre, dpto.getEstado());

            if (actualizado) {
                dpto.setNombre(nuevoNombre); // Actualiza el objeto actual
                adapter.notifyDataSetChanged(); // Refresca el RecyclerView
                Toasty.success(getContext(), "✅ Departamento actualizado correctamente", Toasty.LENGTH_SHORT, true).show();
            } else {
                Toasty.error(getContext(), "❌ Error al actualizar el departamento", Toasty.LENGTH_SHORT, true).show();
            }

            dialog.dismiss();
        });

        dialog.show();
    }

    private void mostrarDialogoActivarDesactivarDepartamento(Departamentos dpto) {
        String nuevoEstado = dpto.getEstado().equalsIgnoreCase("Activo") ? "Inactivo" : "Activo";

        new AlertDialog.Builder(getContext())
                .setTitle("Cambiar estado")
                .setMessage("¿Deseas cambiar el estado a \"" + nuevoEstado + "\"?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    DepartamentosBD bdpto = new DepartamentosBD();
                    boolean ok = bdpto.actualizarDepartamento(dpto.getId_departamentos(), dpto.getNombre(), nuevoEstado);

                    if (ok) {
                        dpto.setEstado(nuevoEstado);
                        adapter.notifyDataSetChanged();
                        Toasty.info(getContext(), "Estado cambiado a " + nuevoEstado, Toasty.LENGTH_SHORT, true).show();
                    } else {
                        Toasty.error(getContext(), "Error al cambiar estado", Toasty.LENGTH_SHORT, true).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void filtrarLista(String texto) {
        ArrayList<Departamentos> listaFiltrada = new ArrayList<>();
        for (Departamentos d : listaDepartamentos) {
            if (d.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                listaFiltrada.add(d);
            }
        }
        adapter.actualizarLista(listaFiltrada);
    }

    private void filtrarPorCantidad(int cantidad) {
        ArrayList<Departamentos> filtrados = new ArrayList<>();

        for (int i = 0; i < listaDepartamentos.size() && i < cantidad; i++) {
            filtrados.add(listaDepartamentos.get(i));
        }

        adapter.actualizarLista(filtrados);
    }

    private void aplicarFiltros() {
        ArrayList<Departamentos> filtrados = new ArrayList<>();

        for (Departamentos d : listaDepartamentos) {
            if (d.getNombre().toLowerCase().contains(textoBusqueda.toLowerCase())) {
                filtrados.add(d);
            }
        }

        // Aplicar límite por cantidad
        ArrayList<Departamentos> resultadoFinal = new ArrayList<>();
        for (int i = 0; i < Math.min(cantidadSeleccionada, filtrados.size()); i++) {
            resultadoFinal.add(filtrados.get(i));
        }

        adapter.actualizarLista(resultadoFinal);
    }



}
