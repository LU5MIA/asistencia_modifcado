package com.appcali.pantalla_principal.Adaptadores;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.appcali.pantalla_principal.R;
import com.appcali.pantalla_principal.entidades.Asistencias;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class LIstaAsisUsuariosAdapter extends RecyclerView.Adapter<LIstaAsisUsuariosAdapter.AsistenciaViewHolder> {

    ArrayList<Asistencias> listaAsisUsuarios;

    public LIstaAsisUsuariosAdapter(ArrayList<Asistencias> listaAsisUsuarios) {
        this.listaAsisUsuarios = listaAsisUsuarios;
    }

    @NonNull
    @Override
    public AsistenciaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_asis_usuarios, parent, false);
        return new AsistenciaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AsistenciaViewHolder holder, int position) {
        Asistencias asistencia = listaAsisUsuarios.get(position);

        holder.vid.setText(String.valueOf(asistencia.getId_asistencias()));

        holder.vmarcacion.setText(asistencia.getMarcacion());

        holder.vtipo.setText(asistencia.getTipo() != null ? asistencia.getTipo() : "Sin tipo");
        holder.vestado.setText(asistencia.getEstado() != null ? asistencia.getEstado() : "Sin estado");

        Context context = holder.itemView.getContext();
        String estado = asistencia.getEstado() != null ? asistencia.getEstado().toLowerCase() : "";

        switch (estado) {
            case "registrado":
                holder.vestado.setBackground(ContextCompat.getDrawable(context, R.drawable.estado_registrado));
                break;
            case "justificacion":
                holder.vestado.setBackground(ContextCompat.getDrawable(context, R.drawable.estado_justificacion));
                break;
            case "tarde":
                holder.vestado.setBackground(ContextCompat.getDrawable(context, R.drawable.estado_tarde));
                break;
            default:
                holder.vestado.setBackgroundColor(Color.TRANSPARENT);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return listaAsisUsuarios.size();
    }

    public static class AsistenciaViewHolder extends RecyclerView.ViewHolder {
        TextView vid, vmarcacion, vtipo, vestado;

        public AsistenciaViewHolder(@NonNull View itemView) {
            super(itemView);
            vid = itemView.findViewById(R.id.txt_col1);
            vmarcacion = itemView.findViewById(R.id.txt_col2);
            vtipo = itemView.findViewById(R.id.txt_col3);
            vestado = itemView.findViewById(R.id.txt_col4);
        }
    }
}
