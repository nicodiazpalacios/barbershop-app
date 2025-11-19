package com.example.barberiashop_app.ui.mapa_negocio;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.barberiashop_app.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

public class MapaNegocioFragment extends Fragment implements OnMapReadyCallback {

    private static final int REQUEST_LOCATION_PERMISSION = 1;

    private GoogleMap mMap;
    private MapaNegocioViewModel viewModel;
    private Marker mUserLocationMarker;

    // Vistas de la UI
    private TextView mAddressTextView;
    private TextView mDistanceTextView;
    private Button mNavigateButton;
    private ImageButton mBtnBack;
    private Location location;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inicializar el ViewModel
        viewModel = new ViewModelProvider(this).get(MapaNegocioViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Asumiendo que fragment_mapa_negocio es el layout principal
        View root = inflater.inflate(R.layout.fragment_mapa_negocio, container, false);

        // Inicializar Vistas (Asegúrate de que estos IDs existan en tu layout)
        mBtnBack = root.findViewById(R.id.btnBack);
        mAddressTextView = root.findViewById(R.id.textview_user_address);
        mDistanceTextView = root.findViewById(R.id.textview_distance);
        mNavigateButton = root.findViewById(R.id.button_navigate);

        // Inicializar el Mapa
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Configurar Listeners y Observadores
        setupListeners();
        observeViewModel();

        return root;
    }

    private void setupListeners() {
        mBtnBack.setOnClickListener(v -> {
            // Usar popBackStack del NavController o requireActivity().onBackPressed()
            if (getParentFragmentManager() != null) {
                getParentFragmentManager().popBackStack();
            }
        });

        mNavigateButton.setOnClickListener(v -> navigateToBarber());
    }

    private void observeViewModel() {
        // Observar la dirección geocodificada
        viewModel.currentAddress.observe(getViewLifecycleOwner(), address -> {
            mAddressTextView.setText(getString(R.string.address_text_display, address, System.currentTimeMillis()));
        });

        // Observar la distancia calculada
        viewModel.distanceToBarber.observe(getViewLifecycleOwner(), distance -> {
            mDistanceTextView.setText(distance);
        });

        // Observar la ubicación en tiempo real del usuario
        viewModel.userLocation.observe(getViewLifecycleOwner(), location -> {
            if (location != null && mMap != null) {
                // Llamamos a una función para actualizar el marcador en el mapa
                updateUserLocationMarker(location);
            }
        });
    }

    /**
     * Crea o actualiza el marcador de la ubicación del usuario en el mapa.
     * @param location La ubicación actual del usuario.
     */
    private void updateUserLocationMarker(Location location) {
        this.location = location;
        LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        if (mUserLocationMarker == null) {
            // Si el marcador no existe, lo creamos por primera vez
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(userLatLng)
                    .title("Tu Ubicación")
                    //  Usamos un color diferente (azul) para distinguirlo
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

            mUserLocationMarker = mMap.addMarker(markerOptions);
        } else {
            // Si el marcador ya existe, solo actualizamos su posición
            mUserLocationMarker.setPosition(userLatLng);
        }
    }

    // --- Manejo de Ciclo de Vida del Fragment ---

    @Override
    public void onResume() {
        super.onResume();
        // Intentar iniciar las actualizaciones. El VM verifica si los permisos ya están.
        checkLocationPermissions();
    }

    @Override
    public void onPause() {
        super.onPause();
        viewModel.stopLocationUpdates();
    }

    // --- Manejo de Permisos ---

    private void checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Solicitar permisos
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            // Permisos concedidos, iniciar actualizaciones en el ViewModel
            viewModel.startLocationUpdates();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, iniciar rastreo y habilitar capa en el mapa
                viewModel.startLocationUpdates();
                if (mMap != null) {
                    if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(true);
                    }
                }
            } else {
                Toast.makeText(requireContext(), R.string.location_permission_denied, Toast.LENGTH_SHORT).show();
                mAddressTextView.setText(R.string.permission_required_hint);
            }
        }
    }

    // --- Manejo de Mapas y Navegación ---

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Agregar marcador de la barbería y centrar la cámara.
        LatLng barberia = new LatLng(MapaNegocioViewModel.BARBER_LAT, MapaNegocioViewModel.BARBER_LON);
        mMap.addMarker(new MarkerOptions().position(barberia).title("Barbería DITOMATTO"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(barberia, 15));

        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Habilitar la capa de ubicación si los permisos ya están
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
    }

    private void navigateToBarber() {
        // Crea un URI con las coordenadas de la barbería para la navegación
        String uri = String.format(Locale.ENGLISH, "google.navigation:q=%f,%f",
                MapaNegocioViewModel.BARBER_LAT, MapaNegocioViewModel.BARBER_LON);

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");

        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(requireContext(), "Google Maps no está instalado.", Toast.LENGTH_SHORT).show();
        }
    }
}