package com.appcali.pantalla_principal.ui.Perfil;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.appcali.pantalla_principal.BaseFragment;
import com.appcali.pantalla_principal.Connection.EmpleadosBD;
import com.appcali.pantalla_principal.Connection.UsuariosBD;
import com.appcali.pantalla_principal.R;
import com.appcali.pantalla_principal.entidades.Empleado;
import com.appcali.pantalla_principal.entidades.Usuarios;

public class PerfilFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private EditText txtusuario,txtcontra, txtnombres, txtApe_P, txtApe_M, txtcorreo;
    private Button btnActualizar;

    private boolean modoEdicion = false;
    private String originalUsuario, originalContra, originalNombres, originalApeP, originalApeM, originalCorreo;

    public PerfilFragment() {

    }


    public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
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


        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        txtusuario = view.findViewById(R.id.txtNombreUsuario);
        txtcontra = view.findViewById(R.id.txtcontraseña);
        txtnombres = view.findViewById(R.id.txtNombres);
        txtApe_P = view.findViewById(R.id.txtApe_P);
        txtApe_M = view.findViewById(R.id.txtApe_M);
        txtcorreo = view.findViewById(R.id.txtcorreo);
        btnActualizar = view.findViewById(R.id.btnEditar);

        // Desactivar campos al inicio
        txtusuario.setEnabled(false);
        txtcontra.setEnabled(false);
        txtnombres.setEnabled(false);
        txtApe_P.setEnabled(false);
        txtApe_M.setEnabled(false);
        txtcorreo.setEnabled(false);

        cargarDatosPerfil();

        btnActualizar.setOnClickListener(v -> {
            if (!modoEdicion) {
                // Guardar valores originales
                originalUsuario = txtusuario.getText().toString().trim();
                originalContra = txtcontra.getText().toString().trim();
                originalNombres = txtnombres.getText().toString().trim();
                originalApeP = txtApe_P.getText().toString().trim();
                originalApeM = txtApe_M.getText().toString().trim();
                originalCorreo = txtcorreo.getText().toString().trim();

                // Activar campos
                txtusuario.setEnabled(true);
                txtcontra.setEnabled(true);
                txtnombres.setEnabled(true);
                txtApe_P.setEnabled(true);
                txtApe_M.setEnabled(true);
                txtcorreo.setEnabled(true);

                // Cambiar texto e ícono
                btnActualizar.setText("Actualizar");
                btnActualizar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.outline_cached_24, 0, 0, 0);

                modoEdicion = true;
            }
            else {
                // Obtener los valores actuales
                String nuevoUsuario = txtusuario.getText().toString().trim();
                String nuevaContra = txtcontra.getText().toString().trim();
                String nuevoNombres = txtnombres.getText().toString().trim();
                String nuevoApeP = txtApe_P.getText().toString().trim();
                String nuevoApeM = txtApe_M.getText().toString().trim();
                String nuevoCorreo = txtcorreo.getText().toString().trim();

                // Verificar si hubo cambios
                boolean sinCambios =
                        nuevoUsuario.equals(originalUsuario) &&
                                nuevaContra.equals(originalContra) &&
                                nuevoNombres.equals(originalNombres) &&
                                nuevoApeP.equals(originalApeP) &&
                                nuevoApeM.equals(originalApeM) &&
                                nuevoCorreo.equals(originalCorreo);

                if (sinCambios) {
                    Toast.makeText(getContext(), "No se detectaron cambios", Toast.LENGTH_SHORT).show();

                    // Desactivar campos y volver a modo no edición
                    txtusuario.setEnabled(false);
                    txtcontra.setEnabled(false);
                    txtnombres.setEnabled(false);
                    txtApe_P.setEnabled(false);
                    txtApe_M.setEnabled(false);
                    txtcorreo.setEnabled(false);

                    btnActualizar.setText("Editar");
                    btnActualizar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_square_24, 0, 0, 0);

                    modoEdicion = false;
                    return; // salir sin actualizar
                }

                // Si hubo cambios, actualiza
                actualizarDatosUsuario_Empleado();

                // Desactivar campos
                txtusuario.setEnabled(false);
                txtcontra.setEnabled(false);
                txtnombres.setEnabled(false);
                txtApe_P.setEnabled(false);
                txtApe_M.setEnabled(false);
                txtcorreo.setEnabled(false);

                btnActualizar.setText("Editar");
                btnActualizar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_square_24, 0, 0, 0);

                modoEdicion = false;
            }

        });
        return view;
    }

    private void cargarDatosPerfil() {
        SharedPreferences preferences = requireContext().getSharedPreferences("sesion", Context.MODE_PRIVATE);
        String nombreUsuario = preferences.getString("nombreUsuario", null);

        if (nombreUsuario != null) {
            UsuariosBD usuariosBD = new UsuariosBD();
            Usuarios usuario = usuariosBD.obtenerDatosUsuario(nombreUsuario);

            if (usuario != null && usuario.getEmpleado() != null) {
                // Datos del usuario
                txtusuario.setText(usuario.getNombre_usuario());
                txtcontra.setText(usuario.getPassword());

                // Datos del empleado
                Empleado empleado = usuario.getEmpleado();
                txtnombres.setText(empleado.getNombres());
                txtApe_P.setText(empleado.getApe_p());
                txtApe_M.setText(empleado.getApe_m());
                txtcorreo.setText(empleado.getCorreo());
            }
        }
    }


    private void actualizarDatosUsuario_Empleado() {
        SharedPreferences preferences = requireContext().getSharedPreferences("sesion", Context.MODE_PRIVATE);
        int idEmpleado = preferences.getInt("idEmpleado", -1);

        EmpleadosBD empleadoBD = new EmpleadosBD();

        if (idEmpleado != -1) {
            Empleado empleado = new Empleado();
            empleado.setId_empleados(idEmpleado);
            empleado.setNombres(txtnombres.getText().toString().trim());
            empleado.setApe_p(txtApe_P.getText().toString().trim());
            empleado.setApe_m(txtApe_M.getText().toString().trim());
            empleado.setCorreo(txtcorreo.getText().toString().trim());

            boolean actualizado = empleadoBD.actualizarEmpleado(empleado);
            if (actualizado) {
                Toast.makeText(getContext(), "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Error al actualizar", Toast.LENGTH_SHORT).show();
            }
        }

        String nombreUsuarioActual = preferences.getString("nombreUsuario", null);

        if (nombreUsuarioActual != null) {
            String nuevoNombreUsuario = txtusuario.getText().toString().trim();
            String nuevaContraseña = txtcontra.getText().toString().trim();

            UsuariosBD usuariosBD = new UsuariosBD();
            boolean actualizadoUsuario = usuariosBD.actualizarUsuario(nombreUsuarioActual, nuevoNombreUsuario, nuevaContraseña);

            if (actualizadoUsuario) {
                Toast.makeText(getContext(), "Usuario actualizado correctamente", Toast.LENGTH_SHORT).show();

                // Actualizar SharedPreferences si cambió el nombre de usuario
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("nombreUsuario", nuevoNombreUsuario);
                editor.apply();
            } else {
                Toast.makeText(getContext(), "Error al actualizar usuario", Toast.LENGTH_SHORT).show();
            }
        }

    }


}