package com.example.sistemadecalidad.utils

/**
 * Configuración de producción para el despliegue
 * Aquí puedes cambiar fácilmente la IP y puerto del servidor
 */
object ProductionConfig {
    
    // CONFIGURACIÓN DEL SERVIDOR - CAMBIAR AQUÍ PARA DESPLIEGUE
    const val SERVER_IP = "nice-breads-judge.loca.lt"  // Túnel público configurado
    const val SERVER_PORT = ""                         // Puerto incluido en el dominio del túnel
    const val USE_HTTPS = true                         // Túnel público usa HTTPS
    
    /**
     * Obtiene la URL completa del servidor de producción
     */
    fun getServerUrl(): String {
        val protocol = if (USE_HTTPS) "https" else "http"
        return if (SERVER_PORT.isNotBlank()) {
            "$protocol://$SERVER_IP:$SERVER_PORT/"
        } else {
            "$protocol://$SERVER_IP/"
        }
    }
    
    /**
     * Obtiene solo la dirección base (sin protocolo)
     */
    fun getServerAddress(): String {
        return if (SERVER_PORT.isNotBlank()) {
            "$SERVER_IP:$SERVER_PORT"
        } else {
            SERVER_IP
        }
    }
    
    /**
     * Verifica si la configuración es válida
     */
    fun isConfigurationValid(): Boolean {
        return SERVER_IP.isNotBlank() && 
               SERVER_IP != "your-server-ip" &&
               (SERVER_PORT.isBlank() || SERVER_PORT.toIntOrNull() != null)
    }
}