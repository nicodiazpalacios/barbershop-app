package com.example.barberiashop_app.ui.login;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.barberiashop_app.UserPreferences;

public class LoginViewModel extends AndroidViewModel {

    private UserPreferences userPrefs;
    private MutableLiveData<Boolean> loginResult = new MutableLiveData<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
        userPrefs = new UserPreferences(application.getApplicationContext());
    }

    public LiveData<Boolean> getLoginResult() {
        return loginResult;
    }

    public void loginUser(String email, String password) {
        if (userPrefs.validateUser(email, password)) {
            userPrefs.setLoggedIn(true);
            loginResult.setValue(true);
        } else {
            loginResult.setValue(false);
        }
    }
}
