package com.example.sistemadecalidad.data.model

import com.google.gson.annotations.SerializedName

data class LavadoFrutas(
    val id: Int = 0,
    val mes: Int,
    val anio: Int,
    val dia: Int,
    val fecha: String,
    val hora: String,
    val producto_quimico: String, // Ej: "Cloro"
    val concentracion_producto: Double, // Concentración en ppm o %
    val nombre_fruta_verdura: String, // Ej: "Lechugas", "Tomates"
    val lavado_agua_potable: String, // C o NC
    val desinfeccion_producto_quimico: String, // C o NC
    val concentracion_correcta: String, // C o NC
    val tiempo_desinfeccion_minutos: Int, // 0-10 min
    val acciones_correctivas: String? = null,
    val supervisor_id: Int,
    val supervisor_nombre: String? = null,
    val timestamp_creacion: String? = null
)

/**
 * Request para lavado y desinfección de frutas y verduras
 * El supervisor se obtiene automáticamente del token JWT
 * La fecha/hora se genera automáticamente en el backend
 * MES y AÑO se pueden pasar o generar automáticamente
 */
data class LavadoFrutasRequest(
    val mes: Int, // 1-12
    val anio: Int, // 2024, 2025, etc.
    @SerializedName("producto_quimico")
    val productoQuimico: String, // Ej: "Cloro"
    @SerializedName("concentracion_producto")
    val concentracionProducto: Double, // Concentración utilizada
    @SerializedName("nombre_fruta_verdura")
    val nombreFrutaVerdura: String, // Tipo de fruta/verdura
    @SerializedName("lavado_agua_potable")
    val lavadoAguaPotable: String, // "C" o "NC"
    @SerializedName("desinfeccion_producto_quimico")
    val desinfeccionProductoQuimico: String, // "C" o "NC"
    @SerializedName("concentracion_correcta")
    val concentracionCorrecta: String, // "C" o "NC"
    @SerializedName("tiempo_desinfeccion_minutos")
    val tiempoDesinfeccionMinutos: Int, // 0-10 min
    @SerializedName("acciones_correctivas")
    val accionesCorrectivas: String? = null // Descripción si es necesario
)

/**
 * Productos químicos comunes para desinfección
 */
enum class ProductoQuimico(val valor: String, val display: String) {
    CLORO("Cloro", "Cloro"),
    HIPOCLORITO("Hipoclorito de Sodio", "Hipoclorito de Sodio"),
    PEROXIDO("Peróxido de Hidrógeno", "Peróxido de Hidrógeno"),
    OTRO("Otro", "Otro")
}
