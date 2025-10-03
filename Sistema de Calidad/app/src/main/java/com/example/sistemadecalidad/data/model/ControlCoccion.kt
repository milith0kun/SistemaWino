package com.example.sistemadecalidad.data.model

data class ControlCoccion(
    val id: Int = 0,
    val mes: Int,
    val anio: Int,
    val dia: Int,
    val fecha: String,
    val hora: String,
    val producto_cocinar: String, // POLLO, CARNE, PESCADO, HAMBURGUESA, PIZZA
    val proceso_coccion: String, // H: Horno, P: Plancha, C: Cocina
    val temperatura_coccion: Double,
    val tiempo_coccion_minutos: Int,
    val conformidad: String, // C, NC
    val accion_correctiva: String? = null,
    val responsable_id: Int,
    val responsable_nombre: String,
    val timestamp_creacion: String? = null
)

/**
 * Request simplificado para control de cocción
 * El responsable se obtiene automáticamente del token JWT
 * La fecha/hora se genera automáticamente en el backend
 */
data class ControlCoccionRequest(
    val producto_cocinar: String, // POLLO, CARNE, PESCADO, HAMBURGUESA, PIZZA
    val proceso_coccion: String, // H: Horno, P: Plancha, C: Cocina
    val temperatura_coccion: Double, // Debe ser > 80°C
    val tiempo_coccion_minutos: Int,
    val accion_correctiva: String? = null // Obligatoria si temperatura <= 80°C
)

enum class ProductoCocinar(val valor: String, val display: String) {
    POLLO("POLLO", "Pollo"),
    CARNE("CARNE", "Carne"),
    PESCADO("PESCADO", "Pescado"),
    HAMBURGUESA("HAMBURGUESA", "Hamburguesa"),
    PIZZA("PIZZA", "Pizza")
}

enum class ProcesoCoccion(val valor: String, val display: String) {
    HORNO("H", "Horno"),
    PLANCHA("P", "Plancha"),
    COCINA("C", "Cocina")
}
