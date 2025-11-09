package com.example.barberiashop_app.ui.personal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.barberiashop_app.R;
// IMPORTANTE: Asegúrate de que R apunte a tu paquete base (com.example.barberiashop_app)

/**
 * Fragmento estático para mostrar la lista del personal ("Nuestro equipo").
 * Incluye una barra superior con un botón de retroceso.
 */
public class PersonalFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Infla el layout estático que creaste (fragment_personal.xml)
        return inflater.inflate(R.layout.fragment_personal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Obtener referencia al botón de retroceso
        ImageButton backButton = view.findViewById(R.id.btn_back);

        // 2. Configurar la acción al hacer clic en el botón de retroceso
        if (backButton != null) {
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Esta acción simula presionar el botón de "Atrás" del sistema.
                    // Hace que el Fragmento actual desaparezca de la pila.
                    if (getActivity() != null) {
                        getActivity().onBackPressed();
                    }

                    // Nota: Si usas Android Navigation Component, aquí usarías
                    // Navigation.findNavController(v).popBackStack();
                }
            });
        }
    }
}