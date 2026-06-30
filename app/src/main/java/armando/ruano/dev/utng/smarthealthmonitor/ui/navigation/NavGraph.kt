package armando.ruano.dev.utng.smarthealthmonitor.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import armando.ruano.dev.utng.smarthealthmonitor.LoginScreen
import armando.ruano.dev.utng.smarthealthmonitor.ui.screens.DashboardScreen
import armando.ruano.dev.utng.smarthealthmonitor.ui.screens.HistorialScreen
import armando.ruano.dev.utng.smarthealthmonitor.ui.theme.SmartHealthMonitorTheme

@Composable
fun SmartHealthNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController    = navController,
        startDestination = Screen.Login.route
    ) {
        // ── Login ──────────────────────────────────────
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) {
                            inclusive = true  // eliminar Login del back stack
                        }
                    }
                }
            )
        }
        // ── Dashboard ──────────────────────────────────
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onHistorialClick = {
                    navController.navigate(Screen.Historial.route)
                },
                onAlertClick = {
                    navController.navigate(Screen.Alerta.route)
                }
            )
        }
        // ── Historial ──────────────────────────────────
        composable(Screen.Historial.route) {
            HistorialScreen(
                onBack = { navController.popBackStack() }
            )
        }

        // ── Alerta ─────────────────────────────────────
        composable(Screen.Alerta.route) {
            PantallaEnConstruccion(
                titulo = "Enviar alerta",
                onBack = { navController.popBackStack() }
            )
        }
    }
}

// Pantalla temporal para destinos no implementados aún
@Composable
fun PantallaEnConstruccion(titulo: String, onBack: () -> Unit) {
    SmartHealthMonitorTheme {
        Scaffold(topBar = {
            TopAppBar(
                title = { Text(titulo) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar")
                    }
                }
            )
        }) { pad ->
            Box(Modifier.fillMaxSize().padding(pad),
                contentAlignment = Alignment.Center) {
                Text("Próximamente: $titulo",
                    style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}
