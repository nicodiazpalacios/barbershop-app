package com.example.barberiashop_app.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barberiashop_app.R;
import com.example.barberiashop_app.domain.entity.Turno;

public class TurnoListAdapter extends ListAdapter<Turno, TurnoListAdapter.TurnoViewHolder> {

    public TurnoListAdapter(@NonNull DiffUtil.ItemCallback<Turno> diffCallback) {
        super(diffCallback);
    }


    @NonNull
    @Override
    public TurnoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TurnoViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TurnoViewHolder holder, int position) {
            Turno current = getItem(position);
            holder.tvFecha.setText(current.getFecha());
            holder.tvHorario.setText(current.getHorarioInicio() + " - " + current.getHorarioFin());
    }

    static class TurnoViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvFecha;
        private final TextView tvHorario;
        public TurnoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFecha = itemView.findViewById(R.id.textFecha);
            tvHorario = itemView.findViewById(R.id.textHorario);
        }
    }
    public static class TurnoDiff extends DiffUtil.ItemCallback<Turno>{

        @Override
        public boolean areItemsTheSame(@NonNull Turno oldItem, @NonNull Turno newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Turno oldItem, @NonNull Turno newItem) {
            return oldItem.getFecha().equals(newItem.getFecha()) &&
                    oldItem.getHorarioInicio().equals(newItem.getHorarioInicio()) &&
                    oldItem.getHorarioFin().equals(newItem.getHorarioFin());
        }
    }
}
