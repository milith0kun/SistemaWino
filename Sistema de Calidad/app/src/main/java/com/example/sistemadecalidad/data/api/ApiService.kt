package com.example.sistemadecalidad.data.api

import com.example.sistemadecalidad.data.model.*
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.*

/**
 * Interfaz del servicio API para comunicación con el backend
 * Basado en los endpoints definidos en la documentación del backend
 */
interface ApiService {
    
    // ========== AUTENTICACIÓN ==========
    
    /**
     * Login del usuario
     * POST /auth/login
     */
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
    
    /**
     * Verificar token JWT
     * GET /auth/verify
     */
    @GET("auth/verify")
    suspend fun verifyToken(@Header("Authorization") token: String): Response<LoginResponse>
    
    // ========== FICHADO ==========
    
    /**
     * Registrar entrada
     * POST /fichado/entrada
     */
    @POST("fichado/entrada")
    suspend fun registrarEntrada(
        @Header("Authorization") token: String,
        @Body request: FichadoEntradaRequest
    ): Response<FichadoResponse>
    
    /**
     * Registrar salida
     * POST /fichado/salida
     */
    @POST("fichado/salida")
    suspend fun registrarSalida(
        @Header("Authorization") token: String,
        @Body request: FichadoSalidaRequest
    ): Response<FichadoResponse>
    
    /**
     * Obtener historial de fichados
     * GET /fichado/historial
     */
    @GET("fichado/historial")
    suspend fun obtenerHistorial(
        @Header("Authorization") token: String
    ): Response<HistorialResponse>
    
    // ========== DASHBOARD ==========
    
    /**
     * Obtener información del día actual
     * GET /dashboard/hoy
     */
    @GET("dashboard/hoy")
    suspend fun obtenerDashboardHoy(
        @Header("Authorization") token: String
    ): Response<DashboardHoyResponse>
    
    /**
     * Obtener resumen para analítica
     * GET /dashboard/resumen
     */
    @GET("dashboard/resumen")
    suspend fun obtenerResumen(
        @Header("Authorization") token: String
    ): Response<ResumenResponse>
    
    // ========== QR (Para futuras implementaciones) ==========
    
    /**
     * Validar código QR
     * GET /qr/validar/{codigo}
     */
    @GET("qr/validar/{codigo}")
    suspend fun validarCodigoQR(
        @Header("Authorization") token: String,
        @Path("codigo") codigo: String
    ): Response<QRValidationResponse>
    
    // ========== HEALTH CHECK ==========
    
    /**
     * Verificar estado del servidor
     * GET /health
     */
    @GET("health")
    suspend fun healthCheck(): Response<HealthResponse>
    
    // ========== HACCP - FORMULARIOS ==========
    
    /**
     * Registrar control de lavado y desinfección de frutas
     * POST /haccp/lavado-frutas
     */
    @POST("haccp/lavado-frutas")
    suspend fun registrarLavadoFrutas(
        @Header("Authorization") token: String,
        @Body request: LavadoFrutasRequest
    ): Response<HaccpResponse>
    
    /**
     * Registrar control de lavado de manos
     * POST /haccp/lavado-manos
     */
    @POST("haccp/lavado-manos")
    suspend fun registrarLavadoManos(
        @Header("Authorization") token: String,
        @Body request: LavadoManosRequest
    ): Response<HaccpResponse>
    
    /**
     * Registrar control de cocción
     * POST /haccp/control-coccion
     */
    @POST("haccp/control-coccion")
    suspend fun registrarControlCoccion(
        @Header("Authorization") token: String,
        @Body request: ControlCoccionRequest
    ): Response<HaccpResponse>
    
    /**
     * Registrar control de temperatura de cámaras
     * POST /haccp/temperatura-camaras
     */
    @POST("haccp/temperatura-camaras")
    suspend fun registrarTemperaturaCamaras(
        @Header("Authorization") token: String,
        @Body request: TemperaturaCamarasRequest
    ): Response<HaccpResponse>
    
    /**
     * Obtener lista de cámaras frigoríficas
     * GET /haccp/camaras
     */
    @GET("haccp/camaras")
    suspend fun obtenerCamaras(
        @Header("Authorization") token: String
    ): Response<CamarasResponse>
    
    /**
     * Registrar recepción de abarrotes
     * POST /haccp/recepcion-abarrotes
     */
    @POST("haccp/recepcion-abarrotes")
    suspend fun registrarRecepcionAbarrotes(
        @Header("Authorization") token: String,
        @Body request: RecepcionAbarrotesRequest
    ): Response<HaccpResponse>
    
    /**
     * Registrar recepción de frutas y verduras
     * POST /haccp/recepcion-mercaderia
     */
    @POST("haccp/recepcion-mercaderia")
    suspend fun registrarRecepcionMercaderia(
        @Header("Authorization") token: String,
        @Body request: RecepcionFrutasVerdurasRequest
    ): Response<HaccpResponse>
    
    /**
     * Obtener lista de empleados
     * GET /haccp/empleados
     */
        @GET("haccp/empleados")
    suspend fun obtenerEmpleados(
        @Header("Authorization") token: String,
        @Query("area") area: String? = null
    ): Response<EmpleadosResponse>
    
    @GET("haccp/supervisores")
    suspend fun obtenerSupervisores(
        @Header("Authorization") token: String,
        @Query("area") area: String? = null,
        @Query("turno") turno: String? = null
    ): Response<EmpleadosResponse>
}

/**
 * Modelo para la respuesta de validación de QR (futuro)
 */
data class QRValidationResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("codigo_valido")
    val codigoValido: Boolean,
    
    @SerializedName("ubicacion")
    val ubicacion: String?,
    
    @SerializedName("descripcion")
    val descripcion: String?,
    
    @SerializedName("error")
    val error: String?
)

/**
 * Modelo para la respuesta de health check
 */
data class HealthResponse(
    @SerializedName("status")
    val status: String,
    
    @SerializedName("timestamp")
    val timestamp: String,
    
    @SerializedName("uptime")
    val uptime: Double?,
    
    @SerializedName("environment")
    val environment: String?,
    
    @SerializedName("version")
    val version: String?,
    
    @SerializedName("server")
    val server: ServerInfo?,
    
    @SerializedName("database")
    val database: DatabaseInfo?
)

/**
 * Información del servidor en la respuesta de health check
 */
data class ServerInfo(
    @SerializedName("port")
    val port: String,
    
    @SerializedName("host")
    val host: String
)

/**
 * Información de la base de datos en la respuesta de health check
 */
data class DatabaseInfo(
    @SerializedName("path")
    val path: String,
    
    @SerializedName("connected")
    val connected: Boolean
)

// ========== MODELOS HACCP ==========

/**
 * Request para registro de lavado de frutas
 */
data class LavadoFrutasRequest(
    val mes: Int, // 1-12
    val anio: Int, // 2024, 2025, etc.
    
    @SerializedName("producto_quimico")
    val productoQuimico: String,
    
    @SerializedName("concentracion_producto")
    val concentracionProducto: Double,
    
    @SerializedName("nombre_fruta_verdura")
    val nombreFrutaVerdura: String,
    
    @SerializedName("lavado_agua_potable")
    val lavadoAguaPotable: String, // "C" o "NC"
    
    @SerializedName("desinfeccion_producto_quimico")
    val desinfeccionProductoQuimico: String, // "C" o "NC"
    
    @SerializedName("concentracion_correcta")
    val concentracionCorrecta: String, // "C" o "NC"
    
    @SerializedName("tiempo_desinfeccion_minutos")
    val tiempoDesinfeccionMinutos: Int,
    
    @SerializedName("acciones_correctivas")
    val accionesCorrectivas: String?
)

/**
 * Request para registro de lavado de manos
 * El empleado_id puede venir del empleado seleccionado o del usuario logueado (opcional)
 */
data class LavadoManosRequest(
    @SerializedName("empleado_id")
    val empleadoId: Int, // ID del empleado que realizó el lavado de manos
    
    @SerializedName("area_estacion")
    val areaEstacion: String, // "COCINA" o "SALON"
    
    @SerializedName("turno")
    val turno: String?, // "MAÑANA", "TARDE", "NOCHE" - si es null se calcula automáticamente
    
    @SerializedName("firma")
    val firma: String?, // Base64 o "FIRMA_PENDIENTE"
    
    @SerializedName("procedimiento_correcto")
    val procedimientoCorrecto: String, // "C" o "NC"
    
    @SerializedName("accion_correctiva")
    val accionCorrectiva: String?,
    
    @SerializedName("supervisor_id")
    val supervisorId: Int? // ID del supervisor que verifica
)

/**
 * Request para registro de temperatura de cámaras
 */
data class TemperaturaCamarasRequest(
    @SerializedName("camara_id")
    val camaraId: Int,
    
    @SerializedName("fecha")
    val fecha: String, // YYYY-MM-DD
    
    @SerializedName("temperatura_manana")
    val temperaturaManana: Double?,
    
    @SerializedName("temperatura_tarde")
    val temperaturaTarde: Double?,
    
    @SerializedName("acciones_correctivas")
    val accionesCorrectivas: String?
)

/**
 * Request para registro de recepción de abarrotes
 */
data class RecepcionAbarrotesRequest(
    @SerializedName("mes")
    val mes: Int,
    
    @SerializedName("anio")
    val anio: Int,
    
    @SerializedName("fecha")
    val fecha: String, // YYYY-MM-DD
    
    @SerializedName("hora")
    val hora: String, // HH:mm
    
    @SerializedName("nombreProveedor")
    val nombreProveedor: String,
    
    @SerializedName("nombreProducto")
    val nombreProducto: String,
    
    @SerializedName("cantidadSolicitada")
    val cantidadSolicitada: String,
    
    @SerializedName("registroSanitarioVigente")
    val registroSanitarioVigente: Boolean,
    
    @SerializedName("evaluacionVencimiento")
    val evaluacionVencimiento: String, // EXCELENTE, REGULAR, PESIMO
    
    @SerializedName("conformidadEmpaque")
    val conformidadEmpaque: String, // EXCELENTE, REGULAR, PESIMO
    
    @SerializedName("uniformeCompleto")
    val uniformeCompleto: String, // C, NC
    
    @SerializedName("transporteAdecuado")
    val transporteAdecuado: String, // C, NC
    
    @SerializedName("puntualidad")
    val puntualidad: String, // C, NC
    
    @SerializedName("observaciones")
    val observaciones: String?,
    
    @SerializedName("accionCorrectiva")
    val accionCorrectiva: String?,
    
    @SerializedName("supervisorId")
    val supervisorId: Int
)

/**
 * Request para registro de recepción de frutas y verduras
 */
data class RecepcionFrutasVerdurasRequest(
    @SerializedName("mes")
    val mes: Int,
    
    @SerializedName("anio")
    val anio: Int,
    
    @SerializedName("fecha")
    val fecha: String, // YYYY-MM-DD
    
    @SerializedName("hora")
    val hora: String, // HH:mm
    
    @SerializedName("tipo_control")
    val tipoControl: String, // "FRUTAS_VERDURAS"
    
    @SerializedName("nombre_proveedor")
    val nombreProveedor: String,
    
    @SerializedName("nombre_producto")
    val nombreProducto: String,
    
    @SerializedName("cantidad_solicitada")
    val cantidadSolicitada: String,
    
    @SerializedName("peso_unidad_recibido")
    val pesoUnidadRecibido: Double,
    
    @SerializedName("unidad_medida")
    val unidadMedida: String,
    
    @SerializedName("estado_producto")
    val estadoProducto: String, // EXCELENTE, REGULAR, PESIMO
    
    @SerializedName("conformidad_integridad_producto")
    val conformidadIntegridad: String, // EXCELENTE, REGULAR, PESIMO
    
    @SerializedName("uniforme_completo")
    val uniformeCompleto: String, // C, NC
    
    @SerializedName("transporte_adecuado")
    val transporteAdecuado: String, // C, NC
    
    @SerializedName("puntualidad")
    val puntualidad: String, // C, NC
    
    @SerializedName("observaciones")
    val observaciones: String?,
    
    @SerializedName("accion_correctiva")
    val accionCorrectiva: String?,
    
    @SerializedName("producto_rechazado")
    val productoRechazado: Boolean,
    
    @SerializedName("supervisor_id")
    val supervisorId: Int?
)

/**
 * Respuesta genérica para operaciones HACCP
 */
data class HaccpResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("message")
    val message: String?,
    
    @SerializedName("data")
    val data: Any?,
    
    @SerializedName("error")
    val error: String?
)

/**
 * Respuesta para lista de cámaras
 */
data class CamarasResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("data")
    val data: List<Camara>?,
    
    @SerializedName("error")
    val error: String?
)

/**
 * Modelo de cámara frigorífica
 */
data class Camara(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("nombre")
    val nombre: String,
    
    @SerializedName("tipo")
    val tipo: String, // REFRIGERACION o CONGELACION
    
    @SerializedName("temperatura_minima")
    val temperaturaMinima: Double,
    
    @SerializedName("temperatura_maxima")
    val temperaturaMaxima: Double,
    
    @SerializedName("ubicacion")
    val ubicacion: String?
)

/**
 * Respuesta para lista de empleados
 */
data class EmpleadosResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("data")
    val data: List<Empleado>?,
    
    @SerializedName("error")
    val error: String?
)

/**
 * Modelo de empleado
 */
data class Empleado(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("nombre")
    val nombre: String,
    
    @SerializedName("rol")
    val rol: String?,
    
    @SerializedName("cargo")
    val cargo: String?,
    
    @SerializedName("area")
    val area: String?,
    
    @SerializedName("activo")
    val activo: Boolean
)