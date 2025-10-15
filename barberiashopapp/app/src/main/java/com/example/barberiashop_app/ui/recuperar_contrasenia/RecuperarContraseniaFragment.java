package com.example.barberiashop_app.ui.recuperar_contrasenia;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.barberiashop_app.databinding.FragmentRecuperarContraseniaBinding;


//public class RecoverPasswordFragment extends Fragment {

public class RecuperarContraseniaFragment extends Fragment {
    private FragmentRecuperarContraseniaBinding binding;

    public RecuperarContraseniaFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRecuperarContraseniaBinding.inflate(inflater, container, false);
        binding.btnBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        return binding.getRoot();
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;

    }

}