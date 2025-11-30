package com.example.barberiashop_app.data.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.barberiashop_app.UserPreferences;
import com.example.barberiashop_app.data.dao.UsuarioDao;
import com.example.barberiashop_app.data.db.AppDatabase;
import com.example.barberiashop_app.data.network.ApiService;
import com.example.barberiashop_app.data.network.RetrofitClient;
import com.example.barberiashop_app.domain.entity.LoginRequest;
import com.example.barberiashop_app.domain.entity.RegisterRequest;
import com.example.barberiashop_app.domain.entity.UserResponse;
import com.example.barberiashop_app.domain.entity.Usuario;

import java.util.List;
import java.util.concurrent.ExecutorService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuarioRepository {
    private final UsuarioDao usuarioDao;
    private final UserPreferences userPreferences;
    private final ExecutorService executorService;
    private final ApiService apiService;
    private final LiveData<List<Usuario>> allUsuarios;

    public UsuarioRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getDatabase(application);
        usuarioDao = appDatabase.usuarioDao();
        allUsuarios = usuarioDao.getAll();
        userPreferences = new UserPreferences(application.getApplicationContext());
        executorService = AppDatabase.databaseWriteExecutor;
        apiService = RetrofitClient.getService();
    }

    public void loginRemoto(String email, String password, MutableLiveData<Boolean> loginResult) {
        LoginRequest request = new LoginRequest(email, password);
        Log.d("API_LOGIN", "Intentando login con: " + email);

        apiService.loginUser(request).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserResponse apiUser = response.body();

                    if (apiUser.getId() != null)
                        userPreferences.saveUserId(apiUser.getId());

                    // Guardar pass real para usarla después
                    userPreferences.saveUserPassword(password);

                    userPreferences.setLoggedIn(true);
                    userPreferences.saveLoggedInUserEmail(apiUser.getEmail());

                    executorService.execute(() -> {
                        try {
                            String userEmail = apiUser.getEmail();
                            if (userEmail == null)
                                return; // Evitar crash si email es null

                            Usuario existingUser = usuarioDao.getUserByEmailSync(userEmail);
                            String fName = apiUser.getFirstName() != null ? apiUser.getFirstName() : "Usuario";
                            String lName = apiUser.getLastName() != null ? apiUser.getLastName() : "";

                            if (existingUser != null) {
                                // Actualizamos datos pero mantenemos el ID y el celular si ya existe
                                existingUser.setNombre(fName + " " + lName);
                                // No sobreescribimos celular con "" si ya tiene uno
                                usuarioDao.update(existingUser);
                            } else {
                                // Insertamos nuevo
                                Usuario newUser = new Usuario(fName + " " + lName,
                                        userEmail, "***", null, "", 1);
                                usuarioDao.insert(newUser);
                            }
                        } catch (Exception e) {
                            Log.e("DB_ERROR", "Error guardando usuario en login: " + e.getMessage());
                        }
                    });

                    loginResult.postValue(true);
                } else {
                    loginResult.postValue(false);
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                loginResult.postValue(false);
            }
        });
    }

    public void registerRemoto(String firstName, String lastName, String email, String password, String celular,
            MutableLiveData<Boolean> registerResult, MutableLiveData<String> errorMsg) {
        String finalFirstName = formatName(firstName, "Nombre");
        String finalLastName = formatName(lastName, "Apellido");

        // No enviamos celular al backend
        RegisterRequest request = new RegisterRequest(finalFirstName, finalLastName, email, password);

        apiService.registerUser(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 201) {
                    // Guardamos la pass aunque no haya auto-login, por si acaso
                    userPreferences.saveUserPassword(password);

                    Usuario nuevoUsuario = new Usuario(finalFirstName + " " + finalLastName, email, password, null,
                            celular, 1);
                    executorService.execute(() -> usuarioDao.insert(nuevoUsuario));

                    registerResult.postValue(true);
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "";
                        Log.e("API_REGISTER_ERROR", "Code: " + response.code() + ", Body: " + errorBody);

                        String displayMessage = "Error en el registro";
                        try {
                            org.json.JSONObject jsonObject = new org.json.JSONObject(errorBody);
                            if (jsonObject.has("message")) {
                                displayMessage = jsonObject.getString("message");
                            } else if (jsonObject.has("error")) {
                                displayMessage = jsonObject.getString("error");
                            }
                        } catch (Exception e) {
                            // Si no es JSON, usamos el cuerpo tal cual si es corto, o un genérico
                            if (errorBody.length() < 50)
                                displayMessage = errorBody;
                        }

                        errorMsg.postValue(displayMessage);
                    } catch (Exception e) {
                        Log.e("API_REGISTER_ERROR", "Error reading error body", e);
                        errorMsg.postValue("Error al leer respuesta del servidor");
                    }
                    registerResult.postValue(false);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("API_REGISTER_ERROR", "Failure: " + t.getMessage(), t);
                errorMsg.postValue("Fallo de conexión: " + t.getMessage());
                registerResult.postValue(false);
            }
        });
    }

    public void fetchUserProfile(MutableLiveData<Usuario> userData) {
        String userId = userPreferences.getUserId();
        if (userId == null)
            return;

        apiService.getUserProfile(userId).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserResponse apiUser = response.body();

                    String savedPassword = userPreferences.getUserPassword();
                    String passwordDisplay = (savedPassword != null) ? savedPassword : "";

                    Usuario user = new Usuario(
                            apiUser.getFirstName() + " " + apiUser.getLastName(),
                            apiUser.getEmail(),
                            passwordDisplay, // Usamos la real
                            null,
                            "",
                            1);

                    try {
                        if (apiUser.getId() != null)
                            user.setId(Integer.parseInt(apiUser.getId()));
                    } catch (NumberFormatException e) {
                    }

                    userData.postValue(user);
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
            }
        });
    }

    public void updateProfileRemoto(String firstName, String lastName, String email, String password,
            MutableLiveData<Boolean> updateResult) {
        String userId = userPreferences.getUserId();
        if (userId == null) {
            updateResult.postValue(false);
            return;
        }

        String finalFirstName = formatName(firstName, "Nombre");
        String finalLastName = formatName(lastName, "Apellido");
        String passwordToSend = password;

        if (password.contains("*") || password.isEmpty()) {
            String savedPassword = userPreferences.getUserPassword();
            if (savedPassword != null && !savedPassword.isEmpty()) {
                passwordToSend = savedPassword;
            } else {
                Log.e("API_UPDATE", "Error: No hay contraseña válida para enviar.");
                updateResult.postValue(false);
                return;
            }
        } else {
            userPreferences.saveUserPassword(password);
        }

        RegisterRequest request = new RegisterRequest(finalFirstName, finalLastName, email, passwordToSend);

        apiService.updateUserProfile(userId, request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    updateResult.postValue(true);
                } else {
                    updateResult.postValue(false);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                updateResult.postValue(false);
            }
        });
    }

    private String formatName(String input, String defaultVal) {
        if (input == null || input.trim().isEmpty())
            return defaultVal;
        if (!input.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ]+$"))
            return defaultVal;
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    public void logout() {
        userPreferences.clearLoggedInUser();
        userPreferences.setLoggedIn(false);
    }

    public LiveData<Usuario> getLoggedInUser() {
        String email = userPreferences.getRegisteredEmail();
        if (email != null && !email.isEmpty())
            return usuarioDao.getUserByEmail(email);
        return new MutableLiveData<>(null);
    }

    public void update(Usuario usuario) {
        executorService.execute(() -> usuarioDao.update(usuario));
    }

    public LiveData<List<Usuario>> getAllUsuarios() {
        return allUsuarios;
    }

    public void saveLoggedInUserEmail(String email) {
        userPreferences.saveLoggedInUserEmail(email);
    }

    public void insert(Usuario usuario) {
        AppDatabase.databaseWriteExecutor.execute(() -> usuarioDao.insert(usuario));
    }
}