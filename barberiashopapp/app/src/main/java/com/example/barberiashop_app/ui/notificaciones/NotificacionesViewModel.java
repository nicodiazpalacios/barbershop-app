package com.example.barberiashop_app.ui.notificaciones;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.barberiashop_app.UserPreferences;
import com.example.barberiashop_app.data.repository.NotificacionRepository;
import com.example.barberiashop_app.domain.entity.Notificacion;

import java.util.List;

public class NotificacionesViewModel extends AndroidViewModel {
    private NotificacionRepository repository;
    private MutableLiveData<String> usuarioEmail = new MutableLiveData<>();
    private LiveData<List<Notificacion>> notificaciones;
    private LiveData<Integer> unreadCount;

    public NotificacionesViewModel(@NonNull Application application) {
        super(application);
        repository = new NotificacionRepository(application);

        UserPreferences userPreferences = new UserPreferences(application);
        usuarioEmail.setValue(userPreferences.getRegisteredEmail());

        notificaciones = Transformations.switchMap(usuarioEmail, email -> {
            if (email != null) {
                return repository.getAllByUsuario(email);
            }
            return null;
        });

        unreadCount = Transformations.switchMap(usuarioEmail, email -> {
            if (email != null) {
                return repository.getUnreadCount(email);
            }
            return null;
        });
    }

    public LiveData<List<Notificacion>> getNotificaciones() {
        return notificaciones;
    }

    public LiveData<Integer> getUnreadCount() {
        return unreadCount;
    }

    public void markAllAsRead() {
        String email = usuarioEmail.getValue();
        if (email != null) {
            repository.markAllAsRead(email);
        }
    }

    public void refreshUser() {
        UserPreferences userPreferences = new UserPreferences(getApplication());
        usuarioEmail.setValue(userPreferences.getRegisteredEmail());
    }
}
