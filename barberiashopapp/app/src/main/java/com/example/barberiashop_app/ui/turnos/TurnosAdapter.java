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

import java.util.ArrayList;
import java.util.List;

public class TurnosAdapter extends RecyclerView.Adapter<TurnosAdapter.TurnoViewHolder> {

    private List<Turno> turnos = new ArrayList<>();
    private OnEstadoClickListener listener;

    public interface OnEstadoClickListener {
        void onEstadoClick(Turno turno);
    }

    public void setOnEstadoClickListener(OnEstadoClickListener listener) {
        this.listener = listener;
    }

    public void setTurnos(List<Turno> nuevosTurnos) {
        this.turnos = nuevosTurnos;
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(@NonNull TurnoViewHolder holder, int position) {
        Turno turno = turnos.get(position);
        holder.textFecha.setText(turno.getFecha());
        holder.textHora.setText(turno.getHorarioInicio());
        holder.textServicio.setText("Corte de pelo"); // temporal, hasta unir con servicio
        holder.textEstado.setText(turno.getEstadoNombre()); // asumiendo que tenés ese campo o relación


//        holder.textEstado.setOnClickListener(v -> {
//            if (listener != null) listener.onEstadoClick(turno);
//        });

        holder.textEstado.setText(turno.getEstadoNombre());

        // Lógica para aplicar estilos y estado de clic
        if (turno.getEstadoId() == 3) { // 3 es Cancelado
            // Estado CANCELADO: Deshabilitado visual y funcionalmente
            holder.textEstado.setBackgroundResource(R.drawable.bg_status_cancelled);
            holder.textEstado.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.grey_disabled_text));
            holder.textEstado.setEnabled(false); // Deshabilitar el componente
            holder.textEstado.setClickable(false);
            holder.textEstado.setAlpha(0.7f); // Opcional: bajar la opacidad
            holder.textEstado.setOnClickListener(null); // Quitar listener
        } else {
            // Estado PENDIENTE (o cualquier otro estado clicable)
            holder.textEstado.setBackgroundResource(R.drawable.bg_status_pending);
            holder.textEstado.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.status_pending_text));
            holder.textEstado.setEnabled(true); // Habilitar componente
            holder.textEstado.setClickable(true);
            holder.textEstado.setAlpha(1.0f);

            holder.textEstado.setOnClickListener(v -> {
                if (listener != null) listener.onEstadoClick(turno);
            });
        }
    }

    @Override
    public int getItemCount() {
        return turnos.size();
    }

    static class TurnoViewHolder extends RecyclerView.ViewHolder {
        TextView textServicio, textFecha, textHora, textEstado;

        public TurnoViewHolder(@NonNull View itemView) {
            super(itemView);
            textServicio = itemView.findViewById(R.id.textServicio);
            textFecha = itemView.findViewById(R.id.textFecha);
            textHora = itemView.findViewById(R.id.textHora);
            textEstado = itemView.findViewById(R.id.textEstado);
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
