package com.example.sistemadecalidad.ui.screens.haccp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioPlaceholderScreen(
    titulo: String,
    descripcion: String,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(titulo) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = descripcion,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                color = androidx.compose.ui.graphics.Color.Gray
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LavadoManosScreen(
    haccpViewModel: com.example.sistemadecalidad.ui.viewmodel.HaccpViewModel,
    preferencesManager: com.example.sistemadecalidad.data.local.PreferencesManager,
    onNavigateBack: () -> Unit
) {
    // Estados para los campos del formulario
    var mes by remember { mutableStateOf(LocalDate.now().monthValue) }
    var anio by remember { mutableStateOf(LocalDate.now().year) }
    var areaEstacion by remember { mutableStateOf("COCINA") }
    var turno by remember { mutableStateOf("MAÑANA") }
    var procedimientoCorrecto by remember { mutableStateOf("C") } // C = Conforme, NC = No Conforme
    var accionesCorrectivas by remember { mutableStateOf("") }
    
    val uiState by haccpViewModel.uiState.collectAsState()
    val usuario by preferencesManager.getUser().collectAsState(initial = null)
    
    var showSuccessDialog by remember { mutableStateOf(false) }
    
    val scrollState = rememberScrollState()
    
    // Validación del formulario
    val isFormValid = areaEstacion.isNotEmpty() && 
                     turno.isNotEmpty() &&
                     // Si es NC, se requiere acciones correctivas
                     (procedimientoCorrecto == "C" || accionesCorrectivas.isNotEmpty())
    
    // Mostrar diálogo de éxito
    LaunchedEffect(uiState.successMessage) {
        if (uiState.successMessage != null) {
            showSuccessDialog = true
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lavado de Manos") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Título
            Text(
                text = "REGISTRO HACCP CONTROL DE LAVADO Y DESINFECCIÓN DE MANOS",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Divider()
            
            // DATOS DEL EMPLEADO (automático del usuario logueado)
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("EMPLEADO QUE SE LAVA LAS MANOS", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    Text("Nombre: ${usuario?.nombreCompleto ?: "..."}")
                    Text("Cargo: ${usuario?.cargo ?: "..."}")
                    Text("Fecha: ${LocalDate.now()}")
                    Text("Hora: ${LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))}")
                }
            }
            
            // DATOS DEL PERÍODO (Auto-generados)
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("DATOS DEL PERÍODO", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("MES: ${getMonthName(mes)}", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                        Text("AÑO: $anio", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                    }
                    Text("(Generado automáticamente)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f))
                }
            }
            
            Divider()
            
            // ÁREA O ESTACIÓN
            Text("ÁREA O ESTACIÓN *", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = areaEstacion == "COCINA",
                    onClick = { areaEstacion = "COCINA" },
                    label = { Text("Cocina") },
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = areaEstacion == "SALON",
                    onClick = { areaEstacion = "SALON" },
                    label = { Text("Salón") },
                    modifier = Modifier.weight(1f)
                )
            }
            
            Divider()
            
            // TURNO
            Text("TURNO *", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = turno == "MAÑANA",
                    onClick = { turno = "MAÑANA" },
                    label = { Text("☀️ Mañana") },
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = turno == "TARDE",
                    onClick = { turno = "TARDE" },
                    label = { Text("🌙 Tarde") },
                    modifier = Modifier.weight(1f)
                )
            }
            
            Divider()
            
            // PROCEDIMIENTO DE LAVADO
            Text("PROCEDIMIENTO DE LAVADO DE MANOS", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Text(
                "Evaluar si el personal siguió correctamente el procedimiento de lavado y desinfección de manos",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (procedimientoCorrecto == "C") 
                        MaterialTheme.colorScheme.primaryContainer 
                    else 
                        MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        "Seleccione la evaluación:",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        FilterChip(
                            selected = procedimientoCorrecto == "C",
                            onClick = { procedimientoCorrecto = "C" },
                            label = { 
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("✓ CONFORME", fontWeight = FontWeight.Bold)
                                    Text("Procedimiento correcto", style = MaterialTheme.typography.bodySmall)
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF4CAF50)
                            )
                        )
                        FilterChip(
                            selected = procedimientoCorrecto == "NC",
                            onClick = { procedimientoCorrecto = "NC" },
                            label = { 
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("✗ NO CONFORME", fontWeight = FontWeight.Bold)
                                    Text("Requiere repetir", style = MaterialTheme.typography.bodySmall)
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFFEF5350)
                            )
                        )
                    }
                }
            }
            
            Divider()
            
            // ACCIONES CORRECTIVAS
            Text("ACCIÓN CORRECTIVA", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            
            val requiereAcciones = procedimientoCorrecto == "NC"
            
            if (requiereAcciones) {
                Text(
                    "⚠️ Requerido: Se detectó un proceso NO CONFORME",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            OutlinedTextField(
                value = accionesCorrectivas,
                onValueChange = { accionesCorrectivas = it },
                label = { Text(if (requiereAcciones) "Descripción de acciones correctivas *" else "Descripción de acciones correctivas (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5,
                isError = requiereAcciones && accionesCorrectivas.isEmpty()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Botón de registro
            Button(
                onClick = {
                    usuario?.let { user ->
                        haccpViewModel.registrarLavadoManos(
                            empleadoId = user.id,
                            area = areaEstacion,
                            turno = turno,
                            firma = user.nombreCompleto,
                            procedimientoCorrecto = procedimientoCorrecto,
                            accionCorrectiva = accionesCorrectivas.ifEmpty { null },
                            supervisorId = null
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = isFormValid && !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text("Registrar Control de Lavado de Manos")
            }
            
            // Mostrar error si existe
            if (uiState.error != null) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Error: ${uiState.error}",
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
    
    // Diálogo de éxito
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
                haccpViewModel.clearMessages()
                onNavigateBack()
            },
            title = { Text("✓ Registro Exitoso") },
            text = { Text(uiState.successMessage ?: "Control de lavado de manos registrado correctamente") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSuccessDialog = false
                        haccpViewModel.clearMessages()
                        onNavigateBack()
                    }
                ) {
                    Text("Aceptar")
                }
            }
        )
    }
}

/**
 * Función helper para obtener el nombre del mes
 */
private fun getMonthName(month: Int): String {
    return when(month) {
        1 -> "Enero"
        2 -> "Febrero"
        3 -> "Marzo"
        4 -> "Abril"
        5 -> "Mayo"
        6 -> "Junio"
        7 -> "Julio"
        8 -> "Agosto"
        9 -> "Septiembre"
        10 -> "Octubre"
        11 -> "Noviembre"
        12 -> "Diciembre"
        else -> month.toString()
    }
}
