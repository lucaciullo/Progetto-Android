package com.example.hcr0_4.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import java.time.LocalDate

@Database(
    entities = [
        Cartella::class,
        Referto::class,
        Terapia::class,
        Posologia::class,
        Somministrazione::class,
        ParametroVitale::class,
        Misurazione::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class DatabaseHCR : RoomDatabase() {
    abstract fun cartellaDao(): CartellaDao
    abstract fun refertoDao(): RefertoDao
    abstract fun terapiaDao(): TerapiaDao
    abstract fun posologiaDao(): PosologiaDao
    abstract fun somministrazioneDao(): SomministrazioneDao
    abstract fun parametroVitaleDao(): ParametroVitaleDao
    abstract fun misurazioneDao(): MisurazioneDao

    fun generateDatesBasedOnFrequency(startDate: LocalDate, endDate: LocalDate, frequency: String): List<LocalDate> {
        val dates = mutableListOf<LocalDate>()
        var currentDate = startDate

        while (currentDate <= endDate) {
            dates.add(currentDate)

            currentDate = when (frequency) {
                "giornaliera" -> currentDate.plusDays(1)
                "settimanale" -> currentDate.plusWeeks(1)
                "mensile" -> currentDate.plusMonths(1)
                "annuale" -> currentDate.plusYears(1)
                else -> throw IllegalArgumentException("Frequenza non supportata: $frequency")
            }
        }

        return dates
    }

    companion object {
        @Volatile
        private var Instance: DatabaseHCR? = null

        fun getDatabase(context: Context): DatabaseHCR {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, DatabaseHCR::class.java, "hcr_database")
                    .build()
            }
        }
    }

    suspend fun initializeDatabase(context: Context) {
        val database = getDatabase(context)
        val parametroVitaleDao = database.parametroVitaleDao()

        // Check if the step count parameter exists
        val stepCountParameter = parametroVitaleDao.getParametroVitaleByName("Passi")

            // Insert the step count parameter
            val passiParametro = ParametroVitale(
                nomeParametro = "Passi",
                unitaMisura = "passi",
                valoriAccumulati = true
            )
            parametroVitaleDao.insertParametroVitale(passiParametro)
        
    }
}