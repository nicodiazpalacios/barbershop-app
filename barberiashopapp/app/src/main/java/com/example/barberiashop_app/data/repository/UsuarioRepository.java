package com.example.barberiashop_app.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.barberiashop_app.UserPreferences;
import com.example.barberiashop_app.data.dao.UsuarioDao;
import com.example.barberiashop_app.data.db.AppDatabase;
import com.example.barberiashop_app.domain.entity.Usuario;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class UsuarioRepository {
    private final UsuarioDao usuarioDao;
    private final UserPreferences userPreferences;
    private final ExecutorService executorService;
    private final LiveData<List<Usuario>> allUsuarios;

    public UsuarioRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getDatabase(application);
        usuarioDao = appDatabase.usuarioDao();
        allUsuarios = usuarioDao.getAll();
        userPreferences = new UserPreferences(application.getApplicationContext());
        executorService = AppDatabase.databaseWriteExecutor;
    }

    // --- Métodos de Sesión (SharedPreferences) ---
    public void setLoggedIn(boolean isLoggedIn) {
        userPreferences.setLoggedIn(isLoggedIn);
    }

    // Este método permanece en el Repository para que los ViewModels accedan a la persistencia de sesión
    public boolean isLoggedIn() {
        return userPreferences.isLoggedIn();
    }

    // Obtiene el email de la sesión guardada en SharedPreferences
    public String getLoggedInUserEmail() {
        return userPreferences.getRegisteredEmail();
    }

    // --- Métodos de Autenticación (Room) ---

    // Login: Busca en la DB. Si existe, guarda el estado de sesión y el email en SharedPreferences.
    public LiveData<Usuario> login(String email, String password) {
        final MutableLiveData<Usuario> result = new MutableLiveData<>();
        executorService.execute(() -> {
            Usuario user = usuarioDao.getUserByCredentials(email, password);
            if (user != null) {
                userPreferences.setLoggedIn(true);
                userPreferences.saveLoggedInUserEmail(user.getEmail()); // Guardar solo el email
            }
            result.postValue(user); // postValue para actualizar LiveData desde el hilo de background
        });
        return result;
    }

    // Registro: Inserta en la DB.
    public void register(Usuario usuario) {
        executorService.execute(() -> usuarioDao.insert(usuario));
    }

    // Obtener Perfil: Recupera el usuario logueado de la DB usando el email de SharedPreferences.
    public LiveData<Usuario> getLoggedInUser() {
        String email = userPreferences.getRegisteredEmail();
        if (email != null && !email.isEmpty()) {
            // Este método sí puede retornar LiveData directo del DAO
            return usuarioDao.getUserByEmail(email);
        }
        return new MutableLiveData<>(null);
    }

    // Logout: Cierra sesión y borra el email de SharedPreferences.
    public void logout() {
        userPreferences.setLoggedIn(false);
        userPreferences.clearLoggedInUser();
        // Nota: No borramos los datos del usuario de Room, solo la sesión.
    }

    // Actualizar Perfil:
    public void updateProfile(Usuario usuario) {
        executorService.execute(() -> usuarioDao.insert(usuario)); // Usamos insert con REPLACE
    }

    public LiveData<List<Usuario>> getAllUsuarios() {
        return allUsuarios;
    }

    public LiveData<Usuario> findByEmail(String email) {
        return usuarioDao.findByEmail(email);
    }

    public void  insert(Usuario usuario) {
        AppDatabase
                .databaseWriteExecutor
                .execute(() -> usuarioDao.insert(usuario));
    }

    public void  update(Usuario usuario) {
        AppDatabase
                .databaseWriteExecutor
                .execute(() -> usuarioDao.update(usuario));
    }

    public void saveLoggedInUserEmail(String email) {
        userPreferences.saveLoggedInUserEmail(email);
    }

//    public Usuario login(String email, String password) {
//        Future<Usuario> future = AppDatabase.databaseWriteExecutor.submit(new Callable<Usuario>() {
//            @Override
//            public Usuario call() {
//                Usuario usuario = usuarioDao.getUsuarioPorEmailSync(email);
//                if (usuario != null && usuario.getContrasenia().equals(password)) {
//                    return usuario;
//                }
//                return null;
//            }
//        });
//
//        try {
//            return future.get(); // bloquea hasta obtener el resultado
//        } catch (ExecutionException | InterruptedException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

}
