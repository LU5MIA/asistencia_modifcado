package com.appcali.pantalla_principal.ui.Empleados;

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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.appcali.pantalla_principal.Adaptadores.ListaEmpleadosAdapter;
import com.appcali.pantalla_principal.BaseFragment;
import com.appcali.pantalla_principal.Connection.CargosBD;
import com.appcali.pantalla_principal.Connection.DepartamentosBD;
import com.appcali.pantalla_principal.Connection.EmpleadosBD;
import com.appcali.pantalla_principal.R;
import com.appcali.pantalla_principal.entidades.Cargos;
import com.appcali.pantalla_principal.entidades.Departamentos;
import com.appcali.pantalla_principal.entidades.Empleado;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class EmpleadosFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private ArrayList<Empleado> listaEmpleados;
    private ListaEmpleadosAdapter adapter;

    private String textoBusqueda = "";
    private int cantidadSeleccionada = 10; // Valor por defecto


    public EmpleadosFragment() {
    }

    public static EmpleadosFragment newInstance(String param1, String param2) {
        EmpleadosFragment fragment = new EmpleadosFragment();
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
        View view = inflater.inflate(R.layout.fragment_empleados, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.rv_empleados);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Obtener datos y configurar el adapter
        EmpleadosBD empleadosBD = new EmpleadosBD();
        listaEmpleados = empleadosBD.mostrarDatosEmpleado();

        adapter = new ListaEmpleadosAdapter(listaEmpleados);
        recyclerView.setAdapter(adapter);

        Spinner spCantidad = view.findViewById(R.id.spCantidad);
        ImageButton btnFiltrador = view.findViewById(R.id.ibtn_filtrador);

        // Cuando el botón se presione, abre el Spinner como si se tocara
        btnFiltrador.setOnClickListener(v -> spCantidad.performClick());

        spCantidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String opcionSeleccionada = parent.getItemAtPosition(position).toString();
                cantidadSeleccionada = Integer.parseInt(opcionSeleccionada);
                aplicarFiltrosEmpleados();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                cantidadSeleccionada = listaEmpleados.size();
                aplicarFiltrosEmpleados();
            }
        });

        EditText txtBuscar = view.findViewById(R.id.txtbuscarem);

        txtBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textoBusqueda = s.toString();
                aplicarFiltrosEmpleados();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });



        adapter.setOnEmpleadoClickListener(new ListaEmpleadosAdapter.OnEmpleadoClickListener() {
            @Override
            public void onEditarClick(Empleado empleado) {
                mostrarDialogoEditarEmpleado(empleado);
            }

            @Override
            public void onEliminarClick(Empleado empleado) {
                mostrarDialogoConfirmacionEliminar(empleado); // 👈 Ahora creamos este
            }
        });

        // Botón agregar
        ImageButton btnAgregar = view.findViewById(R.id.btn_agregar);
        btnAgregar.setOnClickListener(v -> mostrarDialogoAgregarEmpleado());


        return view;
    }

    private void mostrarDialogoAgregarEmpleado() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View formulario = inflater.inflate(R.layout.dialog_form_empleado, null);
        EditText etDNI = formulario.findViewById(R.id.etdni);
        EditText etNombres = formulario.findViewById(R.id.etNombre);
        EditText etApeP = formulario.findViewById(R.id.etApe_pater);
        EditText etApeM = formulario.findViewById(R.id.etApe_Mater);
        EditText etCorreo = formulario.findViewById(R.id.etCorreo);
        AutoCompleteTextView autoDepartamento = formulario.findViewById(R.id.autoDepartamento);
        AutoCompleteTextView autoCargo = formulario.findViewById(R.id.autoCargo);
        Button btnGuardar = formulario.findViewById(R.id.btnGuardar);

        ArrayList<Departamentos> departamentos = configurarAutoCompleteDepartamentos(autoDepartamento);
        ArrayList<Cargos> cargos = configurarAutoCompleteCargos(autoCargo);

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(formulario)
                .create();

        btnGuardar.setOnClickListener(v -> {
            String dni = etDNI.getText().toString().trim();
            String nombres = etNombres.getText().toString().trim();
            String apeP = etApeP.getText().toString().trim();
            String apeM = etApeM.getText().toString().trim();
            String correo = etCorreo.getText().toString().trim();
            String nombreDptoSeleccionado = autoDepartamento.getText().toString().trim();
            String nombreCargoSeleccionado = autoCargo.getText().toString().trim();

            // Validar departamento
            Departamentos dptoSeleccionado = null;
            for (Departamentos d : departamentos) {
                if (d.getNombre().equals(nombreDptoSeleccionado)) {
                    dptoSeleccionado = d;
                    break;
                }
            }

            if (dptoSeleccionado == null) {
                Toast.makeText(getContext(), "Selecciona un departamento válido", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validar cargo
            Cargos cargoSeleccionado = null;
            for (Cargos c : cargos) {
                if (c.getNombre().equals(nombreCargoSeleccionado)) {
                    cargoSeleccionado = c;
                    break;
                }
            }

            if (cargoSeleccionado == null) {
                Toast.makeText(getContext(), "Selecciona un cargo válido", Toast.LENGTH_SHORT).show();
                return;
            }

            // Crear nuevo empleado
            Empleado nuevo = new Empleado();
            nuevo.setDni(dni);
            nuevo.setNombres(nombres);
            nuevo.setApe_p(apeP);
            nuevo.setApe_m(apeM);
            nuevo.setCorreo(correo);
            nuevo.setDpto(dptoSeleccionado);
            nuevo.setCargo(cargoSeleccionado); // asignar cargo

            EmpleadosBD bd = new EmpleadosBD();
            int nuevoId = bd.insertarEmpleado(nuevo);
            if (nuevoId > 0) {
                Toasty.success(getContext(), "✅ Empleado agregado correctamente", Toasty.LENGTH_LONG, true).show();
                nuevo.setId_empleados(nuevoId);
                listaEmpleados.add(nuevo);
                adapter.notifyItemInserted(listaEmpleados.size() - 1);
            }
            dialog.dismiss();
        });

        dialog.show();
    }


    private void mostrarDialogoEditarEmpleado(Empleado empleado) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View formulario = inflater.inflate(R.layout.dialog_form_empleado, null);
        TextView tvTitulo = formulario.findViewById(R.id.tvTitulo);
        EditText etDNI = formulario.findViewById(R.id.etdni);
        EditText etNombres = formulario.findViewById(R.id.etNombre);
        EditText etApeP = formulario.findViewById(R.id.etApe_pater);
        EditText etApeM = formulario.findViewById(R.id.etApe_Mater);
        EditText etCorreo = formulario.findViewById(R.id.etCorreo);
        AutoCompleteTextView autoDepartamento = formulario.findViewById(R.id.autoDepartamento);
        AutoCompleteTextView autoCargo = formulario.findViewById(R.id.autoCargo);
        Button btnGuardar = formulario.findViewById(R.id.btnGuardar);

        tvTitulo.setText("Editar Empleado");
        etDNI.setText(empleado.getDni());
        etNombres.setText(empleado.getNombres());
        etApeP.setText(empleado.getApe_p());
        etApeM.setText(empleado.getApe_m());
        etCorreo.setText(empleado.getCorreo());

        ArrayList<Departamentos> departamentos = configurarAutoCompleteDepartamentos(autoDepartamento);
        ArrayList<Cargos> cargos = configurarAutoCompleteCargos(autoCargo);

        autoDepartamento.setText(empleado.getDpto().getNombre(), false);
        autoCargo.setText(empleado.getCargo().getNombre(), false);

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(formulario)
                .create();

        btnGuardar.setOnClickListener(v -> {
            String dni = etDNI.getText().toString().trim();
            String nombres = etNombres.getText().toString().trim();
            String apeP = etApeP.getText().toString().trim();
            String apeM = etApeM.getText().toString().trim();
            String correo = etCorreo.getText().toString().trim();
            String nombreDptoSeleccionado = autoDepartamento.getText().toString().trim();
            String nombreCargoSeleccionado = autoCargo.getText().toString().trim();

            // Validar departamento
            Departamentos dptoSeleccionado = null;
            for (Departamentos d : departamentos) {
                if (d.getNombre().equals(nombreDptoSeleccionado)) {
                    dptoSeleccionado = d;
                    break;
                }
            }

            if (dptoSeleccionado == null) {
                Toast.makeText(getContext(), "Selecciona un departamento válido", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validar cargo
            Cargos cargoSeleccionado = null;
            for (Cargos c : cargos) {
                if (c.getNombre().equals(nombreCargoSeleccionado)) {
                    cargoSeleccionado = c;
                    break;
                }
            }

            if (cargoSeleccionado == null) {
                Toast.makeText(getContext(), "Selecciona un cargo válido", Toast.LENGTH_SHORT).show();
                return;
            }

            empleado.setDni(dni);
            empleado.setNombres(nombres);
            empleado.setApe_p(apeP);
            empleado.setApe_m(apeM);
            empleado.setCorreo(correo);
            empleado.setDpto(dptoSeleccionado);
            empleado.setCargo(cargoSeleccionado);

            EmpleadosBD bd = new EmpleadosBD();
            boolean actualizado = bd.actualizarEmpleado(empleado);

            if (actualizado) {
                adapter.notifyDataSetChanged();
                Toasty.success(getContext(), "Empleado actualizado", Toast.LENGTH_SHORT, true).show();
            } else {
                Toasty.error(getContext(), "Error al actualizar", Toast.LENGTH_SHORT, true).show();
            }

            dialog.dismiss();
        });

        dialog.show();
    }


    private void mostrarDialogoConfirmacionEliminar(Empleado empleado) {
        new AlertDialog.Builder(getContext())
                .setTitle("Eliminar Empleado")
                .setMessage("¿Estás seguro de que deseas eliminar a " + empleado.getNombreCompleto() + "?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    EmpleadosBD bd = new EmpleadosBD();
                    boolean eliminado = bd.eliminarEmpleado(empleado.getId_empleados());
                    if (eliminado) {
                        listaEmpleados.remove(empleado);
                        adapter.notifyDataSetChanged();
                        Toasty.success(getContext(), "Empleado eliminado", Toast.LENGTH_SHORT, true).show();
                    } else {
                        Toasty.error(getContext(), "Error al eliminar", Toast.LENGTH_SHORT, true).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private ArrayList<Departamentos> configurarAutoCompleteDepartamentos(AutoCompleteTextView autoDepartamento) {
        DepartamentosBD bd = new DepartamentosBD();

        // Obtener solo los departamentos activos directamente desde la BD
        ArrayList<Departamentos> activos = bd.obtenerDepartamentosActivos();

        // Crear el adaptador con los activos
        ArrayAdapter<Departamentos> adapterDeptos = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_dropdown_item_1line,
                activos
        );

        autoDepartamento.setAdapter(adapterDeptos);
        autoDepartamento.setOnClickListener(v -> autoDepartamento.showDropDown());

        return activos; // Por si necesitas esta lista luego
    }

    private ArrayList<Cargos> configurarAutoCompleteCargos(AutoCompleteTextView autoCargo) {
        CargosBD bd = new CargosBD();

        // Obtener solo los cargos activos directamente desde la BD
        ArrayList<Cargos> activos = bd.obtenerCargosActivos();

        // Crear el adaptador con los cargos activos
        ArrayAdapter<Cargos> adapterCargos = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_dropdown_item_1line,
                activos
        );

        autoCargo.setAdapter(adapterCargos);
        autoCargo.setOnClickListener(v -> autoCargo.showDropDown());

        return activos; // Por si necesitas esta lista luego
    }

    private void aplicarFiltrosEmpleados() {
        ArrayList<Empleado> filtrados = new ArrayList<>();

        for (Empleado emp : listaEmpleados) {
            if (emp.getNombreCompleto().toLowerCase().contains(textoBusqueda.toLowerCase())) {
                filtrados.add(emp);
            }
        }

        // Aplica el límite por cantidad
        ArrayList<Empleado> resultadoFinal = new ArrayList<>();
        for (int i = 0; i < Math.min(cantidadSeleccionada, filtrados.size()); i++) {
            resultadoFinal.add(filtrados.get(i));
        }

        adapter.actualizarLista(resultadoFinal);
    }



}