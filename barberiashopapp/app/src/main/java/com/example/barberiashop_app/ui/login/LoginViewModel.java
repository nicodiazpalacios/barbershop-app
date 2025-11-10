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

    public void loginUser(String email, String password) {
        // Usamos el LiveData que el repositorio devuelve.
        // Creamos un observador de única vez para capturar el resultado.
        usuarioRepository.login(email, password).observeForever(new androidx.lifecycle.Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                // 💡 El repositorio ya hizo setLoggedIn y saveLoggedInUserEmail si usuario != null
                if (usuario != null) {
                    loginResult.postValue(true);
                } else {
                    loginResult.postValue(false);
                }
                // Importante: Remover el observador después de la primera notificación
                usuarioRepository.login(email, password).removeObserver(this);
            }
        });
    }
//    public void loginUser(String email, String password) {
//        // LLama al repositorio para ejecutar el login de forma ASÍNCRONA.
//        // El repositorio se encarga de cambiar el LiveData 'result' con el usuario o null.
//
//        // Observamos el resultado del login.
//        usuarioRepository.login(email, password).observeForever(usuario -> {
//            if (usuario != null) {
//                // El Repositorio ya maneja guardar el email y setLoggedIn(true)
//                // Aquí solo actualizamos el resultado en el ViewModel
//                loginResult.postValue(true);
//            } else {
//                // Esto solo se ejecutará si el LiveData inicializa como null
//                // o si el repositorio lo devuelve como null (datos incorrectos).
//                loginResult.postValue(false);
//            }
//
//            // Es crucial remover el observador después de recibir la respuesta
//            // para evitar múltiples actualizaciones no deseadas.
//            usuarioRepository.login(email, password).removeObserver(this::onLoginResult); // Necesitas un método auxiliar para esto
//        });
//    }

    private void onLoginResult(Usuario usuario) {

    }

    public void logout() {
        usuarioRepository.logout();
    }
}
