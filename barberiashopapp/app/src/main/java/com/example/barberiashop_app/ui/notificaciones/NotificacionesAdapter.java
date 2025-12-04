package com.example.barberiashop_app.ui.notificaciones;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barberiashop_app.R;
import com.example.barberiashop_app.domain.entity.Notificacion;

import java.util.ArrayList;
import java.util.List;

public class NotificacionesAdapter extends RecyclerView.Adapter<NotificacionesAdapter.NotificacionViewHolder> {

    private List<Notificacion> notificaciones = new ArrayList<>();

    public void setNotificaciones(List<Notificacion> notificaciones) {
        this.notificaciones = notificaciones;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotificacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notificacion, parent, false);
        return new NotificacionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificacionViewHolder holder, int position) {
        Notificacion notificacion = notificaciones.get(position);
        holder.bind(notificacion);
    }

    @Override
    public int getItemCount() {
        return notificaciones.size();
    }

    static class NotificacionViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitulo;
        private TextView tvMensaje;
        private TextView tvFecha;

        public NotificacionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTituloNotificacion);
            tvMensaje = itemView.findViewById(R.id.tvMensajeNotificacion);
            tvFecha = itemView.findViewById(R.id.tvFechaNotificacion);
        }

        public void bind(Notificacion notificacion) {
            tvTitulo.setText(notificacion.getTitulo());
            tvMensaje.setText(notificacion.getMensaje());

            // Calcular "hace cuanto"
            long now = System.currentTimeMillis();
            CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(notificacion.getFecha(), now,
                    DateUtils.SECOND_IN_MILLIS);
            tvFecha.setText(timeAgo);
        }
    }
}
