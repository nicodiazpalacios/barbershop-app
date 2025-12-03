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
        // 4. Lógica para aplicar estilos y estado de clic (usando 'turno')
        boolean isCancelled = turno.getEstadoId() == 3; // 3 es Cancelado
        boolean isFinished = false;

        // Verificar si el turno ya pasó (Terminado)
        if (!isCancelled) {
            try {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm",
                        java.util.Locale.getDefault());
                // Asumimos que el formato de fecha es dd/MM/yyyy y horario es HH:mm (o similar)
                // Nota: turno.getHorarioInicio() puede venir como "08:00 AM", hay que tener
                // cuidado con el parseo.
                // Si el formato es "hh:mm a", ajustamos.

                String fechaHoraStr = turno.getFecha() + " " + turno.getHorarioInicio();
                // Intentar parsear con formato de 12 horas si tiene AM/PM, o 24 si no.
                // Basado en ReservarTurnoFragment, parece ser "hh:mm a" (ej: 08:00 AM)
                java.text.SimpleDateFormat sdfInput = new java.text.SimpleDateFormat("dd/MM/yyyy hh:mm a",
                        java.util.Locale.getDefault());

                java.util.Date fechaTurno = sdfInput.parse(fechaHoraStr);
                java.util.Date ahora = new java.util.Date();

                if (fechaTurno != null && fechaTurno.before(ahora)) {
                    isFinished = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                // Si falla el parseo, asumimos que no es terminado para no romper la UI
            }
        }

        if (isCancelled) {
            // Estado CANCELADO: Deshabilitado visual y funcionalmente
            holder.textEstado.setText("Cancelado");
            holder.textEstado.setBackgroundResource(R.drawable.bg_status_cancelled);
            holder.textEstado
                    .setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.status_cancelled_text));
            holder.textEstado.setEnabled(false);
            holder.textEstado.setClickable(false);
            holder.textEstado.setAlpha(0.7f);
            holder.textEstado.setOnClickListener(null);
        } else if (isFinished) {
            // Estado TERMINADO: Verde
            holder.textEstado.setText("Terminado");
            holder.textEstado.setBackgroundResource(R.drawable.bg_status_finished);
            holder.textEstado
                    .setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.status_finished_text));
            holder.textEstado.setEnabled(false); // No se puede cancelar si ya terminó
            holder.textEstado.setClickable(false);
            holder.textEstado.setAlpha(1.0f);
            holder.textEstado.setOnClickListener(null);
        } else {
            // Estado PENDIENTE (o cualquier otro estado futuro)
            holder.textEstado.setText(turno.getEstadoNombre()); // Muestra "pendiente" o lo que venga de la BD
            holder.textEstado.setBackgroundResource(R.drawable.bg_status_pending);
            holder.textEstado
                    .setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.status_pending_text));
            holder.textEstado.setEnabled(true);
            holder.textEstado.setClickable(true);
            holder.textEstado.setAlpha(1.0f);

            // Configurar el listener para el estado (para cancelar, etc.)
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
