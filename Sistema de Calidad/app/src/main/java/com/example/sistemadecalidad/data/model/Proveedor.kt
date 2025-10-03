package com.example.sistemadecalidad.data.model

data class Proveedor(
    val id: Int = 0,
    val nombre_completo: String,
    val razon_social: String? = null,
    val ruc: String? = null,
    val contacto_nombre: String? = null,
    val contacto_telefono: String? = null,
    val contacto_email: String? = null,
    val direccion: String? = null,
    val tipo_productos: String? = null,
    val calificacion_promedio: Double? = null,
    val activo: Boolean = true,
    val fecha_registro: String? = null,
    val observaciones: String? = null
)

data class ProveedorRequest(
    val nombre_completo: String,
    val razon_social: String? = null,
    val ruc: String? = null,
    val contacto_nombre: String? = null,
    val contacto_telefono: String? = null,
    val contacto_email: String? = null,
    val direccion: String? = null,
    val tipo_productos: String? = null
)
