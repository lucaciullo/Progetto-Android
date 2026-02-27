package com.example.hcr0_4.ui.schermate.menuCartelle


import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.hcr0_4.data.Cartella
import com.example.hcr0_4.data.*
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import java.io.File

/**
 * ViewModel to validate and insert items in the Room database.
 */
class InserisciCartellaViewModel(
    private val cartella: CartelleRepository
) : ViewModel() {

    var cartellaUiState by mutableStateOf(CartellaUiState())
        private set

    fun updateUiStateCartella(cartellaDetails: CartellaDetails) {
        cartellaUiState = CartellaUiState(cartellaDetails = cartellaDetails, isEntryValid = validateCartellaInput())
    }

    suspend fun saveCartella(appContext: Context) {
        if (validateCartellaInput()) {
            val cartellaEntity = cartellaUiState.cartellaDetails.toCartella()
            cartella.insertCartella(cartellaEntity)

            // Crea cartella nel filesystem
            createPhysicalFolder(appContext, cartellaEntity.nome)
        }
    }

    private fun validateCartellaInput(): Boolean {
        // Validazione logica se necessario
        return cartellaUiState.cartellaDetails.nome.isNotBlank() // Valida che il nome non sia vuoto
    }

    private fun createPhysicalFolder(context: Context, folderName: String) {
        // Percorso alla cartella principale nella memoria esterna pubblica
        val baseDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Hcr/Documenti")

        // Assicurati che la directory di base esista
        if (!baseDir.exists()) {
            baseDir.mkdirs()
        }

        // Crea la nuova cartella
        val newFolder = File(baseDir, folderName)
        if (!newFolder.exists()) {
            val created = newFolder.mkdir()
            if (created) {
                Log.d("HcrCartella", "Cartella creata con successo: ${newFolder.absolutePath}")
            } else {
                Log.d("HcrCartella", "Errore nella creazione della cartella: ${newFolder.absolutePath}")
            }
        } else {
            Log.d("HcrCartella", "La cartella esiste già: ${newFolder.absolutePath}")
        }
    }
}





data class CartellaUiState(
    val cartellaDetails: CartellaDetails = CartellaDetails(),
    val isEntryValid: Boolean = false
)

data class CartellaDetails(
    val idCartella: Int = 0,
    val nome: String = ""

    )


fun CartellaDetails.toCartella(): Cartella = Cartella(
    idCartella = idCartella,
    nome = nome

)

fun Cartella.toCartellaUiState(isEntryValid: Boolean = false): CartellaUiState = CartellaUiState(
    cartellaDetails = this.toCartellaDetails(),
    isEntryValid = isEntryValid
)

fun Cartella.toCartellaDetails(): CartellaDetails = CartellaDetails(
    idCartella = idCartella,
    nome = nome
)