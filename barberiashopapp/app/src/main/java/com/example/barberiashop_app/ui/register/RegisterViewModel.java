package com.example.barberiashop_app.ui.register;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.barberiashop_app.UserPreferences;

public class RegisterViewModel extends AndroidViewModel {

    private UserPreferences userPrefs;
    private MutableLiveData<Boolean> registrationResult = new MutableLiveData<>();

    public RegisterViewModel(@NonNull Application application) {
        super(application);
        userPrefs = new UserPreferences(application.getApplicationContext());
    }

    public LiveData<Boolean> getRegistrationResult() {
        return registrationResult;
    }

    public void registerUser(String name, String email, String password, String celular, String photoURI) {
        userPrefs.registerUser(name, email, celular, password, photoURI);
        userPrefs.setLoggedIn(true); // Auto-login despues del registro
        registrationResult.setValue(true);
    }
}
