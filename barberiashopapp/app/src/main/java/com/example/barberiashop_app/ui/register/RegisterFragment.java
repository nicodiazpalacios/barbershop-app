package com.example.barberiashop_app.ui.register;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.barberiashop_app.R;
import com.example.barberiashop_app.databinding.FragmentRegisterBinding;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private RegisterViewModel registerViewModel;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        registerViewModel.getRegistrationResult().observe(getViewLifecycleOwner(), registered -> {
            if (registered) {
                // CASO ÉXITO
                Toast.makeText(getContext(), "Registro exitoso. Bienvenido", Toast.LENGTH_SHORT).show();
                NavController navController = NavHostFragment.findNavController(this);
                navController.navigate(R.id.action_registerFragment_to_loginFragment);
            }
        });

        registerViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
            }
        });

        binding.btnRegister.setOnClickListener(v -> registerUser());
        binding.btnBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());
    }

    private void registerUser() {
        String name = binding.etNombreRegister.getText().toString().trim();
        String email = binding.etEmailRegister.getText().toString().trim();
        String password = binding.etPasswordRegister.getText().toString().trim();
        String celular = binding.etCelularRegister.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || celular.isEmpty()) {
            Toast.makeText(getContext(), "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getContext(), "Por favor, ingresa un email válido.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!name.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ]{2,}\\s[a-zA-ZáéíóúÁÉÍÓÚñÑ]+.*$")) {
            Toast.makeText(getContext(), "Ingresa Nombre y Apellido (solo letras, mín 2 letras nombre).",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if (!isValidPassword(password)) {
            Toast.makeText(getContext(),
                    "La contraseña debe tener al menos 8 caracteres, una letra, un número y un carácter especial.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        String photoURI = null;
        registerViewModel.registerUser(name, email, password, celular, photoURI);
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 8)
            return false;
        boolean hasLetter = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isLetter(c))
                hasLetter = true;
            else if (Character.isDigit(c))
                hasDigit = true;
            else if (!Character.isLetterOrDigit(c))
                hasSpecial = true;
        }

        return hasLetter && hasDigit && hasSpecial;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
