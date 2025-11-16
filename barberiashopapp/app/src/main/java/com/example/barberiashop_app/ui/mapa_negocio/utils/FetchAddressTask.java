package com.example.barberiashop_app.ui.mapa_negocio.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.example.barberiashop_app.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class FetchAddressTask implements  Runnable{


    private final String TAG = FetchAddressTask.class.getSimpleName();
    private Context mContext;
    private Location mLocation;
    private final OnTaskCompleted mListener;

    // El constructor recibe el Location, no lo espera como parámetro de 'run'
    public FetchAddressTask(Context applicationContext,
                            OnTaskCompleted listener,
                            Location location) {
        mContext = applicationContext;
        mListener = listener;
        mLocation = location;
    }

    @Override
    public void run() {
        Geocoder geocoder = new Geocoder(mContext,
                Locale.getDefault());

        List<Address> addresses = null;
        String resultMessage = "";

        try {
            addresses = geocoder.getFromLocation(
                    mLocation.getLatitude(),
                    mLocation.getLongitude(),
                    1);
        } catch (IOException ioException) {
            resultMessage = mContext.getString(R.string.service_not_available);
            Log.e(TAG, "Error de I/O en Geocoder", ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            resultMessage = mContext.getString(R.string.invalid_lat_long_used);
            Log.e(TAG, "Latitud/Longitud inválida: " + mLocation.getLatitude() + ", " + mLocation.getLongitude(), illegalArgumentException);
        }

        if (addresses == null || addresses.size() == 0) {
            if (resultMessage.isEmpty()) {
                resultMessage = mContext.getString(R.string.no_address_found);
                Log.e(TAG, resultMessage);
            }
        } else {
            Address address = addresses.get(0);
            ArrayList<String> addressParts = new ArrayList<>();

            for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                addressParts.add(address.getAddressLine(i));
            }

            // Usamos "\n" para un salto de línea en la dirección legible
            resultMessage = TextUtils.join("\n", addressParts);
        }

        // Devolver el resultado al hilo principal (UI) usando Handler
        final String finalResultMessage = resultMessage;
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> mListener.onTaskCompleted(finalResultMessage));

    }


    public interface OnTaskCompleted {
        void onTaskCompleted(String result);
    }
}
