package com.example.sistemadecalidad

import android.app.Application
import android.util.Log
import com.example.sistemadecalidad.utils.NetworkConfig
// import dagger.hilt.android.HiltAndroidApp

/**
 * Clase principal de la aplicación
 * Inicializa configuración de red con AWS Production
 */
// @HiltAndroidApp
class HaccpApplication : Application() {
    
    companion object {
        private const val TAG = "HaccpApplication"
    }
    
    override fun onCreate() {
        super.onCreate()
        
        // Inicializar configuración de red (AWS Production por defecto)
        initializeNetworkConfig()
    }
    
    /**
     * Inicializa la configuración de red con AWS Production
     */
    private fun initializeNetworkConfig() {
        try {
            Log.d(TAG, "🔧 Inicializando configuración de red...")
            NetworkConfig.initialize(this)
            Log.d(TAG, "✅ Red configurada: ${NetworkConfig.getCurrentUrl()}")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error inicializando red: ${e.message}")
        }
    }
}