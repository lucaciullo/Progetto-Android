package com.example.hcr0_4.ui.schermate.menuCartelle

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hcr0_4.data.RefertiRepository
import com.example.hcr0_4.data.Cartella
import com.example.hcr0_4.data.CartelleRepository
import com.example.hcr0_4.data.Posologia
import com.example.hcr0_4.data.PosologieRepository
import com.example.hcr0_4.data.Referto
import com.example.hcr0_4.data.SomministrazioniRepository
import com.example.hcr0_4.data.Terapia
import com.example.hcr0_4.data.TerapieRepository
import com.example.hcr0_4.ui.schermate.home.InserisciPosologiaViewModel
import com.example.hcr0_4.ui.schermate.home.PosologiaDetails
import com.example.hcr0_4.ui.schermate.home.TerapiaDetails
import com.example.hcr0_4.ui.schermate.home.toPosologia
import com.example.hcr0_4.ui.schermate.home.toTerapia
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class FolderViewModel(
    private val cartelleRepository: CartelleRepository,
    private val refertiRepository: RefertiRepository,
    private val posologiaRepository: PosologieRepository,
    private val terapieRepository: TerapieRepository,
    private val somministrazioneRepository: SomministrazioniRepository


) : ViewModel() {


    var refertiUiState by mutableStateOf(RefertiUiState())
        private set

    var selectedCartella by mutableStateOf<Cartella?>(null)
        private set

    val listaReferti: StateFlow<listaReferti> =
        refertiRepository.getAllReferti()
            .map { listaReferti(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = listaReferti()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    val cartelleUiState: StateFlow<CartelleUiState> = cartelleRepository.getAllCartelle()
        .map { CartelleUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = CartelleUiState()
        )

    fun updateUiStateReferti(refertiDetails: RefertiDetails) {
        refertiUiState = refertiUiState.copy(
            refertiDetails = refertiDetails,
            isEntryValid = validateRefertiInput(refertiDetails)
        )
    }

    fun saveReferto() {
        if (refertiUiState.isEntryValid) {
            viewModelScope.launch {
                saveRefertoToDatabase(refertiUiState.refertiDetails)
            }
        }
    }

    private suspend fun saveRefertoToDatabase(details: RefertiDetails) {
        refertiRepository.insertReferto(details.toReferti())
    }

    fun saveFileToDestination(context: Context, uri: Uri) {
        val cartellaName = selectedCartella?.nome

        if (cartellaName == null) {
            Log.e("saveFileToDestination", "Cartella name is null")
            return
        }

        Log.d("saveFileToDestination", "Cartella name: $cartellaName")

        val destinationPath = "/storage/emulated/0/Documents/Hcr/Documenti/$cartellaName"
        Log.d("saveFileToDestination", "Destination path: $destinationPath")

        val percorsofile = copyFileToInternalStorage(context, uri, destinationPath)
        if (percorsofile != null) {
            Log.d("saveFileToDestination", "File saved successfully: $percorsofile")
            val fileName = percorsofile.substringAfterLast("/")
            val updatedDetails = refertiUiState.refertiDetails.copy(
                idCartella = selectedCartella?.idCartella,
                percorsoFfile = percorsofile,
                nomeFile = fileName
            )
            updateUiStateReferti(updatedDetails)
            viewModelScope.launch {
                saveReferto()
            }

            // Verifica se il file è un XML
            val file = File(percorsofile)
            if (isFileXml(file)) {
                // Verifica se il file è compatibile
                if (isFileCompatible(file)) {
                    // Estrai le informazioni di terapia e posologia
                    val (terapiaDetails, posologiaDetailsList) = extractTerapiaAndPosologia(file)

                    // Salva la terapia e le posologie
                    saveTerapiaAndPosologie(terapiaDetails, posologiaDetailsList, updatedDetails.idReferto)
                } else {
                    Log.e("saveFileToDestination", "File non compatibile")
                }
            } else {
                Log.e("saveFileToDestination", "File non è un XML")
            }
        }
    }

    fun selectCartella(cartella: Cartella) {
        selectedCartella = cartella
    }

    private fun validateRefertiInput(details: RefertiDetails): Boolean {
        return details.nomeFile.isNotEmpty() && details.percorsoFfile.isNotEmpty()
    }

    private fun copyFileToInternalStorage(context: Context, uri: Uri, destinationPath: String): String? {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val fileName = getFileName(context, uri)
        val outputFile = File(destinationPath, fileName ?: "tempFile")

        try {
            val outputStream = FileOutputStream(outputFile)
            val buffer = ByteArray(1024)
            var length: Int

            inputStream?.use { input ->
                while (input.read(buffer).also { length = it } > 0) {
                    outputStream.write(buffer, 0, length)
                }
                outputStream.flush()
            }

            return outputFile.absolutePath
        } catch (e: Exception) {
            Log.e("copyFileToInternalStorage", "Error copying file to internal storage: ${e.message}")
            e.printStackTrace()
            return null
        }
    }

    private fun getFileName(context: Context, uri: Uri): String? {
        var fileName: String? = null

        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    fileName = it.getString(it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
                }
            }
        }

        if (fileName == null) {
            fileName = uri.path
            val cut = fileName?.lastIndexOf('/')
            if (cut != -1) {
                fileName = fileName?.substring(cut!! + 1)
            }
        }

        return fileName
    }

    fun isFileXml(file: File): Boolean {
        return file.extension.equals("xml", ignoreCase = true)
    }

    fun isFileCompatible(file: File): Boolean {
        // Implementa la logica per verificare se il file XML contiene le informazioni di terapia e posologia
        // Ad esempio, controlla se il file contiene specifici tag XML
        return true // Placeholder
    }

    val inserisciPosologiaViewModel: InserisciPosologiaViewModel by lazy {
        InserisciPosologiaViewModel(
            somministrazioneRepository = somministrazioneRepository,
            posologiaRepository = posologiaRepository,
            terapiaRepository = terapieRepository
        )
    }

    // Altri membri del ViewModel...

    fun saveTerapiaAndPosologie(terapiaDetails: TerapiaDetails, posologiaDetailsList: List<PosologiaDetails>, idReferto: Int) {
        // Aggiorna lo uiState del InserisciPosologiaViewModel
        inserisciPosologiaViewModel.updateUiStatePosologia(posologiaDetailsList)
        inserisciPosologiaViewModel.terapiaDetails = terapiaDetails.copy(idReferto = idReferto)

        // Salva la terapia, le posologie e genera le somministrazioni
        viewModelScope.launch {
            inserisciPosologiaViewModel.saveAll()
        }
    }
    fun extractTerapiaAndPosologia(file: File): Pair<TerapiaDetails, List<PosologiaDetails>> {
        // Implementa la logica per estrarre le informazioni di terapia e posologia dal file XML


        val terapiaDetails = TerapiaDetails() // Placeholder
        val posologiaDetailsList = listOf(PosologiaDetails()) // Placeholder
        return Pair(terapiaDetails, posologiaDetailsList)
    }

}

data class listaReferti(val refertiList: List<Referto> = listOf())
data class CartelleUiState(val cartelleList: List<Cartella> = listOf())

data class RefertiDetails(
    val idCartella: Int? = 0,
    val idReferto: Int = 0,
    val nomeFile: String = "",
    val percorsoFfile: String = ""
)

fun RefertiDetails.toReferti(): Referto = Referto(
    idCartella = idCartella,
    idReferto = idReferto,
    nomeFile = nomeFile,
    percorsoFfile = percorsoFfile
)

fun Referto.toRefertiDetails(idCartella: Int): RefertiDetails = RefertiDetails(
    idCartella = idCartella,
    idReferto = idReferto,
    nomeFile = nomeFile,
    percorsoFfile = percorsoFfile
)