package com.example.barberiashop_app.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.barberiashop_app.data.dao.UsuarioDao;
import com.example.barberiashop_app.data.db.AppDatabase;
import com.example.barberiashop_app.domain.entity.Usuario;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class UsuarioRepository {
    private final UsuarioDao usuarioDao;

    private final LiveData<List<Usuario>> allUsuarios;

    public UsuarioRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getDatabase(application);
        usuarioDao = appDatabase.usuarioDao();
        allUsuarios = usuarioDao.getAll();
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

    public Usuario login(String email, String password) {
        Future<Usuario> future = AppDatabase.databaseWriteExecutor.submit(new Callable<Usuario>() {
            @Override
            public Usuario call() {
                Usuario usuario = usuarioDao.getUsuarioPorEmailSync(email);
                if (usuario != null && usuario.getContrasenia().equals(password)) {
                    return usuario;
                }
                return null;
            }
        });

        try {
            return future.get(); // bloquea hasta obtener el resultado
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

}
