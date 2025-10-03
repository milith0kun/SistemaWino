package com.example.sistemadecalidad.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sistemadecalidad.data.api.NetworkModule
import com.example.sistemadecalidad.data.repository.AuthRepository
import com.example.sistemadecalidad.data.repository.FichadoRepository
import com.example.sistemadecalidad.ui.screens.analitica.AnaliticaScreen
import com.example.sistemadecalidad.ui.screens.dashboard.DashboardScreen
import com.example.sistemadecalidad.ui.screens.historial.HistorialScreen
import com.example.sistemadecalidad.ui.screens.login.LoginScreen
import com.example.sistemadecalidad.ui.screens.marcaciones.MarcacionesScreen
import com.example.sistemadecalidad.ui.screens.settings.NetworkSettingsScreen
import com.example.sistemadecalidad.ui.screens.settings.LocationSettingsScreen
import com.example.sistemadecalidad.ui.screens.welcome.WelcomeScreen
import com.example.sistemadecalidad.ui.screens.haccp.HaccpMenuScreen
import com.example.sistemadecalidad.ui.screens.haccp.RecepcionMercaderiaScreen
import com.example.sistemadecalidad.ui.screens.haccp.ControlCoccionScreen
import com.example.sistemadecalidad.ui.screens.haccp.LavadoFrutasScreen
import com.example.sistemadecalidad.ui.screens.haccp.LavadoManosScreen
import com.example.sistemadecalidad.ui.screens.haccp.TemperaturaCamarasScreen
import com.example.sistemadecalidad.ui.screens.haccp.RecepcionAbarrotesScreen
import com.example.sistemadecalidad.data.local.PreferencesManager
import com.example.sistemadecalidad.ui.viewmodel.AuthViewModel
import com.example.sistemadecalidad.ui.viewmodel.FichadoViewModel
import com.google.gson.Gson

/**
 * Configuración principal de navegación para la aplicación HACCP
 * Temporalmente con instanciación manual de ViewModels
 */
@Composable
fun HaccpNavigation(
    navController: NavHostController,
    startDestination: String = NavigationDestinations.WELCOME
) {
    // Crear instancias temporales de dependencias
    val context = LocalContext.current
    val gson = Gson()
    val preferencesManager = PreferencesManager(context, gson)
    val networkModule = NetworkModule
    val apiService = networkModule.provideApiService(
        networkModule.provideRetrofit(
            networkModule.provideOkHttpClient(
                networkModule.provideHttpLoggingInterceptor()
            ),
            networkModule.provideGson(),
            context
        )
    )
    val authRepository = AuthRepository(apiService)
    val fichadoRepository = FichadoRepository(apiService)
    val haccpRepository = com.example.sistemadecalidad.data.repository.HaccpRepository(apiService)
    val authViewModel = AuthViewModel(authRepository, preferencesManager)
    val fichadoViewModel = FichadoViewModel(fichadoRepository, preferencesManager)
    val haccpViewModel = com.example.sistemadecalidad.ui.viewmodel.HaccpViewModel(haccpRepository, preferencesManager)
    
    // Observar el estado de autenticación (solo para lectura, sin redirecciones automáticas)
    val isAuthenticated by authViewModel.isAuthenticated.collectAsStateWithLifecycle()
    
    // NO USAR LaunchedEffect para redirecciones automáticas
    // El logout manual debe manejarse directamente en las pantallas
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Pantalla de bienvenida
        composable(NavigationDestinations.WELCOME) {
            WelcomeScreen(
                onNavigateToLogin = {
                    navController.navigate(NavigationDestinations.LOGIN) {
                        popUpTo(NavigationDestinations.WELCOME) { inclusive = true }
                    }
                }
            )
        }
        
        // Pantalla de login
        composable(NavigationDestinations.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(NavigationDestinations.DASHBOARD) {
                        popUpTo(NavigationDestinations.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToNetworkSettings = {
                    navController.navigate(NavigationDestinations.NETWORK_SETTINGS)
                },
                viewModel = authViewModel
            )
        }
        
        // Pantalla principal - Dashboard
        composable(NavigationDestinations.DASHBOARD) {
            DashboardScreen(
                onNavigateToAnalitica = {
                    navController.navigate(NavigationDestinations.ANALITICA)
                },
                onNavigateToMarcaciones = {
                    navController.navigate(NavigationDestinations.MARCACIONES)
                },
                onNavigateToHistorial = {
                    navController.navigate(NavigationDestinations.HISTORIAL)
                },
                onNavigateToHaccp = {
                    navController.navigate(NavigationDestinations.HACCP_MENU)
                },
                onLogout = {
                    navController.navigate(NavigationDestinations.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                authViewModel = authViewModel,
                fichadoViewModel = fichadoViewModel
            )
        }
        
        // Pantalla de marcaciones (fichado)
        composable(NavigationDestinations.MARCACIONES) {
            MarcacionesScreen(
                fichadoViewModel = fichadoViewModel,
                onNavigateToDashboard = {
                    navController.navigate(NavigationDestinations.DASHBOARD) {
                        popUpTo(NavigationDestinations.DASHBOARD) { inclusive = true }
                    }
                },
                onNavigateToHistorial = {
                    navController.navigate(NavigationDestinations.HISTORIAL)
                },
                onNavigateToHaccp = {
                    navController.navigate(NavigationDestinations.HACCP_MENU)
                },
                onNavigateToLocationSettings = {
                    navController.navigate(NavigationDestinations.LOCATION_SETTINGS)
                }
            )
        }
        
        // Pantalla de historial
        composable(NavigationDestinations.HISTORIAL) {
            HistorialScreen(
                fichadoViewModel = fichadoViewModel,
                authViewModel = authViewModel,
                onNavigateToDashboard = {
                    navController.navigate(NavigationDestinations.DASHBOARD) {
                        popUpTo(NavigationDestinations.DASHBOARD) { inclusive = true }
                    }
                },
                onNavigateToMarcaciones = {
                    navController.navigate(NavigationDestinations.MARCACIONES)
                },
                onNavigateToHaccp = {
                    navController.navigate(NavigationDestinations.HACCP_MENU)
                },
                onLogout = {
                    navController.navigate(NavigationDestinations.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        
        // Pantalla de analítica de datos
        composable(NavigationDestinations.ANALITICA) {
            AnaliticaScreen(fichadoViewModel = fichadoViewModel)
        }
        
        // Pantalla de configuración de red
        composable(NavigationDestinations.NETWORK_SETTINGS) {
            NetworkSettingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        // Menú principal de formularios HACCP
        composable(NavigationDestinations.HACCP_MENU) {
            HaccpMenuScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToForm = { formId ->
                    navController.navigate(formId)
                }
            )
        }
        
        // Pantalla de configuración de ubicación
        composable(NavigationDestinations.LOCATION_SETTINGS) {
            LocationSettingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        // Formularios HACCP individuales
        composable(NavigationDestinations.RECEPCION_MERCADERIA) {
            RecepcionMercaderiaScreen(
                haccpViewModel = haccpViewModel,
                preferencesManager = preferencesManager,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(NavigationDestinations.CONTROL_COCCION) {
            ControlCoccionScreen(
                haccpViewModel = haccpViewModel,
                preferencesManager = preferencesManager,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(NavigationDestinations.LAVADO_FRUTAS) {
            LavadoFrutasScreen(
                haccpViewModel = haccpViewModel,
                preferencesManager = preferencesManager,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(NavigationDestinations.LAVADO_MANOS) {
            LavadoManosScreen(
                haccpViewModel = haccpViewModel,
                preferencesManager = preferencesManager,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(NavigationDestinations.TEMPERATURA_CAMARAS) {
            TemperaturaCamarasScreen(
                haccpViewModel = haccpViewModel,
                preferencesManager = preferencesManager,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(NavigationDestinations.RECEPCION_ABARROTES) {
            RecepcionAbarrotesScreen(
                haccpViewModel = haccpViewModel,
                preferencesManager = preferencesManager,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}