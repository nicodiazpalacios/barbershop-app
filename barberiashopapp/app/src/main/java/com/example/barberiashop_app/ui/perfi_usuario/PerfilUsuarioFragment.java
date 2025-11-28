package com.example.barberiashop_app.ui.perfi_usuario;

import android.net.Uri;
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
import com.example.barberiashop_app.databinding.FragmentPerfilUsuarioBinding;
import com.example.barberiashop_app.domain.entity.Usuario;

public class PerfilUsuarioFragment extends Fragment {

    private FragmentPerfilUsuarioBinding binding;
    private PerfilUsuarioViewModel userProfileViewModel;

    private Usuario currentUser; // Variable para almacenar el usuario actual

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

        // 1. Mostrar Datos del Usuario (Observador)
        userProfileViewModel.getUserData().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                currentUser = user; // Guardamos el usuario actual
                binding.etNombre.setText(user.getNombre());
                binding.etEmail.setText(user.getEmail());
                binding.etCelular.setText(user.getCelular());
                binding.etPassword.setText(user.getContrasenia());

                if (user.getFotoUrl() != null && !user.getFotoUrl().isEmpty()) {
                    binding.ivProfilePhoto.setImageURI(Uri.parse(user.getFotoUrl()));
                } else {
                    binding.ivProfilePhoto.setImageResource(R.drawable.account_circle);
                }
            }
        });

        // 2. Observar Resultado de la Actualización
        userProfileViewModel.getUpdateResult().observe(getViewLifecycleOwner(), updated -> {
            // 1. Manejar nulo para evitar repeticiones o errores
            if (updated == null) {
                return;
            }

            if (updated) {
                Toast.makeText(getContext(), "Perfil actualizado correctamente.", Toast.LENGTH_SHORT).show();

                // 2. Forzamos la recarga inmediata de los datos desde la API para refrescar la pantalla
                userProfileViewModel.loadUserProfile();

            } else {
                Toast.makeText(getContext(), "Error al guardar los cambios.", Toast.LENGTH_SHORT).show();
            }

            // 3. IMPORTANTE: Reseteamos el valor a null en el ViewModel
            // Esto evita que el Toast salga de nuevo si rotas la pantalla o vuelves de otra pestaña
            userProfileViewModel.resetUpdateState();
        });

        // 3. Observar Resultado del Logout
        userProfileViewModel.getLogoutResult().observe(getViewLifecycleOwner(), loggedOut -> {
            if (loggedOut) {
                NavController navController = NavHostFragment.findNavController(PerfilUsuarioFragment.this);
                navController.navigate(R.id.action_perfilUsuarioFragment_to_loginFragment, null,
                        new androidx.navigation.NavOptions.Builder()
                                .setPopUpTo(R.id.mobile_navigation, true)
                                .build());
            }
        });

        // 4. Configurar Botones
        binding.btnLogout.setOnClickListener(v -> userProfileViewModel.logoutUser());
        // Conectar el botón de guardar al nuevo método
        binding.btnGuardarCambios.setOnClickListener(v -> saveChanges());
    }


    /**
     * Recolecta los datos de la UI y llama al ViewModel para actualizar el perfil.
     */
    private void saveChanges() {
        if (currentUser == null) {
            Toast.makeText(getContext(), "Error: No se pudo cargar el perfil.", Toast.LENGTH_SHORT).show();
            return;
        }

        String nuevoNombre = binding.etNombre.getText().toString().trim();
        String nuevaContrasenia = binding.etPassword.getText().toString().trim();

        // Validación de Campos
        if (nuevoNombre.isEmpty()  || nuevaContrasenia.isEmpty()) {
            Toast.makeText(getContext(), "Todos los campos son obligatorios.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear el objeto Usuario actualizado
        Usuario usuarioActualizado = new Usuario(
                nuevoNombre,
                currentUser.getEmail(), // Mantener el email
                nuevaContrasenia,
                currentUser.getFotoUrl(),
                null, // No actualizamos el celular
                currentUser.getRolId() // Mantener el rol ID
        );
        // Es crucial establecer el ID
        usuarioActualizado.setId(currentUser.getId());

        userProfileViewModel.updateProfile(usuarioActualizado);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
