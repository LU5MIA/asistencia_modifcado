package com.appcali.pantalla_principal.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appcali.pantalla_principal.R;
import com.appcali.pantalla_principal.entidades.Asistencias;
import com.appcali.pantalla_principal.entidades.Empleado;

import java.util.ArrayList;

public class ListaEmpleadosAdapter extends RecyclerView.Adapter<ListaEmpleadosAdapter.EmpleadosViewHolder> {

    public interface OnEmpleadoClickListener {
        void onEditarClick(Empleado empleado);
        void onEliminarClick(Empleado empleado);
    }

    ArrayList<Empleado> listaempleados;
    private OnEmpleadoClickListener listener;

    public void actualizarLista(ArrayList<Empleado> nuevaLista) {
        this.listaempleados = nuevaLista;
        notifyDataSetChanged();
    }


    public void setOnEmpleadoClickListener(OnEmpleadoClickListener listener) {
        this.listener = listener;
    }

    public ListaEmpleadosAdapter(ArrayList<Empleado> listaempleados) {
        this.listaempleados = listaempleados;
    }

    @NonNull
    @Override
    public EmpleadosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_empleados, parent, false);
        return new EmpleadosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmpleadosViewHolder holder, int position) {
        Empleado empleado = listaempleados.get(position);

        holder.vid.setText(String.valueOf(empleado.getId_empleados()));
        holder.vempleado.setText(empleado.getNombreCompleto());

        if (empleado.getDpto() != null) {
            holder.vdpto.setText(empleado.getDpto().getNombre());
        } else {
            holder.vdpto.setText("Sin departamento");
        }

        holder.btnEditar.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditarClick(empleado);
            }
        });

        holder.btnEliminar.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEliminarClick(empleado);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listaempleados.size();
    }

    public class EmpleadosViewHolder extends RecyclerView.ViewHolder {
        TextView vid, vempleado, vdpto;
        ImageButton btnEditar, btnEliminar;

        public EmpleadosViewHolder(@NonNull View itemView) {
            super(itemView);
            vid = itemView.findViewById(R.id.txt_col1);
            vempleado = itemView.findViewById(R.id.txt_col2);
            vdpto = itemView.findViewById(R.id.txt_col3);
            btnEditar = itemView.findViewById(R.id.btn_editar);
            btnEliminar = itemView.findViewById(R.id.btn_eliminar);
        }
    }
}

