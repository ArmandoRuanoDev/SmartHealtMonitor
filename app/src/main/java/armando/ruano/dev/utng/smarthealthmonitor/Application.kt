package armando.ruano.dev.utng.smarthealthmonitor

import android.app.Application
import armando.ruano.dev.utng.smarthealthmonitor.data.SmartHealthRepository

class SmartHealthApp : Application() {
    override fun onCreate() {
        super.onCreate()
        SmartHealthRepository.init(this)  // inicializar Room
    }
}