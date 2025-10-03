package com.example.sistemadecalidad.data.model

data class Producto(
    val id: Int = 0,
    val nombre: String,
    val categoria_id: Int? = null,
    val unidad_medida: String, // KG, UNIDAD, CAJA, GRAMOS, LITROS, PAQUETE, LATA
    val descripcion: String? = null,
    val requiere_refrigeracion: Boolean = false,
    val requiere_registro_sanitario: Boolean = false,
    val temperatura_almacenamiento: String? = null,
    val activo: Boolean = true
)

data class ProductoRequest(
    val nombre: String,
    val categoria_id: Int? = null,
    val unidad_medida: String,
    val descripcion: String? = null,
    val requiere_refrigeracion: Boolean = false,
    val requiere_registro_sanitario: Boolean = false,
    val temperatura_almacenamiento: String? = null
)

enum class UnidadMedida(val valor: String) {
    KG("KG"),
    UNIDAD("UNIDAD"),
    CAJA("CAJA"),
    GRAMOS("GRAMOS"),
    LITROS("LITROS"),
    PAQUETE("PAQUETE"),
    LATA("LATA")
}
