package com.example.barberiashop_app.ui.notificaciones;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.barberiashop_app.databinding.FragmentNotificacionesBinding;

public class NotificacionesFragment extends Fragment {

    private FragmentNotificacionesBinding binding;
    private NotificacionesViewModel viewModel;
    private NotificacionesAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(NotificacionesViewModel.class);

        binding = FragmentNotificacionesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        adapter = new NotificacionesAdapter();
        binding.recyclerNotificaciones.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerNotificaciones.setAdapter(adapter);

        viewModel.getNotificaciones().observe(getViewLifecycleOwner(), notificaciones -> {
            if (notificaciones != null && !notificaciones.isEmpty()) {
                adapter.setNotificaciones(notificaciones);
                binding.textNoNotificaciones.setVisibility(View.GONE);
                binding.recyclerNotificaciones.setVisibility(View.VISIBLE);

                // Marcar como leídas al abrir
                viewModel.markAllAsRead();
            } else {
                binding.textNoNotificaciones.setVisibility(View.VISIBLE);
                binding.recyclerNotificaciones.setVisibility(View.GONE);
            }
        });

        // Refrescar usuario por si cambió
        viewModel.refreshUser();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
