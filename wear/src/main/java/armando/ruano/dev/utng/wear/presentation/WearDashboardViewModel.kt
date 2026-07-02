package armando.ruano.dev.utng.wear.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import armando.ruano.dev.utng.wear.data.db.LecturaFC
import armando.ruano.dev.utng.wear.data.db.SmartHealthRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class WearDashboardViewModel : ViewModel() {

    // Reutiliza el mismo Repository del módulo app
    val fc: StateFlow<Int> = SmartHealthRepository.fcFlow
        .map { if (it == 0) 110 else it }  // valor por defecto
        .stateIn(viewModelScope,
            SharingStarted.WhileSubscribed(5_000), 72)

    val historial: StateFlow<List<LecturaFC>> =
        SmartHealthRepository.obtenerHistorial()
            .stateIn(viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList())

}
