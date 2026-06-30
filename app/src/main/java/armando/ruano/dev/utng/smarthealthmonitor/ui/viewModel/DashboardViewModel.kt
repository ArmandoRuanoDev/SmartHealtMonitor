package armando.ruano.dev.utng.smarthealthmonitor.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import armando.ruano.dev.utng.smarthealthmonitor.data.SmartHealthRepository
import armando.ruano.dev.utng.smarthealthmonitor.data.db.LecturaFC
import armando.ruano.dev.utng.smarthealthmonitor.data.models.MockData
import kotlinx.coroutines.flow.*
class DashboardViewModel : ViewModel() {

    val fc: StateFlow<Int> = SmartHealthRepository.fcFlow
        .map { if (it == 0) MockData.fcActual else it }
        .stateIn(viewModelScope,
            SharingStarted.WhileSubscribed(5_000), MockData.fcActual)

    val pasos: StateFlow<Int> = SmartHealthRepository.pasosFlow
        .map { if (it == 0) MockData.pasosActual else it }
        .stateIn(viewModelScope,
            SharingStarted.WhileSubscribed(5_000), MockData.pasosActual)

    // ← NUEVO: historial desde Room (Flow reactivo)
    val historial: StateFlow<List<LecturaFC>> =
        SmartHealthRepository.obtenerHistorial()
            .stateIn(
                scope        = viewModelScope,
                started      = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )
}

