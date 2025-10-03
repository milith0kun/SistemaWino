package com.example.sistemadecalidad.data.repository

import com.example.sistemadecalidad.data.api.*
import com.example.sistemadecalidad.data.model.ControlCoccionRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.util.Log

/**
 * Repositorio para operaciones HACCP
 * Maneja las llamadas a la API de formularios de control HACCP
 */
class HaccpRepository(
    private val apiService: ApiService
) {
    companion object {
        private const val TAG = "HaccpRepository"
    }

    /**
     * Registrar control de lavado de frutas
     */
    suspend fun registrarLavadoFrutas(
        token: String,
        mes: Int,
        anio: Int,
        productoQuimico: String,
        concentracion: Double,
        nombreFruta: String,
        lavadoAgua: String,
        desinfeccion: String,
        concentracionCorrecta: String,
        tiempoDesinfeccion: Int,
        accionesCorrectivas: String?
    ): Result<HaccpResponse> = withContext(Dispatchers.IO) {
        try {
            val bearerToken = if (token.startsWith("Bearer ")) token else "Bearer $token"
            val request = LavadoFrutasRequest(
                mes,
                anio,
                productoQuimico,
                concentracion,
                nombreFruta,
                lavadoAgua,
                desinfeccion,
                concentracionCorrecta,
                tiempoDesinfeccion,
                accionesCorrectivas
            )
            
            Log.d(TAG, "Registrando lavado de frutas: $request")
            val response = apiService.registrarLavadoFrutas(bearerToken, request)
            
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                if (body.success) {
                    Log.d(TAG, "✅ Lavado de frutas registrado exitosamente")
                    Result.success(body)
                } else {
                    Log.e(TAG, "❌ Error en lavado de frutas: ${body.error}")
                    Result.failure(Exception(body.error ?: "Error desconocido"))
                }
            } else {
                val errorMsg = "Error HTTP ${response.code()}: ${response.message()}"
                Log.e(TAG, errorMsg)
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Excepción en lavado de frutas", e)
            Result.failure(e)
        }
    }

    /**
     * Registrar control de lavado de manos
     */
    suspend fun registrarLavadoManos(
        token: String,
        empleadoId: Int,
        area: String,
        turno: String?,
        firma: String?,
        procedimientoCorrecto: String,
        accionCorrectiva: String?,
        supervisorId: Int?
    ): Result<HaccpResponse> = withContext(Dispatchers.IO) {
        try {
            val bearerToken = if (token.startsWith("Bearer ")) token else "Bearer $token"
            val request = LavadoManosRequest(
                empleadoId = empleadoId,
                areaEstacion = area,
                turno = turno,
                firma = firma,
                procedimientoCorrecto = procedimientoCorrecto,
                accionCorrectiva = accionCorrectiva,
                supervisorId = supervisorId
            )
            
            Log.d(TAG, "Registrando lavado de manos: empleado=$empleadoId, area=$area, turno=$turno, supervisor=$supervisorId")
            val response = apiService.registrarLavadoManos(bearerToken, request)
            
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                if (body.success) {
                    Log.d(TAG, "✅ Lavado de manos registrado exitosamente")
                    Result.success(body)
                } else {
                    Log.e(TAG, "❌ Error en lavado de manos: ${body.error}")
                    Result.failure(Exception(body.error ?: "Error desconocido"))
                }
            } else {
                val errorMsg = "Error HTTP ${response.code()}: ${response.message()}"
                Log.e(TAG, errorMsg)
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Excepción en lavado de manos", e)
            Result.failure(e)
        }
    }
    
    /**
     * Registrar control de cocción
     */
    suspend fun registrarControlCoccion(
        token: String,
        productoCocinar: String,
        procesoCoccion: String,
        temperatura: Double,
        tiempoCoccion: Int,
        accionCorrectiva: String?
    ): Result<HaccpResponse> = withContext(Dispatchers.IO) {
        try {
            val bearerToken = if (token.startsWith("Bearer ")) token else "Bearer $token"
            val request = ControlCoccionRequest(
                producto_cocinar = productoCocinar,
                proceso_coccion = procesoCoccion,
                temperatura_coccion = temperatura,
                tiempo_coccion_minutos = tiempoCoccion,
                accion_correctiva = accionCorrectiva
            )
            
            Log.d(TAG, "Registrando control de cocción: producto=$productoCocinar, temp=$temperatura")
            val response = apiService.registrarControlCoccion(bearerToken, request)
            
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                if (body.success) {
                    Log.d(TAG, "✅ Control de cocción registrado exitosamente")
                    Result.success(body)
                } else {
                    Log.e(TAG, "❌ Error en control de cocción: ${body.error}")
                    Result.failure(Exception(body.error ?: "Error desconocido"))
                }
            } else {
                val errorMsg = "Error HTTP ${response.code()}: ${response.message()}"
                Log.e(TAG, errorMsg)
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Excepción en control de cocción", e)
            Result.failure(e)
        }
    }
    
    /**
     * Obtener supervisores disponibles
     */
    suspend fun obtenerSupervisores(
        token: String,
        area: String? = null,
        turno: String? = null
    ): Result<List<Empleado>> = withContext(Dispatchers.IO) {
        try {
            val bearerToken = if (token.startsWith("Bearer ")) token else "Bearer $token"
            
            Log.d(TAG, "Obteniendo supervisores - Área: $area, Turno: $turno")
            val response = apiService.obtenerSupervisores(bearerToken, area, turno)
            
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                if (body.success && body.data != null) {
                    Log.d(TAG, "✅ ${body.data.size} supervisores obtenidos")
                    Result.success(body.data)
                } else {
                    Log.e(TAG, "❌ Error del servidor")
                    Result.failure(Exception("Error al obtener supervisores"))
                }
            } else {
                val errorMsg = "Error HTTP ${response.code()}: ${response.message()}"
                Log.e(TAG, errorMsg)
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Excepción obteniendo supervisores", e)
            Result.failure(e)
        }
    }

    /**
     * Registrar control de temperatura de cámaras
     */
    suspend fun registrarTemperaturaCamaras(
        token: String,
        camaraId: Int,
        fecha: String,
        temperaturaManana: Double?,
        temperaturaTarde: Double?,
        accionesCorrectivas: String?
    ): Result<HaccpResponse> = withContext(Dispatchers.IO) {
        try {
            val bearerToken = if (token.startsWith("Bearer ")) token else "Bearer $token"
            val request = TemperaturaCamarasRequest(
                camaraId = camaraId,
                fecha = fecha,
                temperaturaManana = temperaturaManana,
                temperaturaTarde = temperaturaTarde,
                accionesCorrectivas = accionesCorrectivas
            )
            
            Log.d(TAG, "Registrando temperatura cámara: camara=$camaraId, fecha=$fecha")
            val response = apiService.registrarTemperaturaCamaras(bearerToken, request)
            
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                if (body.success) {
                    Log.d(TAG, "✅ Temperatura cámara registrada exitosamente")
                    Result.success(body)
                } else {
                    Log.e(TAG, "❌ Error en temperatura cámara: ${body.error}")
                    Result.failure(Exception(body.error ?: "Error desconocido"))
                }
            } else {
                val errorMsg = "Error HTTP ${response.code()}: ${response.message()}"
                Log.e(TAG, errorMsg)
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Excepción en temperatura cámara", e)
            Result.failure(e)
        }
    }

    /**
     * Obtener lista de cámaras frigoríficas
     */
    suspend fun obtenerCamaras(token: String): Result<List<Camara>> = withContext(Dispatchers.IO) {
        try {
            val bearerToken = if (token.startsWith("Bearer ")) token else "Bearer $token"
            
            Log.d(TAG, "Obteniendo lista de cámaras")
            val response = apiService.obtenerCamaras(bearerToken)
            
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                if (body.success && body.data != null) {
                    Log.d(TAG, "✅ ${body.data.size} cámaras obtenidas")
                    Result.success(body.data)
                } else {
                    Log.e(TAG, "❌ Error obteniendo cámaras: ${body.error}")
                    Result.failure(Exception(body.error ?: "Error desconocido"))
                }
            } else {
                val errorMsg = "Error HTTP ${response.code()}: ${response.message()}"
                Log.e(TAG, errorMsg)
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Excepción obteniendo cámaras", e)
            Result.failure(e)
        }
    }

    /**
     * Registrar recepción de abarrotes
     */
    suspend fun registrarRecepcionAbarrotes(
        token: String,
        mes: Int,
        anio: Int,
        fecha: String,
        hora: String,
        nombreProveedor: String,
        nombreProducto: String,
        cantidadSolicitada: String,
        registroSanitarioVigente: Boolean,
        evaluacionVencimiento: String,
        conformidadEmpaque: String,
        uniformeCompleto: String,
        transporteAdecuado: String,
        puntualidad: String,
        observaciones: String?,
        accionCorrectiva: String?,
        supervisorId: Int
    ): Result<HaccpResponse> = withContext(Dispatchers.IO) {
        try {
            val bearerToken = if (token.startsWith("Bearer ")) token else "Bearer $token"
            val request = RecepcionAbarrotesRequest(
                mes = mes,
                anio = anio,
                fecha = fecha,
                hora = hora,
                nombreProveedor = nombreProveedor,
                nombreProducto = nombreProducto,
                cantidadSolicitada = cantidadSolicitada,
                registroSanitarioVigente = registroSanitarioVigente,
                evaluacionVencimiento = evaluacionVencimiento,
                conformidadEmpaque = conformidadEmpaque,
                uniformeCompleto = uniformeCompleto,
                transporteAdecuado = transporteAdecuado,
                puntualidad = puntualidad,
                observaciones = observaciones,
                accionCorrectiva = accionCorrectiva,
                supervisorId = supervisorId
            )
            
            Log.d(TAG, "Registrando recepción de abarrotes: proveedor=$nombreProveedor, producto=$nombreProducto, supervisor=$supervisorId")
            val response = apiService.registrarRecepcionAbarrotes(bearerToken, request)
            
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                if (body.success) {
                    Log.d(TAG, "✅ Recepción de abarrotes registrada exitosamente")
                    Result.success(body)
                } else {
                    Log.e(TAG, "❌ Error en recepción de abarrotes: ${body.error}")
                    Result.failure(Exception(body.error ?: "Error desconocido"))
                }
            } else {
                val errorMsg = "Error HTTP ${response.code()}: ${response.message()}"
                Log.e(TAG, errorMsg)
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Excepción en recepción de abarrotes", e)
            Result.failure(e)
        }
    }
    
    /**
     * Registrar recepción de frutas y verduras
     */
    suspend fun registrarRecepcionMercaderia(
        token: String,
        mes: Int,
        anio: Int,
        fecha: String,
        hora: String,
        tipoControl: String,
        nombreProveedor: String,
        nombreProducto: String,
        cantidadSolicitada: String,
        pesoUnidadRecibido: Double,
        unidadMedida: String,
        estadoProducto: String,
        conformidadIntegridad: String,
        uniformeCompleto: String,
        transporteAdecuado: String,
        puntualidad: String,
        observaciones: String?,
        accionCorrectiva: String?,
        productoRechazado: Boolean,
        supervisorId: Int?
    ): Result<HaccpResponse> = withContext(Dispatchers.IO) {
        try {
            val bearerToken = if (token.startsWith("Bearer ")) token else "Bearer $token"
            val request = RecepcionFrutasVerdurasRequest(
                mes = mes,
                anio = anio,
                fecha = fecha,
                hora = hora,
                tipoControl = tipoControl,
                nombreProveedor = nombreProveedor,
                nombreProducto = nombreProducto,
                cantidadSolicitada = cantidadSolicitada,
                pesoUnidadRecibido = pesoUnidadRecibido,
                unidadMedida = unidadMedida,
                estadoProducto = estadoProducto,
                conformidadIntegridad = conformidadIntegridad,
                uniformeCompleto = uniformeCompleto,
                transporteAdecuado = transporteAdecuado,
                puntualidad = puntualidad,
                observaciones = observaciones,
                accionCorrectiva = accionCorrectiva,
                productoRechazado = productoRechazado,
                supervisorId = supervisorId
            )
            
            Log.d(TAG, "Registrando recepción de mercadería: tipo=$tipoControl, proveedor=$nombreProveedor, producto=$nombreProducto")
            val response = apiService.registrarRecepcionMercaderia(bearerToken, request)
            
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                if (body.success) {
                    Log.d(TAG, "✅ Recepción de mercadería registrada exitosamente")
                    Result.success(body)
                } else {
                    Log.e(TAG, "❌ Error en recepción de mercadería: ${body.error}")
                    Result.failure(Exception(body.error ?: "Error desconocido"))
                }
            } else {
                val errorMsg = "Error HTTP ${response.code()}: ${response.message()}"
                Log.e(TAG, errorMsg)
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Excepción en recepción de mercadería", e)
            Result.failure(e)
        }
    }
    
    /**
     * Obtener lista de empleados
     */
    suspend fun obtenerEmpleados(token: String, area: String? = null): Result<List<Empleado>> = withContext(Dispatchers.IO) {
        try {
            val bearerToken = if (token.startsWith("Bearer ")) token else "Bearer $token"
            
            val areaLog = if (area != null) " del área $area" else ""
            Log.d(TAG, "Obteniendo lista de empleados$areaLog")
            val response = apiService.obtenerEmpleados(bearerToken, area)
            
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                if (body.success && body.data != null) {
                    Log.d(TAG, "✅ ${body.data.size} empleados obtenidos")
                    Result.success(body.data)
                } else {
                    Log.e(TAG, "❌ Error obteniendo empleados: ${body.error}")
                    Result.failure(Exception(body.error ?: "Error desconocido"))
                }
            } else {
                val errorMsg = "Error HTTP ${response.code()}: ${response.message()}"
                Log.e(TAG, errorMsg)
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Excepción obteniendo empleados", e)
            Result.failure(e)
        }
    }
}
