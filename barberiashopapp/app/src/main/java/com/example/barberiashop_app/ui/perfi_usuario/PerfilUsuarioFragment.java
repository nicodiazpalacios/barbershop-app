package com.example.barberiashop_app.ui.perfi_usuario;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.barberiashop_app.R;
import com.example.barberiashop_app.databinding.FragmentPerfilUsuarioBinding;

public class PerfilUsuarioFragment extends Fragment {

    private FragmentPerfilUsuarioBinding  binding;
    private PerfilUsuarioViewModel userProfileViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPerfilUsuarioBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userProfileViewModel = new ViewModelProvider(this).get(PerfilUsuarioViewModel.class);

        userProfileViewModel.getUserData().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                binding.etNombre.setText(user.name);
                binding.etEmail.setText(user.email);
                binding.etCelular.setText(user.phone);
                binding.etPassword.setText(user.password);
                if (user.photoUri != null) {
                    binding.ivProfilePhoto.setImageURI(Uri.parse(user.photoUri));
                }
            }
        });

        userProfileViewModel.getLogoutResult().observe(getViewLifecycleOwner(), loggedOut -> {
            if (loggedOut) {
                NavController navController = NavHostFragment.findNavController(PerfilUsuarioFragment.this);
                navController.navigate(R.id.action_perfilUsuarioFragment_to_loginFragment);
            }
        });

        binding.btnLogout.setOnClickListener(v -> userProfileViewModel.logoutUser());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
