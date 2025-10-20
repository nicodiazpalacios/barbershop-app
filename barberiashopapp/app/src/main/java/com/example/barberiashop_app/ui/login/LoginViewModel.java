package com.example.barberiashop_app.ui.login;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.barberiashop_app.UserPreferences;
import com.example.barberiashop_app.data.db.AppDatabase;
import com.example.barberiashop_app.data.repository.UsuarioRepository;
import com.example.barberiashop_app.domain.entity.Usuario;

public class LoginViewModel extends AndroidViewModel {

    private final UsuarioRepository usuarioRepository;
    private final UserPreferences userPrefs;

    private final MutableLiveData<Boolean> loginResult = new MutableLiveData<>();

    public LoginViewModel(Application application) {
        super(application);
        usuarioRepository = new UsuarioRepository(application);
        userPrefs = new UserPreferences(application);
    }

    public LiveData<Boolean> getLoginResult() {
        return loginResult;
    }

    public void loginUser(String email, String password) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            Usuario usuario = usuarioRepository.login(email, password);

            if (usuario != null) {
                // guardar datos en SharedPreferences
                userPrefs.registerUser(
                        usuario.getNombre(),
                        usuario.getEmail(),
                        usuario.getCelular(),
                        usuario.getContrasenia(),
                        usuario.getFotoUrl()
                );
                userPrefs.setLoggedIn(true);
                loginResult.postValue(true);
            } else {
                loginResult.postValue(false);
            }
        });
    }

    public void logout() {
        userPrefs.logoutUser();
    }
}
