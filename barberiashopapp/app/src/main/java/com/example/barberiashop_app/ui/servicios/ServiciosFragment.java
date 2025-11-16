package com.example.barberiashop_app.ui.servicios;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barberiashop_app.R;
import com.example.barberiashop_app.databinding.FragmentServiciosBinding;
import com.example.barberiashop_app.domain.entity.Servicio;

public class ServiciosFragment extends Fragment implements ServiciosListAdapter.OnItemClickListener {

    private FragmentServiciosBinding binding;
    private ServiciosViewModel serviciosViewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentServiciosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        serviciosViewModel = new ViewModelProvider(this).get(ServiciosViewModel.class);

        final ServiciosListAdapter adapter = new ServiciosListAdapter(new ServiciosListAdapter.ServicioDiff(), this);
        binding.recyclerViewServicios.setAdapter(adapter);
        binding.recyclerViewServicios.setLayoutManager(new LinearLayoutManager(getContext()));

        serviciosViewModel.getAllServicios().observe(getViewLifecycleOwner(), servicios -> {
            adapter.submitList(servicios);
        });

        // Configurar el click del nuevo botón
        binding.btnVerUbicacion.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_navigation_servicios_to_navigation_mapa_negocio);
        });

        // Opcional: Configurar el título si no está fijo en el XML
        // binding.tvTituloServicios.setText("Servicios disponibles");
        return root;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onReservarClick(Servicio servicio) {
        Toast.makeText(getContext(), "Has seleccionado: " + servicio.getNombre(), Toast.LENGTH_SHORT).show();

        // 🧭 Crear la acción de navegación con Safe Args
        ServiciosFragmentDirections.ActionServiciosFragmentToReservarTurnoFragment action =
                ServiciosFragmentDirections.actionServiciosFragmentToReservarTurnoFragment(servicio.getId());

        // 🔀 Navegar al fragmento de reserva

        NavHostFragment.findNavController(this).navigate(action);
    }
}