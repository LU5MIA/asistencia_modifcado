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

import java.util.ArrayList;

public class ListaAsisAdministradorAdapter extends RecyclerView.Adapter<ListaAsisAdministradorAdapter.AsistenciaViewHolder> {

    ArrayList<Asistencias> listaAsistencias;

    public ListaAsisAdministradorAdapter(ArrayList<Asistencias> listaAsistencias){
        this.listaAsistencias = listaAsistencias;
    }

    @NonNull
    @Override
    public AsistenciaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_asis_admin, null, false);
        return new AsistenciaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AsistenciaViewHolder holder, int position) {
        Asistencias asistencia = listaAsistencias.get(position);

        holder.vid.setText(String.valueOf(asistencia.getId_asistencias()));
        holder.vempleado.setText(asistencia.getNombre_completo());
        holder.vtipo.setText(asistencia.getTipo());
        holder.vestado.setText(asistencia.getEstado());

        Context context = holder.itemView.getContext();
        String estado = asistencia.getEstado().toLowerCase();

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
        return listaAsistencias.size();
    }

    public class AsistenciaViewHolder extends RecyclerView.ViewHolder {

        TextView vid, vempleado, vtipo, vestado;

        public AsistenciaViewHolder(@NonNull View itemView) {
            super(itemView);

            vid = itemView.findViewById(R.id.txt_col1);
            vempleado = itemView.findViewById(R.id.txt_col2);
            vtipo = itemView.findViewById(R.id.txt_col3);
            vestado = itemView.findViewById(R.id.txt_col4);
        }
    }
}
