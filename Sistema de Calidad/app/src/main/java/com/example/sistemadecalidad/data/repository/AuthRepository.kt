package com.example.sistemadecalidad.data.repository

import com.example.sistemadecalidad.data.api.ApiService
import com.example.sistemadecalidad.data.model.LoginRequest
import com.example.sistemadecalidad.data.model.LoginResponse
import com.example.sistemadecalidad.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
// import javax.inject.Inject
// import javax.inject.Singleton

/**
 * Repositorio para manejar la autenticación
 * Temporalmente sin Hilt para pruebas de compilación
 */
// @Singleton
class AuthRepository /* @Inject constructor */ (
    private val apiService: ApiService
) {
    
    /**
     * Realizar login con email y contraseña
     */
    suspend fun login(email: String, password: String): Flow<Result<LoginResponse>> = flow {
        try {
            val request = LoginRequest(email = email, password = password)
            val response = apiService.login(request)
            
            if (response.isSuccessful) {
                val loginResponse = response.body()
                if (loginResponse != null) {
                    if (loginResponse.success && loginResponse.token != null) {
                        emit(Result.success(loginResponse))
                    } else {
                        emit(Result.failure(Exception(loginResponse.error ?: "Error desconocido en login")))
                    }
                } else {
                    emit(Result.failure(Exception("Respuesta vacía del servidor")))
                }
            } else {
                emit(Result.failure(Exception("Error HTTP: ${response.code()} - ${response.message()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    /**
     * Verificar si el token JWT sigue siendo válido
     */
    suspend fun verifyToken(token: String): Flow<Result<User>> = flow {
        try {
            val bearerToken = if (token.startsWith("Bearer ")) token else "Bearer $token"
            val response = apiService.verifyToken(bearerToken)
            
            if (response.isSuccessful) {
                val verifyResponse = response.body()
                if (verifyResponse != null) {
                    if (verifyResponse.success && verifyResponse.user != null) {
                        emit(Result.success(verifyResponse.user))
                    } else {
                        emit(Result.failure(Exception(verifyResponse.error ?: "Token inválido")))
                    }
                } else {
                    emit(Result.failure(Exception("Respuesta vacía del servidor")))
                }
            } else {
                emit(Result.failure(Exception("Token expirado o inválido")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    /**
     * Verificar conectividad con el servidor
     */
    suspend fun checkServerHealth(): Flow<Result<Boolean>> = flow {
        try {
            val response = apiService.healthCheck()
            if (response.isSuccessful) {
                val healthResponse = response.body()
                if (healthResponse != null && healthResponse.status == "OK") {
                    emit(Result.success(true))
                } else {
                    emit(Result.failure(Exception("Servidor no disponible")))
                }
            } else {
                emit(Result.failure(Exception("No se puede conectar al servidor")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}