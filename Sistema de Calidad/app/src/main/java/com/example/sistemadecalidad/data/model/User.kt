package com.example.sistemadecalidad.data.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo de datos para el usuario
 * Basado en la estructura de la tabla usuarios del backend
 */
data class User(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("nombre")
    val nombre: String,
    
    @SerializedName("apellido")
    val apellido: String? = null,
    
    @SerializedName("email")
    val email: String,
    
    @SerializedName("rol")
    val rol: String, // "ADMIN", "SUPERVISOR", "COCINERO", "EMPLEADO"
    
    @SerializedName("cargo")
    val cargo: String? = null,
    
    @SerializedName("area")
    val area: String? = null, // "COCINA", "SALON", "ADMINISTRACION", "ALMACEN"
    
    @SerializedName("activo")
    val activo: Boolean = true,
    
    @SerializedName("fecha_creacion")
    val fechaCreacion: String? = null
) {
    val nombreCompleto: String
        get() = if (apellido != null) "$nombre $apellido" else nombre
}

/**
 * Modelo para la respuesta de login
 */
data class LoginResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("token")
    val token: String?,
    
    @SerializedName("user")
    val user: User?,
    
    @SerializedName("message")
    val message: String?,
    
    @SerializedName("error")
    val error: String?
)

/**
 * Modelo para la petición de login
 */
data class LoginRequest(
    @SerializedName("email")
    val email: String,
    
    @SerializedName("password")
    val password: String
)