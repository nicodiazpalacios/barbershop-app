package com.example.barberiashop_app.ui.servicios;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.barberiashop_app.R;
import com.example.barberiashop_app.databinding.FragmentServiciosBinding;

public class ServiciosFragment extends Fragment {

//    private ServiciosViewModel mViewModel;
//
//    public static ServiciosFragment newInstance() {
//        return new ServiciosFragment();
//    }

    private FragmentServiciosBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        ServiciosViewModel serviciosViewModel =
                new ViewModelProvider(this).get(ServiciosViewModel.class);

        binding = FragmentServiciosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textServicios;
        serviciosViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

//        return inflater.inflate(R.layout.fragment_servicios, container, false);
        return root;
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        mViewModel = new ViewModelProvider(this).get(ServiciosViewModel.class);
//        // TODO: Use the ViewModel
//    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}