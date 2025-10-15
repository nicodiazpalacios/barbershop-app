package com.example.barberiashop_app.ui.servicios;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barberiashop_app.R;
import com.example.barberiashop_app.databinding.ItemServicioBinding;
import com.example.barberiashop_app.domain.entity.Servicio;

import java.util.Locale;

public class ServiciosListAdapter extends ListAdapter<Servicio, ServiciosListAdapter.ServiciosViewHolder>  {

    private final OnItemClickListener listener;

    public interface OnItemClickListener{
        void onReservarClick(Servicio servicio);
    }
    public ServiciosListAdapter(@NonNull DiffUtil.ItemCallback<Servicio> diffCallback, OnItemClickListener listener) {
        super(diffCallback);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ServiciosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemServicioBinding binding = ItemServicioBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );

        return new ServiciosViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiciosViewHolder holder, int position) {
        Servicio current = getItem(position);
        holder.bind(current);

        holder.binding.btnReservar.setOnClickListener(v -> {
            if(listener != null){
                listener.onReservarClick(current);
            }
        });
    }

    static class ServiciosViewHolder extends RecyclerView.ViewHolder {
        public final ItemServicioBinding binding;

        private ServiciosViewHolder(ItemServicioBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Servicio servicio){
            binding.tvNombreServicio.setText(servicio.getNombre());
            binding.tvDescripcionServicio.setText(servicio.getDescripcion());

            //formatear precio a $X.XX
            String precioFormateado = String.format(Locale.getDefault(), "$%.2f", servicio.getPrecio());
            binding.tvPrecioServicio.setText(precioFormateado);

            // Formatear duración a X min
            String duracionText = String.format(Locale.getDefault(), "%d min", servicio.getDuracion());
            binding.tvDuracionServicio.setText(duracionText);
        }
    }

    // DiffUtil para mejorar el rendimiento del RecyclerView
    public static class ServicioDiff extends DiffUtil.ItemCallback<Servicio> {

        @Override
        public boolean areItemsTheSame(@NonNull Servicio oldItem, @NonNull Servicio newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Servicio oldItem, @NonNull Servicio newItem) {
            // Comparar campos relevantes para detectar cambios de contenido
            return oldItem.getNombre().equals(newItem.getNombre()) &&
                    oldItem.getPrecio() == newItem.getPrecio() &&
                    oldItem.getDuracion() == newItem.getDuracion();
        }
    }
}
