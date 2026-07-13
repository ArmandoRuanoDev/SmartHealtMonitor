package ruano.dev.tv.data.models

import ruano.dev.tv.data.db.LecturaFC

// Datos de prueba para desarrollo (mock data)
object MockData {
    val historialFC = listOf(
        LecturaFC(valorBpm = 78, hora = "11:00", esNormal = true),
        LecturaFC(valorBpm = 82, hora = "10:30", esNormal = true),
        LecturaFC(valorBpm = 76, hora = "10:00", esNormal = true),
        LecturaFC(valorBpm = 95, hora = "09:30", esNormal = false),  // fuera de rango
        LecturaFC(valorBpm = 71, hora = "09:00", esNormal = true),
        LecturaFC(valorBpm = 80, hora = "08:30", esNormal = true),
        LecturaFC(valorBpm = 74, hora = "08:00", esNormal = true)
    )
    var fcActual = 110
    var pasosActual = 4250
}
