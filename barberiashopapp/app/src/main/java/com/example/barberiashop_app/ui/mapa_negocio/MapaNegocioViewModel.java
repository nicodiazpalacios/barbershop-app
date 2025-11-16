package com.example.barberiashop_app.ui.mapa_negocio;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.barberiashop_app.ui.mapa_negocio.utils.FetchAddressTask;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class MapaNegocioViewModel extends AndroidViewModel {

    // --- Ubicación Fija de la Barbería (Ejemplo: UNSE) ---
    public static final double BARBER_LAT = -27.8015276;
    public static final double BARBER_LON = -64.2537982;
    private final Location barberLocation;

    // --- Servicios ---
    private final FusedLocationProviderClient mFusedLocationClient;
    private final ExecutorService mExecutorService;

    // --- LiveData para la UI ---
    private final MutableLiveData<String> _currentAddress = new MutableLiveData<>();
    public final LiveData<String> currentAddress = _currentAddress;

    private final MutableLiveData<String> _distanceToBarber = new MutableLiveData<>();
    public final LiveData<String> distanceToBarber = _distanceToBarber;

    // LiveData para que el Fragment pueda obtener las coordenadas exactas del usuario si las necesita
    private final MutableLiveData<Location> _userLocation = new MutableLiveData<>();
    public final LiveData<Location> userLocation = _userLocation;

    private boolean mTrackingLocation = false;

    // Callback para recibir actualizaciones de ubicación
    private final LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            if (mTrackingLocation) {
                Location userLocation = locationResult.getLastLocation();
                if (userLocation != null) {
                    _userLocation.setValue(userLocation); // Actualiza LiveData de coordenadas

                    // 1. Geocodificación inversa
                    FetchAddressTask task = new FetchAddressTask(
                            getApplication(),
                            MapaNegocioViewModel.this::onGeocodeTaskCompleted,
                            userLocation);
                    mExecutorService.execute(task);

                    // 2. Cálculo de distancia
                    calculateDistance(userLocation);
                }
            }
        }
    };

    public MapaNegocioViewModel(@NonNull Application application) {
        super(application);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(application);
        mExecutorService = Executors.newSingleThreadExecutor();

        barberLocation = new Location("");
        barberLocation.setLatitude(BARBER_LAT);
        barberLocation.setLongitude(BARBER_LON);
    }

    // --- Lógica de Manejo de Ubicación ---

    public void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getApplication(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("ViewModel", "Permisos de ubicación no concedidos. El Fragment debe solicitarlos.");
            return;
        }

        if (mTrackingLocation) return; // Ya estamos rastreando

        mTrackingLocation = true;

        LocationRequest locationRequest = getLocationRequest();
        /* Looper */
        mFusedLocationClient.requestLocationUpdates( locationRequest,  mLocationCallback, null );
    }

    public void stopLocationUpdates() {
        if (mTrackingLocation) {
            mTrackingLocation = false;
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    private void onGeocodeTaskCompleted(String address) {
        _currentAddress.postValue(address);
    }

    private void calculateDistance(Location userLocation) {
        float distanceInMeters = userLocation.distanceTo(barberLocation);
        String distanceText = String.format(Locale.getDefault(),
                "Distancia: %.2f km", distanceInMeters / 1000);
        _distanceToBarber.postValue(distanceText);
    }

    private LocationRequest getLocationRequest() {
        // Intervalo de 10 segundos, el más rápido de 5 segundos
        return new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
                .setMinUpdateIntervalMillis(5000)
                .build();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        stopLocationUpdates();
        mExecutorService.shutdown();
    }
}
