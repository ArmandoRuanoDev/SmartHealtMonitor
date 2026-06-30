package armando.ruano.dev.utng.wear

import android.content.Context
import android.util.Log
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.tasks.await

class WearDataSender(private val context: Context) {

    suspend fun enviarFC(bpm: Int) {
        Log.d("WearDataSender", "Intentando enviar FC: $bpm")
        enviarMensaje("/smarthealthmonitor/fc", bpm.toString())
    }

    suspend fun enviarPasos(pasos: Int) {
        Log.d("WearDataSender", "Intentando enviar Pasos: $pasos")
        enviarMensaje("/smarthealthmonitor/pasos", pasos.toString())
    }

    private suspend fun enviarMensaje(path: String, data: String) {
        try {
            val capabilityInfo = Wearable.getCapabilityClient(context)
                .getCapability("health_monitor_receiver", CapabilityClient.FILTER_ALL)
                .await()

            val targetNodes = capabilityInfo.nodes.toMutableSet()
            Log.d("WearDataSender", "Nodos con capacidad: ${targetNodes.size}")

            if (targetNodes.isEmpty()) {
                val allNodes = Wearable.getNodeClient(context).connectedNodes.await()
                Log.d("WearDataSender", "Todos los nodos conectados: ${allNodes.size}")
                targetNodes.addAll(allNodes)
            }

            if (targetNodes.isEmpty()) {
                Log.e("WearDataSender", "SIN NODOS — teléfono no detectado")
                return
            }

            targetNodes.forEach { node ->
                Log.d("WearDataSender", "Enviando a: ${node.displayName}")
                Wearable.getMessageClient(context).sendMessage(
                    node.id, path, data.toByteArray()
                ).await()
                Log.d("WearDataSender", "Enviado OK a ${node.displayName}")
            }

        } catch (e: Exception) {
            Log.e("WearDataSender", "Error: ${e.message}", e)
        }
    }
}