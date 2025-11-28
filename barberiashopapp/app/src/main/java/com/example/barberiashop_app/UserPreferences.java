package com.example.barberiashop_app;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferences {
    //nombre del archivo de SharedPreferences
    private static final String PREF_NAME = "BarberiaUserPrefs";

    //claves para guardar los datos del usuario
    private static final String KEY_NAME = "userName";
    private static final String KEY_EMAIL = "userEmail";
    private static final String KEY_PHONE = "userPhone";
    private static final String KEY_PASSWORD = "userPassword";
    private static final String KEY_PHOTO_URI = "userPhotoUri"; // Nuevo para la foto
    private static final String KEY_USER_ID = "userId";

    private static final String KEY_LOGGED_IN_EMAIL = "userEmail"; // Usado para identificar al usuario logueado
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn"; //para persistencia de sesion
    private final SharedPreferences prefs;

    public UserPreferences(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }


    // --- Lógica de Sesión ---

    public void setLoggedIn(boolean isLoggedIn){
        prefs.edit().putBoolean(KEY_IS_LOGGED_IN, isLoggedIn).apply();
    }

    public boolean isLoggedIn(){
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // Guarda solo el email para referencia de sesión (lo que antes era getRegisteredEmail)
    public void saveLoggedInUserEmail(String email) {
        prefs.edit().putString(KEY_LOGGED_IN_EMAIL, email).apply();
    }

    // Obtiene el email del usuario logueado
    public String getRegisteredEmail(){
        return prefs.getString(KEY_LOGGED_IN_EMAIL, null); // Usar null si no hay email
    }


    // --- NUEVOS MÉTODOS PARA EL ID ---
    public void saveUserId(String id) {
        prefs.edit().putString(KEY_USER_ID, id).apply();
    }

    public String getUserId() {
        return prefs.getString(KEY_USER_ID, null);
    }

    // --- NUEVO: Gestión de Contraseña ---
    // Guardamos la contraseña real para poder enviarla en los Updates (PUT)
    public void saveUserPassword(String password) {
        prefs.edit().putString(KEY_PASSWORD, password).apply();
    }

    public String getUserPassword() {
        return prefs.getString(KEY_PASSWORD, null);
    }

    // --- Limpiar ---
    public void clearLoggedInUser(){
        prefs.edit()
                .remove(KEY_IS_LOGGED_IN)
                .remove(KEY_LOGGED_IN_EMAIL)
                .remove(KEY_USER_ID)
                .remove(KEY_PASSWORD) // Borramos también la pass
                .apply();
    }

    // NOTA: Todos los métodos como registerUser, saveUserDetails, updateProfile,
    // validateUser, getUserData deben ser ELIMINADOS de esta clase
    // y manejados por UsuarioRepository.
}

