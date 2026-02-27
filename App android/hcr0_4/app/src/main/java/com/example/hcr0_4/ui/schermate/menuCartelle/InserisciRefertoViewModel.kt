package com.example.hcr0_4.ui.schermate.menuCartelle

import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hcr0_4.data.Cartella
import com.example.hcr0_4.data.CartelleRepository
import com.example.hcr0_4.data.Referto
import com.example.hcr0_4.data.RefertiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.FileOutputStream
import java.io.InputStream

class InserisciRefertoViewModel(
    private val referti: RefertiRepository,
    private val cartella: CartelleRepository,
    savedStateHandle: SavedStateHandle

): ViewModel(){

    var refertiUiState by mutableStateOf(RefertiUiState())
        private set


    private val idCartella: Int = checkNotNull(savedStateHandle[InserisciRefertoDestination.idCartella])
    fun updateUiStateReferti(refertiDetails: RefertiDetails){
        refertiUiState = refertiUiState.copy(
            refertiDetails = RefertiDetails(
                idCartella = idCartella,
                idReferto = refertiDetails.idReferto,
                nomeFile = refertiDetails.nomeFile,
                percorsoFfile = refertiDetails.percorsoFfile
            ),
            isEntryValid = validateRefertiInput(refertiDetails)
        )
    }

    init {
        viewModelScope.launch {
            val cartella = getCartella()
            refertiUiState = refertiUiState.copy(cartella = cartella)
        }
    }
    private suspend fun getCartella(): Cartella? {
        return cartella.getCartella(idCartella).firstOrNull()
    }

    fun saveReferto(){
        if(refertiUiState.isEntryValid){
            viewModelScope.launch {
                Log.d("saveReferto", "Referti details: ${refertiUiState.refertiDetails}")
                referti.insertReferto(refertiUiState.refertiDetails.toReferti())
            }
        }
    }

    suspend fun getCartellaName(): String? {
        return cartella.getCartella(idCartella).map { it?.nome }.firstOrNull()
    }

    fun saveFileToDestination(context: Context, uri: Uri): String? {
        var cartellaName: String? = "alvin"

            cartellaName = refertiUiState.cartella?.nome

        Log.d("saveFileToDestination", "idCartella: $idCartella")

        if (cartellaName == null) {
            Log.e("saveFileToDestination", "Cartella name is null")
            return null
        }

        Log.d("saveFileToDestination", "Cartella name: $cartellaName")


        val destinationPath = "/storage/emulated/0/Documents/Hcr/Documenti/$cartellaName"
    Log.d("saveFileToDestination", "Destination path: $destinationPath")

    val percorsofile = copyFileToInternalStorage(context, uri, destinationPath)
    if (percorsofile != null) {
        Log.d("saveFileToDestination", "File saved successfully: $percorsofile")
        val fileName = percorsofile.substringAfterLast("/")
        val updatedDetails = refertiUiState.refertiDetails.copy(percorsoFfile = percorsofile, nomeFile = fileName)
        updateUiStateReferti(updatedDetails)
        viewModelScope.launch {
            saveReferto()
        }
    }
    return percorsofile
}


    private fun validateRefertiInput(details: RefertiDetails): Boolean {
        return true
    }
}

private fun getPercorsoFile(context: Context, uri: Uri, destinationPath: String): String? {
    if (DocumentsContract.isDocumentUri(context, uri)) {
        val documentId = DocumentsContract.getDocumentId(uri)
        val split = documentId.split(":").toTypedArray()
        val type = split[0]

        return when (type) {
            "primary" -> "/storage/emulated/0/${split[1]}"
            "image", "video", "audio" -> {
                val contentUri = when (type) {
                    "image" -> android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    "video" -> android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    "audio" -> android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    else -> null
                }
                contentUri?.let {
                    val selection = "_id=?"
                    val selectionArgs = arrayOf(split[1])
                    getDataColumn(context, it, selection, selectionArgs)
                }
            }
            else -> {
                copyFileToInternalStorage(context, uri, destinationPath)
            }
        }
    } else if ("content".equals(uri.scheme, ignoreCase = true)) {
        getDataColumn(context, uri, null, null) ?: copyFileToInternalStorage(context, uri, destinationPath)
    } else if ("file".equals(uri.scheme, ignoreCase = true)) {
        return uri.path
    } else {
        return null
    }
    return null
}

private fun copyFileToInternalStorage(context: Context, uri: Uri, destinationPath: String): String? {
    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
    val fileName = getFileName(context, uri)
    val outputFile = java.io.File(destinationPath, fileName ?: "tempFile")

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

private fun getDataColumn(context: Context, uri: Uri, selection: String?, selectionArgs: Array<String>?): String? {
    // Implementa la logica per ottenere la colonna dei dati
    return null
}

private fun getFileName(context: Context, uri: Uri): String? {
    // Implementa la logica per ottenere il nome del file
    return null
}


data class RefertiUiState(
    val refertiDetails: RefertiDetails = RefertiDetails(),
    val cartella: Cartella? = null,
    val isEntryValid: Boolean = false
)
