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
import com.appcali.pantalla_principal.entidades.Cargos;

import java.util.ArrayList;

public class ListaCargosAdapter extends RecyclerView.Adapter<ListaCargosAdapter.CargosViewHolder> {

    public interface OnCargoClickListener {
        void onEditarClick(Cargos cargo); // ✅ CORREGIDO
        void onActivarDesactivarClick(Cargos cargo); // ✅ CORREGIDO
    }

    ArrayList<Cargos> listacargos;
    private OnCargoClickListener listener;

    public void actualizarLista(ArrayList<Cargos> nuevaLista) {
        this.listacargos = nuevaLista;
        notifyDataSetChanged();
    }

    public void setOnCargoClickListener(OnCargoClickListener listener) { // ✅ CORREGIDO
        this.listener = listener;
    }

    public ListaCargosAdapter(ArrayList<Cargos> listacargos) {
        this.listacargos = listacargos;
    }

    @NonNull
    @Override
    public CargosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false); // ✅ CAMBIAR A UN LAYOUT MÁS LÓGICO
        return new CargosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CargosViewHolder holder, int position) {
        Cargos cargo = listacargos.get(position);

        holder.tvid.setText(String.valueOf(cargo.getId_cargos()));
        holder.tvcargo.setText(cargo.getNombre());
        holder.tvestado.setText(cargo.getEstado() != null ? cargo.getEstado() : "Sin estado");

        Context context = holder.itemView.getContext();
        String estado = cargo.getEstado() != null ? cargo.getEstado().toLowerCase() : "";

        switch (estado) {
            case "activo":
                holder.tvestado.setBackground(ContextCompat.getDrawable(context, R.drawable.estado_registrado));
                holder.btnactivacion.setImageResource(R.drawable.outline_close_24);
                holder.btnactivacion.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_circulo_rojo));
                break;
            case "inactivo":
                holder.tvestado.setBackground(ContextCompat.getDrawable(context, R.drawable.estado_tarde));
                holder.btnactivacion.setImageResource(R.drawable.outline_check_24);
                holder.btnactivacion.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_circulo_verde));
                break;
            default:
                holder.tvestado.setBackgroundColor(Color.TRANSPARENT);
                break;
        }

        // Eventos
        holder.btneditar.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditarClick(cargo);
            }
        });

        holder.btnactivacion.setOnClickListener(v -> {
            if (listener != null) {
                listener.onActivarDesactivarClick(cargo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listacargos.size();
    }

    public class CargosViewHolder extends RecyclerView.ViewHolder {
        TextView tvid, tvcargo, tvestado;
        ImageButton btneditar, btnactivacion;

        public CargosViewHolder(@NonNull View itemView) {
            super(itemView);
            tvid = itemView.findViewById(R.id.txt_col1);
            tvcargo = itemView.findViewById(R.id.txt_col2);
            tvestado = itemView.findViewById(R.id.txt_col3);
            btneditar = itemView.findViewById(R.id.btn_editar);
            btnactivacion = itemView.findViewById(R.id.btn_activacion);
        }
    }
}
