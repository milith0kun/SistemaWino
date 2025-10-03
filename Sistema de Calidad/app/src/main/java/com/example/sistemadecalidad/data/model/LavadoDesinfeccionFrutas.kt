package com.example.sistemadecalidad.data.model

data class LavadoDesinfeccionFrutas(
    val id: Int = 0,
    val mes: Int,
    val anio: Int,
    val dia: Int,
    val fecha: String,
    val hora: String,
    val producto_quimico: String,
    val concentracion_producto: Double,
    val nombre_fruta_verdura: String,
    val lavado_agua_potable: String, // C, NC
    val desinfeccion_producto_quimico: String, // C, NC
    val concentracion_correcta: String, // C, NC
    val tiempo_desinfeccion_minutos: Int, // 0-10
    val acciones_correctivas: String? = null,
    val supervisor_id: Int,
    val supervisor_nombre: String,
    val timestamp_creacion: String? = null
)

data class LavadoDesinfeccionFrutasRequest(
    val mes: Int,
    val anio: Int,
    val dia: Int,
    val fecha: String,
    val hora: String,
    val producto_quimico: String,
    val concentracion_producto: Double,
    val nombre_fruta_verdura: String,
    val lavado_agua_potable: String,
    val desinfeccion_producto_quimico: String,
    val concentracion_correcta: String,
    val tiempo_desinfeccion_minutos: Int,
    val acciones_correctivas: String? = null,
    val supervisor_id: Int,
    val supervisor_nombre: String
)
