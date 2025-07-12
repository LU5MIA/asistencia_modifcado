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
import com.appcali.pantalla_principal.entidades.Departamentos;

import java.util.ArrayList;

public class ListaDepartamentosAdapter extends RecyclerView.Adapter<ListaDepartamentosAdapter.DepartamentosViewHolder> {

    public interface OnDepartamentoClickListener {
        void onEditarClick(Departamentos departamento);
        void onActivarDesactivarClick(Departamentos departamento);
    }

    ArrayList<Departamentos> listadepartamentos;

    private OnDepartamentoClickListener listener;

    public void actualizarLista(ArrayList<Departamentos> nuevaLista) {
        this.listadepartamentos = nuevaLista;
        notifyDataSetChanged();
    }

    public void setOnDepartamentoClickListener(OnDepartamentoClickListener listener) {
        this.listener = listener;
    }

    public ListaDepartamentosAdapter(ArrayList<Departamentos> listadepartamentos) {
        this.listadepartamentos = listadepartamentos;
    }

    @NonNull
    @Override
    public ListaDepartamentosAdapter.DepartamentosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ListaDepartamentosAdapter.DepartamentosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaDepartamentosAdapter.DepartamentosViewHolder holder, int position) {

        Departamentos dpto = listadepartamentos.get(position);

        holder.tvid.setText(String.valueOf(dpto.getId_departamentos()));
        holder.tvdepartamento.setText(dpto.getNombre());
        holder.tvestado.setText(dpto.getEstado() != null ? dpto.getEstado() : "Sin estado");

        Context context = holder.itemView.getContext();
        String estado = dpto.getEstado() != null ? dpto.getEstado().toLowerCase() : "";

        switch (estado) {
            case "activo":
                holder.tvestado.setBackground(ContextCompat.getDrawable(context, R.drawable.estado_registrado));
                holder.btnactivacion.setImageResource(R.drawable.outline_close_24); // ❌
                holder.btnactivacion.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_circulo_rojo)); // 🔴 fondo rojo
                break;
            case "inactivo":
                holder.tvestado.setBackground(ContextCompat.getDrawable(context, R.drawable.estado_tarde));
                holder.btnactivacion.setImageResource(R.drawable.outline_check_24); // ✅
                holder.btnactivacion.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_circulo_verde)); // 🟢 fondo verde
                break;
            default:
                holder.tvestado.setBackgroundColor(Color.TRANSPARENT);
                break;
        }


        holder.btneditar.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditarClick(dpto);
            }
        });

        holder.btnactivacion.setOnClickListener(v -> {
            if (listener != null) {
                listener.onActivarDesactivarClick(dpto);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listadepartamentos.size();
    }

    public class DepartamentosViewHolder extends RecyclerView.ViewHolder {

        TextView tvid, tvdepartamento, tvestado;

        ImageButton btneditar, btnactivacion;
        public DepartamentosViewHolder(@NonNull View itemView) {
            super(itemView);
            tvid = itemView.findViewById(R.id.txt_col1);
            tvdepartamento = itemView.findViewById(R.id.txt_col2);
            tvestado = itemView.findViewById(R.id.txt_col3);
            btneditar = itemView.findViewById(R.id.btn_editar);
            btnactivacion = itemView.findViewById(R.id.btn_activacion);

        }
    }
}
