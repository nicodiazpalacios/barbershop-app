package com.example.barberiashop_app.ui.reservar_turno;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.barberiashop_app.databinding.FragmentReservarTurnoBinding;
import com.example.barberiashop_app.domain.entity.Servicio;

import java.util.Locale;

public class ReservarTurnoFragment extends Fragment {
    private FragmentReservarTurnoBinding binding;
    private ReservarTurnoViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentReservarTurnoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // 🧩 Obtener el argumento que pasaste con Safe Args
        int servicioId = ReservarTurnoFragmentArgs.fromBundle(getArguments()).getServicioId();

        // 🔍 Inicializar el ViewModel
        viewModel = new ViewModelProvider(this).get(ReservarTurnoViewModel.class);

        // 🧠 Observar los datos del servicio según el ID recibido
        viewModel.getServicioById(servicioId).observe(getViewLifecycleOwner(), servicio -> {
            if (servicio != null) {
                displayServicioDetails(servicio);
            }
        });

        // (Opcional) Listener para confirmar la reserva
        binding.btnConfirmarReserva.setOnClickListener(v -> confirmReservation());
        binding.btnBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());
        return root;
    }

    /**
     * Muestra los detalles del servicio en la vista
     */
    private void displayServicioDetails(Servicio servicio) {
        // 🏷️ Nombre
        binding.tvNombreServicioReserva.setText(servicio.getNombre());

        // 💲 Precio
        String precioText = String.format(Locale.getDefault(), "$%.2f", servicio.getPrecio());
        binding.tvPrecioServicioReserva.setText(precioText);

        // ⏱️ Duración
        String duracionText = String.format(Locale.getDefault(), "%d min", servicio.getDuracion());
        binding.tvDuracionServicioReserva.setText(duracionText);

        // (Opcional) Si tienes una descripción:
        // binding.tvDescripcionServicioReserva.setText(servicio.getDescripcion());
    }

    private void confirmReservation() {
        Toast.makeText(getContext(), "✅ Reserva confirmada (simulación)", Toast.LENGTH_LONG).show();
        // TODO: Guardar en base de datos o navegar al fragment de Turnos
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}