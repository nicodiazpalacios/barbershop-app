package com.example.barberiashop_app.ui.turnos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barberiashop_app.R;
import com.example.barberiashop_app.domain.entity.Turno;
import com.example.barberiashop_app.domain.entity.TurnoConServicio;

import java.util.ArrayList;
import java.util.List;

public class TurnosAdapter extends RecyclerView.Adapter<TurnosAdapter.TurnoViewHolder> {

    private List<TurnoConServicio> turnos = new ArrayList<>();
    private OnEstadoClickListener listener;

    public interface OnEstadoClickListener {
        void onEstadoClick(Turno turno);
    }

    public void setOnEstadoClickListener(OnEstadoClickListener listener) {
        this.listener = listener;
    }

    public void setTurnos(List<TurnoConServicio> nuevosTurnos) {
        this.turnos = nuevosTurnos;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull TurnoViewHolder holder, int position) {
        // 1. Obtener el POJO TurnoConServicio y la entidad Turno
        TurnoConServicio itemConServicio = turnos.get(position); // 'turnos' debe ser ahora List<TurnoConServicio>
        Turno turno = itemConServicio.turno; // Accede a la entidad Turno incrustada

        // 2. Obtener el nombre del Servicio (la parte que siempre decía "Corte de
        // pelo")
        String nombreServicio = (itemConServicio.servicios != null && !itemConServicio.servicios.isEmpty())
                ? itemConServicio.servicios.get(0).getNombre()
                : "Servicio Desconocido";

        // 3. Asignar datos del Turno y Servicio al ViewHolder
        // Los datos de Turno se acceden a través de la entidad 'turno'
        holder.textFecha.setText(turno.getFecha());
        holder.textHora.setText(turno.getHorarioInicio());
        holder.textServicio.setText(nombreServicio); // FIX: Usa el nombre del servicio real
        holder.textEstado.setText(turno.getEstadoNombre());

        // Mostrar peluquero
        String peluquero = turno.getPeluquero();
        if (peluquero != null && !peluquero.isEmpty()) {
            holder.textPeluquero.setText("Peluquero: " + peluquero);
            holder.textPeluquero.setVisibility(View.VISIBLE);
        } else {
            holder.textPeluquero.setVisibility(View.GONE);
        }

        // 4. Lógica para aplicar estilos y estado de clic (usando 'turno')
        if (turno.getEstadoId() == 3) { // 3 es Cancelado
            // Estado CANCELADO: Deshabilitado visual y funcionalmente
            holder.textEstado.setBackgroundResource(R.drawable.bg_status_cancelled);
            holder.textEstado
                    .setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.grey_disabled_text));
            holder.textEstado.setEnabled(false);
            holder.textEstado.setClickable(false);
            holder.textEstado.setAlpha(0.7f);
            holder.textEstado.setOnClickListener(null);
        } else {
            // Estado PENDIENTE (o cualquier otro estado clicable)
            holder.textEstado.setBackgroundResource(R.drawable.bg_status_pending);
            holder.textEstado
                    .setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.status_pending_text));
            holder.textEstado.setEnabled(true);
            holder.textEstado.setClickable(true);
            holder.textEstado.setAlpha(1.0f);

            // Configurar el listener para el estado
            holder.textEstado.setOnClickListener(v -> {
                if (listener != null)
                    listener.onEstadoClick(turno);
            });
        }
    }

    @Override
    public int getItemCount() {
        return turnos.size();
    }

    static class TurnoViewHolder extends RecyclerView.ViewHolder {
        TextView textServicio, textFecha, textHora, textEstado, textPeluquero;

        public TurnoViewHolder(@NonNull View itemView) {
            super(itemView);
            textServicio = itemView.findViewById(R.id.textServicio);
            textFecha = itemView.findViewById(R.id.textFecha);
            textHora = itemView.findViewById(R.id.textHora);
            textEstado = itemView.findViewById(R.id.textEstado);
            textPeluquero = itemView.findViewById(R.id.textPeluquero);
        }
    }

    @NonNull
    @Override
    public TurnoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_turno, parent, false);
        return new TurnoViewHolder(itemView);
    }

}
