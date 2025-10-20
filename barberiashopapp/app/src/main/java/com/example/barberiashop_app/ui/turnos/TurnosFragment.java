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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentTurnosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        adapter = new TurnosAdapter();
        binding.recyclerTurnos.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerTurnos.setAdapter(adapter);

        TurnosViewModel viewModel = new ViewModelProvider(this).get(TurnosViewModel.class);
        viewModel.getTurnosUsuario().observe(getViewLifecycleOwner(), turnos -> {
            if (turnos == null || turnos.isEmpty()) {
                binding.recyclerTurnos.setVisibility(View.GONE);
                binding.layoutNoTurnos.setVisibility(View.VISIBLE);
            } else {
                binding.recyclerTurnos.setVisibility(View.VISIBLE);
                binding.layoutNoTurnos.setVisibility(View.GONE);
                adapter.setTurnos(turnos);
            }
        });

        binding.btnIrAServicios.setOnClickListener(v -> {
            Navigation.findNavController(v).popBackStack();
            Navigation.findNavController(v).navigate(R.id.navigation_servicios);
        });

        return root;
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        mViewModel = new ViewModelProvider(this).get(TurnosViewModel.class);
//        // TODO: Use the ViewModel
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}