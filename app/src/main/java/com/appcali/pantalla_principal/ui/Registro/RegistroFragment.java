package com.appcali.pantalla_principal.ui.Registro;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.appcali.pantalla_principal.BaseFragment;
import com.appcali.pantalla_principal.Connection.AsistenciasBD;
import com.appcali.pantalla_principal.R;
import com.appcali.pantalla_principal.databinding.FragmentRegistroBinding;
import com.appcali.pantalla_principal.entidades.Asistencias;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RegistroFragment extends BaseFragment {

    private FragmentRegistroBinding binding;
    private static final double LAT_PERMITIDA = -8.084261;
    private static final double LON_PERMITIDA = -79.041214;
    private static final float RANGO_PERMITIDO_METROS = 70;
    private static final int PERMISSION_REQUEST_CODE = 1;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private final Handler handler = new Handler(Looper.getMainLooper());

    private boolean ubicacionObtenida = false;
    private boolean sistemaCerrado = false;

    private int contadorUbicaciones = 0;

    private void inicializarEstadoDelSistema() {
        try {
            SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            Date horaActual = formatoHora.parse(new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date()));
            Date horaApertura = formatoHora.parse("07:00:00");
            Date horaCierre = formatoHora.parse("16:00:00");

            if (horaActual.before(horaApertura) || horaActual.after(horaCierre)) {
                sistemaCerrado = true;
                binding.lblRegistro.setText("⛔ Sistema cerrado");
                binding.lblDetalleCierre.setText("* El sistema se abrirá a las 07:00 am");
                binding.lblDetalleCierre.setVisibility(View.VISIBLE);
                binding.btnMarcar.setEnabled(false);
                binding.btnActualizar.setEnabled(false);

                // 🔒 También cancelamos actualizaciones si estaban activas
                if (fusedLocationClient != null && locationCallback != null) {
                    fusedLocationClient.removeLocationUpdates(locationCallback);
                }
            } else {
                sistemaCerrado = false;
                binding.lblRegistro.setText("✅ Sistema activo");
                binding.lblDetalleCierre.setVisibility(View.GONE);
                binding.btnMarcar.setEnabled(true);
                binding.btnActualizar.setEnabled(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Runnable actualizarHoraRunnable = new Runnable() {
        @Override
        public void run() {
            String fechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            binding.lblhora.setText(fechaHora);

            SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

            try {
                Date horaActual = formatoHora.parse(fechaHora.split(" ")[1]); // Hora actual HH:mm:ss
                Date horaCierre = formatoHora.parse("16:00:00");
                Date horaApertura = formatoHora.parse("07:00:00");

                if (!sistemaCerrado && horaActual.after(horaCierre)) {
                    // Cerrar sistema
                    sistemaCerrado = true;

                    binding.lblRegistro.setText("⛔ Sistema cerrado");
                    binding.lblDetalleCierre.setText("* El sistema se abrirá mañana a las 07:00 am");
                    binding.lblDetalleCierre.setVisibility(View.VISIBLE);

                    binding.btnMarcar.setEnabled(false);
                    binding.btnActualizar.setEnabled(false);

                    if (fusedLocationClient != null && locationCallback != null) {
                        fusedLocationClient.removeLocationUpdates(locationCallback);
                    }

                    ubicacionObtenida = true;
                }

                // Reabrir sistema a las 07:00:00
                if (sistemaCerrado && horaActual.after(horaApertura) && horaActual.before(horaCierre)) {
                    sistemaCerrado = false;

                    binding.lblRegistro.setText("✅ Sistema activo");
                    binding.lblDetalleCierre.setVisibility(View.GONE);

                    binding.btnMarcar.setEnabled(true);
                    binding.btnActualizar.setEnabled(true);

                    verificarEstadoDeMarcacion();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            handler.postDelayed(this, 1000);
        }
    };


    private void verificarEstadoDeMarcacion() {
        SharedPreferences preferences = requireContext().getSharedPreferences("sesion", Context.MODE_PRIVATE);
        int idEmpleado = preferences.getInt("idEmpleado", -1);

        if (idEmpleado != -1) {
            String fechaHoy = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            AsistenciasBD bd = new AsistenciasBD();
            boolean yaMarcoEntrada = bd.existeAsistenciaDelDia(idEmpleado, "entrada", fechaHoy);
            boolean yaMarcoSalida  = bd.existeAsistenciaDelDia(idEmpleado, "salida", fechaHoy);

            if (!sistemaCerrado) {
                if (yaMarcoEntrada && yaMarcoSalida) {
                    binding.lblRegistro.setText("Asistencias Completas");
                    binding.btnMarcar.setEnabled(false);
                    binding.btnActualizar.setEnabled(false);

                    // Detener ubicación si ya marcó todo
                    if (fusedLocationClient != null && locationCallback != null) {
                        fusedLocationClient.removeLocationUpdates(locationCallback);
                    }

                } else if (yaMarcoEntrada) {
                    binding.lblRegistro.setText("Registro de Asistencia de Salida");
                } else {
                    binding.lblRegistro.setText("Registro de Asistencia de Entrada");
                }
            }
        } else {
            binding.lblRegistro.setText("⚠️ Usuario no identificado");
            binding.btnMarcar.setEnabled(false);
        }
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegistroBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        inicializarEstadoDelSistema();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());

        binding.btnMarcar.setEnabled(false);
        iniciarActualizacionHora();
        // 🛡️ SOLO si el sistema está abierto, iniciamos la ubicación
        if (!sistemaCerrado) {
            solicitarPermisosYObtenerUbicacion();
        }

        binding.btnActualizar.setOnClickListener(v -> {
            ubicacionObtenida = false;
            contadorUbicaciones = 0;

            // Limpia el texto de estado
            binding.lblconsideracion.setText("En espera..");
            binding.lblconsideracion.setTextColor(getResources().getColor(android.R.color.white));

            // Desactiva el botón hasta que vuelva a validar
            binding.btnMarcar.setEnabled(false);

            iniciarActualizacionUbicacion();
        });


        binding.btnMarcar.setOnClickListener(v -> {
            SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            SimpleDateFormat formatoCompleto = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

            try {
                // Corrección: obtener hora actual solo como hora
                String horaStr = formatoHora.format(new Date());
                Date horaActual = formatoHora.parse(horaStr);

                Date horaActivacion = formatoHora.parse("07:00:00");
                Date horaEntrada    = formatoHora.parse("08:00:00");
                Date horaCierre     = formatoHora.parse("16:00:00");

                String estado;

                if (horaActual.before(horaActivacion)) {
                    Toast.makeText(getContext(), "⏳ El sistema aún no está activo", Toast.LENGTH_LONG).show();
                    return;
                } else if (horaActual.before(horaEntrada)) {
                    estado = "registrado";
                } else if (horaActual.before(horaCierre)) {
                    estado = "tarde";
                } else {
                    Toast.makeText(getContext(), "❌ El sistema de marcación ha cerrado", Toast.LENGTH_LONG).show();
                    return;
                }

                // Obtener tipo desde lblRegistro
                String textoTipo = binding.lblRegistro.getText().toString().toLowerCase();
                String tipo;
                if (textoTipo.contains("entrada")) {
                    tipo = "entrada";
                } else if (textoTipo.contains("salida")) {
                    tipo = "salida";
                } else {
                    Toast.makeText(getContext(), "❌ No se pudo determinar si es entrada o salida", Toast.LENGTH_LONG).show();
                    return;
                }

                // Obtener ID del empleado desde SharedPreferences
                SharedPreferences preferences = requireContext().getSharedPreferences("sesion", Context.MODE_PRIVATE);
                int idEmpleado = preferences.getInt("idEmpleado", -1);


                if (idEmpleado == -1) {
                    Toast.makeText(getContext(), "❌ No se encontró el ID del empleado en sesión", Toast.LENGTH_LONG).show();
                    return;
                }

                // Crear objeto Asistencias
                Asistencias asistencia = new Asistencias();
                asistencia.setMarcacion(formatoCompleto.format(new Date())); // Fecha y hora completas
                asistencia.setTipo(tipo);
                asistencia.setEstado(estado);
                asistencia.setId_empleado(idEmpleado);

                // Insertar en la base de datos
                boolean insertado = new AsistenciasBD().insertarAsistencia(asistencia);
                if (insertado) {
                    Toast.makeText(getContext(), "✅ Asistencia registrada: " + estado, Toast.LENGTH_LONG).show();
                    binding.btnActualizar.setEnabled(false);
                    binding.btnMarcar.setEnabled(false);
                } else {
                    Toast.makeText(getContext(), "❌ Error al registrar la asistencia", Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "⚠️ Error en la validación de hora", Toast.LENGTH_SHORT).show();
            }
        });


        verificarEstadoDeMarcacion();
        return root;
    }

    private void iniciarActualizacionHora() {
        handler.post(actualizarHoraRunnable);
    }

    private void solicitarPermisosYObtenerUbicacion() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        } else {
            iniciarActualizacionUbicacion();
        }
    }

    private void iniciarActualizacionUbicacion() {
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .setInterval(5000)
                .setFastestInterval(2000);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (ubicacionObtenida) return;

                Location location = locationResult.getLastLocation();
                if (location != null) {
                    contadorUbicaciones++;

                    if (location.getAccuracy() > 25) {
                        binding.lblubicacion.setText(String.format(Locale.getDefault(),
                                "Esperando mejor precisión... %.2f m", location.getAccuracy()));
                        return;
                    }

                    if (contadorUbicaciones < 3) {
                        binding.lblubicacion.setText("Esperando señal precisa del GPS...");
                        return;
                    }

                    double latitud = location.getLatitude();
                    double longitud = location.getLongitude();

                    float[] resultados = new float[1];
                    Location.distanceBetween(latitud, longitud, LAT_PERMITIDA, LON_PERMITIDA, resultados);
                    float distancia = resultados[0];

                    boolean dentroDeRango = distancia <= RANGO_PERMITIDO_METROS;

                    // Mostrar información detallada
                    binding.lblubicacion.setText(String.format(Locale.getDefault(),
                            "Distancia: %.2f m\nLatitud: %.6f\nLongitud: %.6f",
                            distancia, latitud, longitud));

                    // Mostrar estado general en mayúsculas
                    if (dentroDeRango) {
                        binding.lblconsideracion.setText("CORRECTO");
                        binding.lblconsideracion.setTextColor(getResources().getColor(android.R.color.white));
                        binding.btnMarcar.setEnabled(true);
                    } else {
                        binding.lblconsideracion.setText("INCORRECTO");
                        binding.lblconsideracion.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                        binding.btnMarcar.setEnabled(false);
                    }

                    ubicacionObtenida = true;
                    fusedLocationClient.removeLocationUpdates(locationCallback);
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(actualizarHoraRunnable);
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
        binding = null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                iniciarActualizacionUbicacion();
            } else {
                binding.lblubicacion.setText("Permiso de ubicación denegado ❌");
                binding.lblconsideracion.setText("DENEGADO");
                binding.lblconsideracion.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            }
        }
    }
}
