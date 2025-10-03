package com.example.sistemadecalidad

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.sistemadecalidad.navigation.HaccpNavigation
import com.example.sistemadecalidad.ui.theme.SistemaDeCalidadTheme
import com.example.sistemadecalidad.data.api.AutoNetworkDetector
import com.example.sistemadecalidad.data.api.NetworkModule
import kotlinx.coroutines.launch
import android.util.Log
// import dagger.hilt.android.AndroidEntryPoint

/**
 * Actividad principal de la aplicación HACCP
 * Con detección automática de red al inicio
 */
// @AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    private val TAG = "MainActivity"
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Inicializar detección automática de red al inicio
        initializeAutoNetworkDetection()
        
        enableEdgeToEdge()
        setContent {
            SistemaDeCalidadTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Navegación principal de la aplicación
                    val navController = rememberNavController()
                    HaccpNavigation(navController = navController)
                }
            }
        }
    }
    
    /**
     * Inicializa la detección automática de red
     * Encuentra automáticamente la mejor URL del servidor disponible
     */
    private fun initializeAutoNetworkDetection() {
        lifecycleScope.launch {
            try {
                Log.d(TAG, "🔍 Iniciando detección automática de red...")
                
                val detector = AutoNetworkDetector(this@MainActivity)
                val bestUrl = detector.detectBestServerUrl()
                
                if (bestUrl != null) {
                    Log.d(TAG, "✅ Servidor encontrado automáticamente: $bestUrl")
                    NetworkModule.setCustomBaseUrl(bestUrl)
                } else {
                    Log.w(TAG, "⚠️ No se pudo detectar servidor automáticamente, usando configuración por defecto")
                    // Usar la configuración por defecto del NetworkModule
                }
                
            } catch (e: Exception) {
                Log.e(TAG, "❌ Error en detección automática de red: ${e.message}")
                // Continuar con configuración por defecto en caso de error
            }
        }
    }
}