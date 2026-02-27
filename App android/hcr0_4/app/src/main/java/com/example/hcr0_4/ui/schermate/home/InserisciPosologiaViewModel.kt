    package com.example.hcr0_4.ui.schermate.home

    import android.util.Log
    import androidx.compose.runtime.mutableStateOf
    import androidx.lifecycle.ViewModel
    import com.example.hcr0_4.data.*
    import androidx.compose.runtime.setValue
    import androidx.compose.runtime.getValue
    import androidx.lifecycle.SavedStateHandle
    import com.example.hcr0_4.LocalDateAdapter
    import com.google.gson.GsonBuilder
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.withContext
    import java.time.LocalDate
    import java.time.LocalTime

    /**
     * ViewModel to manage the insertion of Terapia, Posologia, and Somministrazione into the Room database.
     */
class InserisciPosologiaViewModel(
        private val somministrazioneRepository: SomministrazioniRepository,
        private val posologiaRepository: PosologieRepository,
        private val terapiaRepository: TerapieRepository,
        savedStateHandle: SavedStateHandle = SavedStateHandle(),
) : ViewModel() {


        private val somministrazioneGenerator = SomministrazioneGenerator(somministrazioneRepository)

        // Altri metodi...

        private suspend fun generateSomministrazioni(listaPosologie: List<Posologia>) {
            somministrazioneGenerator.generateSomministrazioni(terapiaDetails.toTerapia(), listaPosologie)
        }

    // Crea un'istanza personalizzata di Gson
    val gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter)
        .create()

    val terapiaDetailsString: String = checkNotNull(savedStateHandle[InserisciPosologiaDestination.terapiaDetailsArg])
    var terapiaDetails: TerapiaDetails = gson.fromJson(terapiaDetailsString, TerapiaDetails::class.java)



    var posologiaUiState by mutableStateOf(PosologiaUiState())
        private set






        fun updateUiStatePosologia(posologiaDetailsList: List<PosologiaDetails>) {
            posologiaUiState = posologiaUiState.copy(
                posologiaDetailsList = posologiaDetailsList,
                isEntryValid = validatePosologiaListInput(posologiaDetailsList)
            )
            Log.d("InserisciSomministrazioniViewModel", "updateUiStatePosologia: $posologiaUiState")
        }



suspend fun saveTerapia(terapiaDetails: TerapiaDetails): Long {
    return withContext(Dispatchers.IO) {
        terapiaRepository.insertTerapia(terapiaDetails.toTerapia())
    }
}



    suspend fun saveTerapia(): Long {

        return withContext(Dispatchers.IO) {

                terapiaRepository.insertTerapia(terapiaDetails.toTerapia())

        }
    }

 suspend fun savePosologia(idTerapia: Long, posologiaDetails: PosologiaDetails): Long {

    return withContext(Dispatchers.IO) {
        if (validatePosologiaInput(posologiaDetails)) {
            posologiaRepository.insertPosologia(posologiaDetails.toPosologia(idTerapia))
        } else {
            -1L
        }
    }
}
suspend fun saveAll() {
    return withContext(Dispatchers.IO) {
        val terapiaId = saveTerapia()
        if (terapiaId > 0) {
            val posologieId: List<Long> = posologiaUiState.posologiaDetailsList.mapIndexed { index, posologiaDetails ->
                savePosologia(terapiaId, posologiaDetails)
            }
            Log.d("InserisciSomministrazioniViewModel", "saveAll: $posologieId")
            val listaPosologie = posologiaUiState.posologiaDetailsList.mapIndexed { index, posologiaDetails ->
                posologiaDetails.toPosologia(terapiaId, posologieId[index].toInt())
            }
            Log.d("InserisciSomministrazioniViewModel", "saveAll: $listaPosologie")
            generateSomministrazioni(listaPosologie)

        } else {
        }
    }
}




    private fun validatePosologiaListInput(details: List<PosologiaDetails>): Boolean {

        return details.isNotEmpty() && details.all { validatePosologiaInput(it) }

    }
        private fun validatePosologiaInput(details: PosologiaDetails): Boolean {

            return details.dose > 0.0 &&
                    ((terapiaDetails.frequenza == "Sett." && details.giorniSettimana.isNotEmpty() ) ||
                            terapiaDetails.frequenza == "Giorn." ||
                            terapiaDetails.frequenza == "Pers."

                            )

        }





}

// Data classes and conversion functions


data class PosologiaUiState(
    val posologiaDetailsList: List<PosologiaDetails> = listOf(PosologiaDetails()),
    val isEntryValid: Boolean = false
)
data class PosologiaDetails(
    val idPosologia: Int = 0,
    val idTerapia: Int = 0,
    val dose: Double = 0.0,
    val ora: LocalTime = LocalTime.now(),
    val giorniSettimana: List<String> = listOf("")
)

fun PosologiaDetails.toPosologia(idTerapia: Long, idPosologia: Int = 0): Posologia = Posologia(
    idPosologia = idPosologia,
    idTerapia = idTerapia.toInt(),
    dose = dose,
    oraPosologia = ora,
    giorniSettimana = giorniSettimana.toString()
)

