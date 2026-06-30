package armando.ruano.dev.utng.smarthealthmonitor.data.models

import armando.ruano.dev.utng.smarthealthmonitor.data.db.LecturaFC

object MockData {
    val historialFC = listOf(
        LecturaFC(id = 1, valorBpm = 78, hora = "11:00"),
        LecturaFC(id = 2, valorBpm = 82, hora = "10:30"),
        // ...
    )
    var fcActual = 78
    var pasosActual = 4250
}