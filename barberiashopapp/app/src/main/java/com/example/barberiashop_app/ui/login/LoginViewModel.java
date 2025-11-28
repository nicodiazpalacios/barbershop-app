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
//    private final UserPreferences userPrefs;

    private final MutableLiveData<Boolean> loginResult = new MutableLiveData<>();

    public LoginViewModel(Application application) {
        super(application);
        usuarioRepository = new UsuarioRepository(application);
//        userPrefs = new UserPreferences(application);
    }

    public LiveData<Boolean> getLoginResult() {
        return loginResult;
    }

    //    public void loginUser(String email, String password) {
//        // Usamos el LiveData que el repositorio devuelve.
//        // Creamos un observador de única vez para capturar el resultado.
//        usuarioRepository.login(email, password).observeForever(new androidx.lifecycle.Observer<Usuario>() {
//            @Override
//            public void onChanged(Usuario usuario) {
//                // El repositorio ya hizo setLoggedIn y saveLoggedInUserEmail si usuario != null
//                if (usuario != null) {
//                    loginResult.postValue(true);
//                } else {
//                    loginResult.postValue(false);
//                }
//                // Importante: Remover el observador después de la primera notificación
//                usuarioRepository.login(email, password).removeObserver(this);
//            }
//        });
//    }
    public void loginUser(String email, String password) {
        // Llamamos al metodo remoto del repositorio
        // Pasamos el LiveData para que el Repo lo actualice cuando la API responda
        usuarioRepository.loginRemoto(email, password, loginResult);
    }


    public void logout() {
        usuarioRepository.logout();
    }
}
