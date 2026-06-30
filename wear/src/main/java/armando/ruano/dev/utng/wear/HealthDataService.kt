package armando.ruano.dev.utng.wear

import android.content.Context
import android.util.Log
import androidx.health.services.client.HealthServices
import androidx.health.services.client.PassiveListenerService
import androidx.health.services.client.data.DataPointContainer
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.PassiveListenerConfig
import androidx.health.services.client.data.SampleDataPoint
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.runBlocking

class HealthDataService : PassiveListenerService() {

    private lateinit var sender: WearDataSender

    override fun onCreate() {
        super.onCreate()
        sender = WearDataSender(applicationContext)
        Log.d("HEALTH_TEST", "Service onCreate")
    }

    override fun onNewDataPointsReceived(dataPoints: DataPointContainer) {
        val puntos = dataPoints.getData(DataType.HEART_RATE_BPM)
            .filterIsInstance<SampleDataPoint<Double>>()

        if (puntos.isEmpty()) return

        val bpm = puntos.last().value.toInt()
        Log.d("HEALTH_TEST", "FC recibida: $bpm — enviando síncronamente")

        // runBlocking bloquea el hilo hasta que el envío termina,
        // ANTES de que el sistema destruya el Service
        runBlocking {
            try {
                sender.enviarFC(bpm)
                Log.d("HEALTH_TEST", "Envío completado: $bpm")
            } catch (e: Exception) {
                Log.e("HEALTH_TEST", "Error enviando: ${e.message}", e)
            }
        }
    }

    override fun onDestroy() {
        Log.d("HEALTH_TEST", "Service onDestroy")
        super.onDestroy()
    }

    companion object {
        suspend fun registrar(context: Context) {
            val config = PassiveListenerConfig.builder()
                .setDataTypes(setOf(DataType.HEART_RATE_BPM))
                .build()

            HealthServices.getClient(context)
                .passiveMonitoringClient
                .setPassiveListenerServiceAsync(HealthDataService::class.java, config)
                .await()

            Log.d("HEALTH_TEST", "PassiveListener registrado OK")
        }
    }
}