package com.example.barberiashop_app.ui.perfi_usuario;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.barberiashop_app.UserPreferences;

public class PerfilUsuarioViewModel extends AndroidViewModel {

    private UserPreferences userPrefs;
    private MutableLiveData<Boolean> logoutResult = new MutableLiveData<>();
    private final MutableLiveData<UserPreferences.UserData> userData;

    public PerfilUsuarioViewModel(@NonNull Application application) {
        super(application);
        userPrefs = new UserPreferences(application.getApplicationContext());
        userData = new MutableLiveData<>(userPrefs.getUserData());
    }

    public LiveData<UserPreferences.UserData> getUserData() {
        return userData;
    }

    public LiveData<Boolean> getLogoutResult() {
        return logoutResult;
    }

    public void logoutUser() {
        userPrefs.logoutUser();
        logoutResult.setValue(true);
    }
}
