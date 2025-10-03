package com.example.sistemadecalidad.data.model

data class CamaraFrigorifica(
    val id: Int = 0,
    val nombre: String,
    val ubicacion: String,
    val tipo: String, // REFRIGERACION, CONGELACION
    val temperatura_minima: Double,
    val temperatura_maxima: Double,
    val capacidad_litros: Double? = null,
    val activo: Boolean = true,
    val descripcion: String? = null
)

data class ControlTemperaturaCamara(
    val id: Int = 0,
    val mes: Int,
    val anio: Int,
    val dia: Int,
    val fecha: String,
    val hora: String,
    val camara_id: Int,
    val nombre_camara: String,
    val tipo_camara: String, // REFRIGERACION, CONGELACION
    val temperatura_registrada: Double,
    val temperatura_minima_permitida: Double,
    val temperatura_maxima_permitida: Double,
    val conformidad: String, // C, NC
    val accion_correctiva: String? = null,
    val responsable_id: Int,
    val responsable_nombre: String,
    val timestamp_creacion: String? = null
)

data class ControlTemperaturaCamaraRequest(
    val mes: Int,
    val anio: Int,
    val dia: Int,
    val fecha: String,
    val hora: String,
    val camara_id: Int,
    val nombre_camara: String,
    val tipo_camara: String,
    val temperatura_registrada: Double,
    val temperatura_minima_permitida: Double,
    val temperatura_maxima_permitida: Double,
    val accion_correctiva: String? = null,
    val responsable_id: Int,
    val responsable_nombre: String
)

data class ControlTemperaturaAlimento(
    val id: Int = 0,
    val mes: Int,
    val anio: Int,
    val dia: Int,
    val fecha: String,
    val hora: String,
    val tipo_alimento: String, // CALIENTE, FRIO
    val nombre_alimento: String,
    val temperatura_registrada: Double,
    val temperatura_minima_permitida: Double,
    val temperatura_maxima_permitida: Double,
    val conformidad: String, // C, NC
    val ubicacion_exhibicion: String,
    val accion_correctiva: String? = null,
    val responsable_id: Int,
    val responsable_nombre: String,
    val timestamp_creacion: String? = null
)

data class ControlTemperaturaAlimentoRequest(
    val mes: Int,
    val anio: Int,
    val dia: Int,
    val fecha: String,
    val hora: String,
    val tipo_alimento: String,
    val nombre_alimento: String,
    val temperatura_registrada: Double,
    val temperatura_minima_permitida: Double,
    val temperatura_maxima_permitida: Double,
    val ubicacion_exhibicion: String,
    val accion_correctiva: String? = null,
    val responsable_id: Int,
    val responsable_nombre: String
)

enum class TipoCamara(val valor: String) {
    REFRIGERACION("REFRIGERACION"),
    CONGELACION("CONGELACION")
}

enum class TipoAlimento(val valor: String) {
    CALIENTE("CALIENTE"),
    FRIO("FRIO")
}
