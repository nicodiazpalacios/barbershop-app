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

    // Borra solo el estado de la sesión
    public void clearLoggedInUser(){
        prefs.edit().remove(KEY_IS_LOGGED_IN).remove(KEY_LOGGED_IN_EMAIL).apply();
    }

    // NOTA: Todos los métodos como registerUser, saveUserDetails, updateProfile,
    // validateUser, getUserData deben ser ELIMINADOS de esta clase
    // y manejados por UsuarioRepository.
}

    /**
     * Guarda el registro del cliente en SharedPreferences
     * @param name
     * @param email
     * @param phone
     * @param password
     * @param photoUri
     */
    /*public void registerUser(String name, String email, String phone, String password, String photoUri){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PHONE, phone);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_PHOTO_URI, photoUri); // Guardar URI de foto
        editor.apply();
    }*/

    /**
     * Guarda los detalles del usuario en SharedPreferences.
     * @param name
     * @param email
     * @param password
     */
  /*  public void saveUserDetails(String name, String email, String password) {
        registerUser(name, email, "", password, "");
    }*/


    /**
     * Actualiza los datos del cliente.
     */
   /* public void updateProfile(String name, String phone, String password, String photoUri) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_PHONE, phone);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_PHOTO_URI, photoUri);
        editor.apply();
    }

*/    /**
     * Clase auxiliar para devolver los datos del usuario.
     */
   /* public static class UserData {
        public String name;
        public String email;
        public String phone;
        public String password;
        public String photoUri;
    }

*/    /**
     * Obtiene todos los datos del usuario registrado.
     */
   /* public UserData getUserData() {
        UserData user = new UserData();
        user.name = prefs.getString(KEY_NAME, "N/A");
        user.email = prefs.getString(KEY_EMAIL, "N/A");
        user.phone = prefs.getString(KEY_PHONE, "N/A");
        user.password = prefs.getString(KEY_PASSWORD, "");
        user.photoUri = prefs.getString(KEY_PHOTO_URI, null);
        return user;
    }*/

    /**
     * Valida el inicio de sesion
     * @param email
     * @param password
     * @return true si las credenciales coinciden con el registro guardado, false en caso contrario
     */
  /*  public boolean validateUser(String email, String password) {
        String registeredEmail = prefs.getString(KEY_EMAIL, null);
        String registeredPassword = prefs.getString(KEY_PASSWORD, null);

        //Comprueba si existe un registro y si las credenciales coinciden
        return registeredEmail != null && registeredEmail.equalsIgnoreCase(email)
                && registeredPassword != null && registeredPassword.equals(password);
    }*/

    /**
     * marca al usuario como logueado
     */
  /*  public void setLoggedIn(boolean isLoggedIn){
        prefs.edit().putBoolean(KEY_IS_LOGGED_IN, isLoggedIn).apply();
    }*/

    /**
     * verifica si el usuario está actualmente logueado
     * @return
     */
  /*  public boolean isLoggedIn(){
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }*/
   /* public void logoutUser(){
        setLoggedIn(false);
        //Alternativa, borrar los datos del usuario
        // prefs.edit().clear().apply();
    }*/

    //Metodo para obtener el email registrado (util para mostrarlo)
//    public String getRegisteredEmail(){
//        return prefs.getString(KEY_EMAIL, "");
//    }

//}
