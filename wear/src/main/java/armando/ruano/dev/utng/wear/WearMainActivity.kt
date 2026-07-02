package armando.ruano.dev.utng.wear

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import armando.ruano.dev.utng.wear.presentation.SmartHealthWearNavGraph
import armando.ruano.dev.utng.wear.presentation.WearDashboardScreen
import armando.ruano.dev.utng.wear.presentation.theme.SmartHealthWearTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class WearMainActivity : ComponentActivity() {

    // Scope independiente del ciclo de vida de la Activity
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val permissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions.values.all { it }) {
                registrarServicio()
            } else {
                Log.e("HEALTH_TEST", "Permisos denegados: $permissions")
                permissions.forEach { (permiso, concedido) ->
                    if (!concedido) {
                        val puedeVolverAPedir = shouldShowRequestPermissionRationale(permiso)
                        Log.e("HEALTH_TEST", "$permiso denegado. ¿Puede reintentarse?: $puedeVolverAPedir")
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // UI mínima para que la Activity no sea destruida
        setContent {
            SmartHealthWearTheme {
                // TODO Ej.02: reemplazar con WearNavGraph
                SmartHealthWearNavGraph()
            }
        }


        val permisos = arrayOf(
            Manifest.permission.BODY_SENSORS,
            Manifest.permission.ACTIVITY_RECOGNITION
        )

        val faltantes = permisos.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        Log.d("HEALTH_TEST", "Permisos faltantes: ${faltantes.size}")

        if (faltantes.isEmpty()) {
            registrarServicio()
        } else {
            permissionLauncher.launch(faltantes.toTypedArray())
        }
    }

    private fun registrarServicio() {
        // serviceScope en lugar de lifecycleScope
        serviceScope.launch {
            try {
                Log.d("HEALTH_TEST", "Iniciando registro")
                HealthDataService.registrar(applicationContext)
                Log.d("HEALTH_TEST", "Registro completado")
            } catch (e: Exception) {
                Log.e("HEALTH_TEST", "Error en registro: ${e.message}", e)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()  // limpiar al destruir definitivamente
    }
}