package armando.ruano.dev.utng.smarthealthmonitor.data

import android.content.Context
import armando.ruano.dev.utng.smarthealthmonitor.data.db.LecturaFC
import armando.ruano.dev.utng.smarthealthmonitor.data.db.LecturaFCDao
import armando.ruano.dev.utng.smarthealthmonitor.data.db.SmartHealthDB
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Repositorio singleton que centraliza los datos de salud.
 * El WearListenerService escribe aquí.
 * El ViewModel lee de aquí.
 */
object SmartHealthRepository {
    private val _fcFlow = MutableStateFlow(0)
    val fcFlow: StateFlow<Int> = _fcFlow.asStateFlow()

    private val _pasosFlow = MutableStateFlow(0)
    val pasosFlow: StateFlow<Int> = _pasosFlow.asStateFlow()

    fun actualizarPasos(pasos: Int) {
        _pasosFlow.value = pasos
    }
    private var dao: LecturaFCDao? = null

    fun init(context: Context) {
        dao = SmartHealthDB.getDatabase(context).lecturaDao()
    }

    suspend fun actualizarFC(bpm: Int) {
        _fcFlow.value = bpm
        val horaActual = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        dao?.insertar(LecturaFC(valorBpm = bpm, hora = horaActual, esNormal = bpm in 60..100))
    }

    // Flow del historial desde Room
    fun obtenerHistorial(): Flow<List<LecturaFC>> =
        dao?.obtenerUltimas() ?: emptyFlow()
}
