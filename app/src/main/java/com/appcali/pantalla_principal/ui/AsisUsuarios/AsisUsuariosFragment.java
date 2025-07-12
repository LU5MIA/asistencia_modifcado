package com.appcali.pantalla_principal.ui.AsisUsuarios;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appcali.pantalla_principal.Adaptadores.LIstaAsisUsuariosAdapter;
import com.appcali.pantalla_principal.BaseFragment;
import com.appcali.pantalla_principal.Connection.AsistenciasBD;
import com.appcali.pantalla_principal.R;
import com.appcali.pantalla_principal.databinding.FragmentAsisUsuariosBinding;
import com.appcali.pantalla_principal.entidades.Asistencias;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AsisUsuariosFragment extends BaseFragment {

    private EditText etfecha;
    private Spinner spCantidad;
    private ImageButton ibtn_filtrar;
    private RecyclerView recyclerView;
    private TextView txtSinAsistencias;
    private ArrayList<Asistencias> listaAsisUsuarios;
    private LIstaAsisUsuariosAdapter adapter;
    private ArrayList<Asistencias> listaOriginal;

    private FragmentAsisUsuariosBinding binding;
    private final Calendar calendar = Calendar.getInstance();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        AsisUsuariosViewModel notificationsViewModel =
                new ViewModelProvider(this).get(AsisUsuariosViewModel.class);

        binding = FragmentAsisUsuariosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        spCantidad = root.findViewById(R.id.spCantidad);
        etfecha = root.findViewById(R.id.txtfecha);
        ibtn_filtrar = root.findViewById(R.id.ibtn_filtrador);
        recyclerView = root.findViewById(R.id.rv_tabla_mis_asistencias);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        etfecha.setFocusable(false);


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


        etfecha.setOnClickListener(v -> mostrarCalendario());
        etfecha.setOnClickListener(v -> {
            if (etfecha.getText().toString().isEmpty()) {
                mostrarCalendario();
            } else {
                limpiarFecha(); // Toca el ícono de borrar
            }
        });
        etfecha.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (etfecha.getRight() - etfecha.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    if (!etfecha.getText().toString().isEmpty()) {
                        limpiarFecha();
                    } else {
                        mostrarCalendario();
                    }
                    return true;
                }
            }
            return false;
        });


        cargarAsistenciasUsuarios();

        final TextView textView = binding.textNotifications;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;

    }

    private void cargarAsistenciasUsuarios() {
        SharedPreferences preferences = requireContext().getSharedPreferences("sesion", Context.MODE_PRIVATE);
        int idEmpleado = preferences.getInt("idEmpleado", -1);

        if (idEmpleado != -1) {
            AsistenciasBD asistenciasBD = new AsistenciasBD();
            listaAsisUsuarios = asistenciasBD.mostrarAsistenciasPorEmpleado(idEmpleado);
            listaOriginal = new ArrayList<>(listaAsisUsuarios); // Copia original
            adapter = new LIstaAsisUsuariosAdapter(listaAsisUsuarios);
            recyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(getContext(), "No se pudo obtener el ID del empleado", Toast.LENGTH_SHORT).show();
        }
    }



    private void mostrarCalendario() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(requireContext(),
                (DatePicker view, int selectedYear, int selectedMonth, int selectedDay) -> {
                    calendar.set(Calendar.YEAR, selectedYear);
                    calendar.set(Calendar.MONTH, selectedMonth);
                    calendar.set(Calendar.DAY_OF_MONTH, selectedDay);
                    actualizarCampoFechaYFiltrar();
                },
                year, month, day);
        dialog.show();
    }

    private void filtrarPorFecha(String fechaSeleccionada) {
        List<Asistencias> filtradas = new ArrayList<>();

        for (Asistencias asistencia : listaOriginal) {
            String marcacion = asistencia.getMarcacion();
            if (marcacion != null && !marcacion.trim().isEmpty()) {
                String fechaMarcacion = marcacion.trim().length() >= 10 ? marcacion.trim().substring(0, 10) : "";
                if (fechaMarcacion.equals(fechaSeleccionada)) {
                    filtradas.add(asistencia);
                }
            }
        }

        listaAsisUsuarios.clear();
        listaAsisUsuarios.addAll(filtradas);
        adapter.notifyDataSetChanged();

        if (filtradas.isEmpty()) {
            Toast.makeText(getContext(), "No hay registros para esa fecha", Toast.LENGTH_SHORT).show();
        }
    }

    private void actualizarCampoFechaYFiltrar() {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String fechaSeleccionada = formato.format(calendar.getTime());
        etfecha.setText(fechaSeleccionada);
        etfecha.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.outline_delete_24, 0); // Cambiar ícono
        filtrarPorFecha(fechaSeleccionada);
    }

    private void limpiarFecha() {
        etfecha.setText("");
        etfecha.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.outline_calendar_today_24, 0);
        listaAsisUsuarios.clear();
        listaAsisUsuarios.addAll(listaOriginal);
        adapter.notifyDataSetChanged();
    }

    private void mostrarCantidadFiltrada(int cantidad) {
        listaAsisUsuarios.clear();

        for (int i = 0; i < cantidad && i < listaOriginal.size(); i++) {
            listaAsisUsuarios.add(listaOriginal.get(i));
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
