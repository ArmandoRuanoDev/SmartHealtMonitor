package armando.ruano.dev.utng.wear.data.db

import android.content.Context
import androidx.room.*

@Database(
    entities = [armando.ruano.dev.utng.wear.data.db.LecturaFC::class],
    version  = 1,
    exportSchema = false  // true en producción para migraciones
)
abstract class SmartHealthDB : RoomDatabase() {
    abstract fun lecturaDao(): armando.ruano.dev.utng.wear.data.db.LecturaFCDao

    companion object {
        @Volatile
        private var INSTANCE: SmartHealthDB? = null

        fun getDatabase(context: Context): SmartHealthDB {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    SmartHealthDB::class.java,
                    "smarthealthmonitor_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}

