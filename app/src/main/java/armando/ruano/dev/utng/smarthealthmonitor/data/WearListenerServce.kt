package armando.ruano.dev.utng.smarthealthmonitor.data

import android.util.Log
import armando.ruano.dev.utng.smarthealthmonitor.data.models.SmartHealthRepository
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService

class WearListenerService : WearableListenerService() {

    companion object {
        const val PATH_FC    = "/smarthealthmonitor/fc"
        const val PATH_PASOS = "/smarthealthmonitor/pasos"
        private const val TAG = "WearListener"
    }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        val data = String(messageEvent.data)
        val path = messageEvent.path
        // Este log debe aparecer cuando el reloj envía datos
        Log.d("HEALTH_TEST", "TELÉFONO recibió: path=$path, data=$data")

        when (path) {
            PATH_FC -> {
                val bpm = data.toIntOrNull() ?: return
                Log.d("HEALTH_TEST", "TELÉFONO actualizando FC: $bpm")
                SmartHealthRepository.actualizarFC(bpm)
            }
            PATH_PASOS -> {
                val pasos = data.toIntOrNull() ?: return
                SmartHealthRepository.actualizarPasos(pasos)
            }
            else -> Log.w("HEALTH_TEST", "Path desconocido: $path")
        }
    }
}
