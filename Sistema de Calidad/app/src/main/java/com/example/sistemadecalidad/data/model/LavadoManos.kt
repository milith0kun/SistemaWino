package com.example.sistemadecalidad.data.model

data class LavadoManos(
    val id: Int = 0,
    val mes: Int,
    val anio: Int,
    val fecha: String,
    val hora: String,
    val area_estacion: String, // COCINA, SALON
    val turno: String, // MAÑANA, TARDE
    val empleado_id: Int,
    val nombres_apellidos: String,
    val firma: String? = null, // Firma digital o imagen base64
    val procedimiento_correcto: String, // C, NC
    val accion_correctiva: String? = null,
    val supervisor_id: Int,
    val supervisor_nombre: String,
    val timestamp_creacion: String? = null
)

data class LavadoManosRequest(
    val mes: Int,
    val anio: Int,
    val fecha: String,
    val hora: String,
    val area_estacion: String,
    val turno: String,
    val empleado_id: Int,
    val nombres_apellidos: String,
    val firma: String? = null,
    val procedimiento_correcto: String,
    val accion_correctiva: String? = null,
    val supervisor_id: Int,
    val supervisor_nombre: String
)

enum class AreaEstacion(val valor: String) {
    COCINA("COCINA"),
    SALON("SALON")
}

enum class TurnoTrabajo(val valor: String) {
    MANANA("MAÑANA"),
    TARDE("TARDE")
}
