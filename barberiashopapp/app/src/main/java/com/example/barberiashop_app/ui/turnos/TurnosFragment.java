package com.example.barberiashop_app.ui.turnos;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.barberiashop_app.R;
import com.example.barberiashop_app.databinding.FragmentTurnosBinding;

public class TurnosFragment extends Fragment {
    private FragmentTurnosBinding binding;
    private TurnosAdapter adapter;
    private java.util.List<com.example.barberiashop_app.domain.entity.TurnoConServicio> currentTurnos;
    private boolean showingAll = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        binding = FragmentTurnosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        adapter = new TurnosAdapter();
        binding.recyclerTurnos.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerTurnos.setAdapter(adapter);

        TurnosViewModel viewModel = new ViewModelProvider(this).get(TurnosViewModel.class);
        // LLAMADA CLAVE: Refrescar el usuario ANTES de observar
        // Esto asegura que el ViewModel obtenga el email más reciente de las
        // SharedPreferences.
        viewModel.refreshUserData();
        viewModel.getTurnosUsuario().observe(getViewLifecycleOwner(), turnos -> {
            currentTurnos = turnos; // Guardar la lista completa
            updateUI();
        });

        adapter.setOnEstadoClickListener(turno -> {
            new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle("Cancelar turno")
                    .setMessage("¿Querés cancelar este turno?")
                    .setPositiveButton("Sí, cancelar", (dialog, which) -> {
                        viewModel.cancelarTurno(turno, requireContext());
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        binding.btnIrAServicios.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.navigation_servicios);
        });

        // Manejo del botón de retroceso (btnBack)
        binding.btnBack.setOnClickListener(v -> {
            // Ya que TurnosFragment es un destino raíz,
            // TODO: la lógica de retroceso puede ser:
            // 1. Ocultar el botón (recomendado)
            // 2. Usar popBackStack para simular el botón de atrás del sistema.
            // Si no hay nada más en la pila, simplemente cierra la app o va al inicio.
            Navigation.findNavController(v).navigate(R.id.navigation_servicios);
        });

        binding.btnFiltroOrden.setOnClickListener(v -> showFilterOrderDialog(viewModel));

        binding.btnVerTodos.setOnClickListener(v -> {
            showingAll = true;
            updateUI();
        });

        return root;
    }

    // NUEVO: Método para mostrar el diálogo de opciones
    private void showFilterOrderDialog(TurnosViewModel viewModel) {
        String[] options = {
                "Fecha (más reciente primero)",
                "Fecha (más antigua primero)",
                "Horario (ASC)",
                "Servicio (A-Z)",
                "Estado: Pendiente",
                "Estado: Cancelado",
                "Estado: Finalizado",
                "Mostrar Todos"
        };

        // Mapeo simple de opciones a claves del ViewModel
        String[] keys = {
                "FECHA_DESC",
                "FECHA_ASC",
                "HORARIO_ASC",
                "SERVICIO_AZ",
                "PENDIENTE",
                "CANCELADO",
                "FINALIZADO",
                "TODOS"
        };

        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Filtrar y Ordenar Turnos")
                .setItems(options, (dialog, which) -> {
                    String key = keys[which];

                    if (key.startsWith("FECHA") || key.startsWith("HORARIO") || key.startsWith("SERVICIO")) {
                        // Si es una opción de ORDENAMIENTO
                        viewModel.setOrden(key);
                        viewModel.setFiltro("TODOS"); // Limpiar filtro si se ordena
                    } else if (key.equals("TODOS")) {
                        // Si es "Mostrar Todos"
                        viewModel.setOrden("DEFAULT"); // Restaurar orden por defecto (Pila/LIFO)
                        viewModel.setFiltro("TODOS");
                    } else {
                        // Si es una opción de FILTRADO por estado
                        viewModel.setFiltro(key);
                    }
                })
                .show();
    }

    private void updateUI() {
        if (currentTurnos == null || currentTurnos.isEmpty()) {
            binding.recyclerTurnos.setVisibility(View.GONE);
            binding.layoutNoTurnos.setVisibility(View.VISIBLE);
            binding.btnVerTodos.setVisibility(View.GONE);
        } else {
            binding.recyclerTurnos.setVisibility(View.VISIBLE);
            binding.layoutNoTurnos.setVisibility(View.GONE);

            if (!showingAll && currentTurnos.size() > 8) {
                // Mostrar solo los primeros 8
                adapter.setTurnos(currentTurnos.subList(0, 8));
                binding.btnVerTodos.setVisibility(View.VISIBLE);
            } else {
                // Mostrar todos
                adapter.setTurnos(currentTurnos);
                binding.btnVerTodos.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}