package com.example.barberiashop_app.ui.userprofile;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.barberiashop_app.UserPreferences;

public class UserProfileViewModel extends AndroidViewModel {

    private UserPreferences userPrefs;
    private MutableLiveData<Boolean> logoutResult = new MutableLiveData<>();

    public UserProfileViewModel(@NonNull Application application) {
        super(application);
        userPrefs = new UserPreferences(application.getApplicationContext());
    }

    public LiveData<Boolean> getLogoutResult() {
        return logoutResult;
    }

    public void logoutUser() {
        userPrefs.logoutUser();
        logoutResult.setValue(true);
    }
}
