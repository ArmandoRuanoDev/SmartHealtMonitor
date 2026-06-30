package armando.ruano.dev.utng.smarthealthmonitor.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import armando.ruano.dev.utng.smarthealthmonitor.data.SmartHealthRepository
import armando.ruano.dev.utng.smarthealthmonitor.data.db.LecturaFC
import armando.ruano.dev.utng.smarthealthmonitor.data.models.MockData
import kotlinx.coroutines.flow.*
class DashboardViewModel : ViewModel() {

    // FC: viene del wearable real vía Repository.
    // Si es 0 (sin dato aún), usar valor simulado.
    val fc: StateFlow<Int> = SmartHealthRepository.fcFlow
        .map { if (it == 0) MockData.fcActual else it }
        .stateIn(
            scope          = viewModelScope,
            started        = SharingStarted.WhileSubscribed(5_000),
            initialValue   = MockData.fcActual
        )

    val pasos: StateFlow<Int> = SmartHealthRepository.pasosFlow
        .map { if (it == 0) MockData.pasosActual else it }
        .stateIn(
            scope        = viewModelScope,
            started      = SharingStarted.WhileSubscribed(5_000),
            initialValue = MockData.pasosActual
        )
    val historial = MockData.historialFC  // TODO S7: Room
}
