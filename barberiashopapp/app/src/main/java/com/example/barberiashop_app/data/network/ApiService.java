package com.example.barberiashop_app.data.network;

import com.example.barberiashop_app.domain.entity.LoginRequest;
import com.example.barberiashop_app.domain.entity.RegisterRequest;
import com.example.barberiashop_app.domain.entity.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    // POST: Registro
//    @POST("auth/register")
//    Call<UserResponse> registerUser(@Body RegisterRequest request);

    @POST("auth/register")
    Call<Void> registerUser(@Body RegisterRequest request);
    // Void porque la respuesta de éxito 201 a veces no trae body parseable o es solo mensaje

    // POST: Login
    // Nota: Asumo que devuelve UserResponse, si devuelve un token, cambia el tipo de retorno.
    @POST("auth/login")
    Call<UserResponse> loginUser(@Body LoginRequest request);

    // GET: Obtener usuario por ID
    @GET("users/{id}")
    Call<UserResponse> getUserProfile(@Path("id") String userId);

    // Usamos RegisterRequest si el body tiene los mismos campos (firstName, lastName, etc)
    @PUT("users/{id}")
    Call<Void> updateUserProfile(@Path("id") String userId, @Body RegisterRequest request);

}
