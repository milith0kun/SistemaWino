package com.example.sistemadecalidad.data.model

data class RecepcionMercaderia(
    val id: Int = 0,
    val mes: Int,
    val anio: Int,
    val fecha: String,
    val hora: String,
    val tipo_control: String, // FRUTAS_VERDURAS, ABARROTES
    val proveedor_id: Int,
    val nombre_proveedor: String,
    val producto_id: Int,
    val nombre_producto: String,
    val cantidad_solicitada: Double? = null,
    val peso_unidad_recibido: Double,
    val unidad_medida: String,
    
    // Campos para Frutas y Verduras
    val estado_producto: String? = null, // EXCELENTE, REGULAR, PESIMO
    val conformidad_integridad_producto: String? = null, // EXCELENTE, REGULAR, PESIMO
    
    // Campos para Abarrotes
    val registro_sanitario_vigente: Boolean? = null,
    val fecha_vencimiento_producto: String? = null,
    val evaluacion_vencimiento: String? = null, // EXCELENTE, REGULAR, PESIMO
    val conformidad_empaque_primario: String? = null, // EXCELENTE, REGULAR, PESIMO
    
    // Evaluaciones comunes
    val uniforme_completo: String, // C, NC
    val transporte_adecuado: String, // C, NC
    val puntualidad: String, // C, NC
    
    // Responsables
    val responsable_registro_id: Int,
    val responsable_registro_nombre: String,
    val responsable_supervision_id: Int,
    val responsable_supervision_nombre: String,
    
    // Observaciones
    val observaciones: String? = null,
    val accion_correctiva: String? = null,
    val producto_rechazado: Boolean = false,
    
    val timestamp_creacion: String? = null,
    val timestamp_modificacion: String? = null
)

data class RecepcionMercaderiaRequest(
    val mes: Int,
    val anio: Int,
    val fecha: String,
    val hora: String,
    val tipo_control: String,
    val proveedor_id: Int,
    val nombre_proveedor: String,
    val producto_id: Int,
    val nombre_producto: String,
    val cantidad_solicitada: Double? = null,
    val peso_unidad_recibido: Double,
    val unidad_medida: String,
    val estado_producto: String? = null,
    val conformidad_integridad_producto: String? = null,
    val registro_sanitario_vigente: Boolean? = null,
    val fecha_vencimiento_producto: String? = null,
    val evaluacion_vencimiento: String? = null,
    val conformidad_empaque_primario: String? = null,
    val uniforme_completo: String,
    val transporte_adecuado: String,
    val puntualidad: String,
    val responsable_registro_id: Int,
    val responsable_registro_nombre: String,
    val responsable_supervision_id: Int,
    val responsable_supervision_nombre: String,
    val observaciones: String? = null,
    val accion_correctiva: String? = null,
    val producto_rechazado: Boolean = false
)

enum class TipoControlRecepcion(val valor: String) {
    FRUTAS_VERDURAS("FRUTAS_VERDURAS"),
    ABARROTES("ABARROTES")
}

enum class EvaluacionCalidad(val valor: String) {
    EXCELENTE("EXCELENTE"),
    REGULAR("REGULAR"),
    PESIMO("PESIMO")
}

enum class Conformidad(val valor: String) {
    CONFORME("C"),
    NO_CONFORME("NC")
}
