package com.example.barberiashop_app;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.barberiashop_app.data.db.AppDatabase;
import com.example.barberiashop_app.data.repository.UsuarioRepository;
import com.example.barberiashop_app.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private UserPreferences userPrefs;
    //private UsuarioRepository usuarioRepository;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userPrefs = new UserPreferences(this);
        //usuarioRepository = new UsuarioRepository(this.getApplication());
        AppDatabase.getDatabase(this);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {

            // AÑADIR: Si el destino actual es un fragmento hijo de una pestaña,
            // asegúrate de que el ítem de la pestaña padre esté seleccionado.
            if (destination.getId() == R.id.reservarTurnoFragment) {
                // Selecciona visualmente la pestaña "Servicios"
                navView.getMenu().findItem(R.id.navigation_servicios).setChecked(true);
            }


//            if (destination.getId() == R.id.reservarTurnoFragment) {
//                // Selecciona visualmente la pestaña "Servicios"
//                navView.getMenu().findItem(R.id.navigation_servicios).setChecked(true);
//            }
//
            if (destination.getId() == R.id.loginFragment ||
                    destination.getId() == R.id.registerFragment ||
                    destination.getId() == R.id.recuperarContraseniaFragment ||
                    destination.getId() == R.id.navigation_mapa_negocio) {
                navView.setVisibility(View.GONE);
            } else {
                navView.setVisibility(View.VISIBLE);
            }
        });

        NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.mobile_navigation);

        if (userPrefs.isLoggedIn()) {
            navGraph.setStartDestination(R.id.navigation_servicios);
        } else {
            navGraph.setStartDestination(R.id.loginFragment);
        }

        navController.setGraph(navGraph);
    }
}