package com.example.barberiashop_app.ui.turnos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TurnosViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private final MutableLiveData<String> mText;

    public TurnosViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is turnos fragment");
    }

    public LiveData<String> getText() {
        return mText;

    }
}