package com.appcali.pantalla_principal.Adaptadores;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.appcali.pantalla_principal.R;
import com.appcali.pantalla_principal.entidades.Roles;

import java.util.ArrayList;

public class ListaRolesAdapter extends RecyclerView.Adapter<ListaRolesAdapter.RolesViewHolder> {

    public interface OnRolClickListener {
        void onEditarClick(Roles rol);
    }

    ArrayList<Roles> listaroles;
    private ListaRolesAdapter.OnRolClickListener listener;

    public void setOnRolesClickListener(ListaRolesAdapter.OnRolClickListener listener) {
        this.listener = listener;
    }

    public ListaRolesAdapter(ArrayList<Roles> listaroles) {
        this.listaroles = listaroles;
    }


    @NonNull
    @Override
    public ListaRolesAdapter.RolesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_rol, parent, false);
        return new ListaRolesAdapter.RolesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaRolesAdapter.RolesViewHolder holder, int position) {

        Roles rol = listaroles.get(position);

        holder.tvid.setText(String.valueOf(rol.getId_roles()));
        holder.tvrol.setText(rol.getNombre());
        holder.tvestado.setText(rol.getEstado() != null ? rol.getEstado() : "Sin estado");

        String estado = rol.getEstado() != null ? rol.getEstado().toLowerCase() : "";

        Context context = holder.itemView.getContext();

        switch (estado) {
            case "activo":
                holder.tvestado.setBackground(ContextCompat.getDrawable(context, R.drawable.estado_registrado));
                break;
            case "inactivo":
                holder.tvestado.setBackground(ContextCompat.getDrawable(context, R.drawable.estado_tarde));
                break;
            default:
                holder.tvestado.setBackgroundColor(Color.TRANSPARENT);
                break;
        }

        // Eventos
        holder.btneditar.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditarClick(rol);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaroles.size();
    }

    public class RolesViewHolder extends RecyclerView.ViewHolder {
        TextView tvid, tvrol, tvestado;
        ImageButton btneditar;
        public RolesViewHolder(@NonNull View itemView) {
            super(itemView);
            tvid = itemView.findViewById(R.id.txt_col1);
            tvrol = itemView.findViewById(R.id.txt_col2);
            tvestado = itemView.findViewById(R.id.txt_col3);
            btneditar = itemView.findViewById(R.id.btn_editar);
        }
    }
}
