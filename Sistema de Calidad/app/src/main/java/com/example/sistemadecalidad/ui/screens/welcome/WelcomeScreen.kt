package com.example.sistemadecalidad.ui.screens.welcome

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Pantalla de bienvenida - Primera pantalla que ve el usuario
 * Diseño simple sin fondos especiales, solo botón para ir al login
 */
@Composable
fun WelcomeScreen(
    onNavigateToLogin: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Espacio superior para bajar el contenido
        Spacer(modifier = Modifier.weight(0.3f))
        
        // Título principal centrado
        Text(
            text = "Bienvenido, seleccione el tipo de login:",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        // Logo simple del sistema (por ahora solo texto)
        Card(
            modifier = Modifier
                .size(120.dp)
                .padding(bottom = 48.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "HACCP",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
        
        // Botón principal - único botón disponible
        Button(
            onClick = onNavigateToLogin,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = "Usuario y Contraseña",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
        
        // Espacio inferior para mantener el balance
        Spacer(modifier = Modifier.weight(0.7f))
        
        // Enlaces inferiores
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            TextButton(onClick = { /* TODO: Implementar ayuda */ }) {
                Text(
                    text = "¿Necesitas ayuda?",
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            TextButton(onClick = { /* TODO: Implementar privacidad */ }) {
                Text(
                    text = "Declaración de privacidad",
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}