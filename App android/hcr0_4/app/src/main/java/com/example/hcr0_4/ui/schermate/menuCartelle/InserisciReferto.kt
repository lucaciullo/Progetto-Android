package com.example.hcr0_4.ui.schermate.menuCartelle

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.OpenableColumns

import androidx.activity.result.contract.ActivityResultContracts

import kotlinx.coroutines.launch

import com.example.hcr0_4.ui.AppViewModelProvider
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hcr0_4.HcrTopAppBar
import com.example.hcr0_4.R
import com.example.hcr0_4.ui.navigazione.NavigationDestination
import com.example.hcr0_4.ui.theme.components.Pulsante
import org.xml.sax.InputSource
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory

import org.w3c.dom.Element
import org.w3c.dom.Node

object InserisciRefertoDestination : NavigationDestination {
    override val route = "InserisciReferto"
    override val titleRes = R.string.action_add
    const val idCartella = "idCartella"
    val routeWithArgs = "$route/{$idCartella}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InserisciReferto(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: InserisciRefertoViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
Log.d("HcrReferti", viewModel.refertiUiState.cartella.toString())
    val pickFileLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        uri?.let {
            val baseDestinationPath = context.filesDir.absolutePath
            viewModel.saveFileToDestination(context, it)
        }
    }

    Scaffold(
        topBar = {
            HcrTopAppBar(
                title = "Inserisci dati referti",
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        InserisciRefertiBody(
            refertiUiState = viewModel.refertiUiState,
            onValueChangeReferti = viewModel::updateUiStateReferti,
            pickFileLauncher = pickFileLauncher,
            onNavigateBack = navigateBack,
            onSaveClick = {
                coroutineScope.launch {
                    val idCartella = viewModel.refertiUiState.refertiDetails.idCartella
                    Log.d("HcrReferti", "ID Parametro : $idCartella")
                    parseXml(viewModel.refertiUiState.refertiDetails.percorsoFfile)
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding(),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                )
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}
private fun readXmlFileContent(context: Context, uri: Uri): String? {
    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
    val content = inputStream?.bufferedReader().use { it?.readText() }
    inputStream?.close()
    return content
}


fun parseXml(xmlContent: String) {
    try {
        val factory = DocumentBuilderFactory.newInstance()
        val builder = factory.newDocumentBuilder()
        val document = builder.parse(InputSource(StringReader(xmlContent)))

        val nodeList = document.getElementsByTagName("Author") // Sostituisci "tag_name" con il nome del tag desiderato

        for (i in 0 until nodeList.length) {
            val node = nodeList.item(i)
            if (node.nodeType == Node.ELEMENT_NODE) {
                val element = node as Element
                val fieldValue = element.textContent // Ottieni il contenuto del tag
                // Fai qualcosa con fieldValue
                Log.d("HcrFolder", "Valore: ${fieldValue}") // Ad esempio, stampa il contenuto del tag
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}



@Composable
fun InserisciRefertiBody(
    refertiUiState: RefertiUiState,
    onNavigateBack: () -> Unit,
    onValueChangeReferti: (RefertiDetails) -> Unit = { },
    onSaveClick: () -> Unit,
    pickFileLauncher: ActivityResultLauncher<Array<String>>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large))
    ) {
        InserisciRefertiUiElements(
            refertiDetails = refertiUiState.refertiDetails,
            onNavigateBack = onNavigateBack,
            pickFileLauncher = pickFileLauncher
        )

    }
}

@Composable
fun InserisciRefertiUiElements(
    refertiDetails: RefertiDetails,
    onNavigateBack: () -> Unit,
    pickFileLauncher: ActivityResultLauncher<Array<String>>
) {
    Pulsante(
        onClick = { pickFileLauncher.launch(arrayOf("*/*"))
                  onNavigateBack()
                  },
        testoPulsante = "Scegli file"
    )
}



private fun getPercorsoFile(context: Context, uri: Uri): String? {
    // Controlliamo se l'URI è un documento
    if (DocumentsContract.isDocumentUri(context, uri)) {
        Log.d("getPercorsoFile", "URI is document")
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
                Log.d("getPercorsoFile", "URI is document2 $uri")
                // Gestisce altre tipologie di URI di documento
                copyFileToInternalStorage(context, uri)
            }
        }
    } else if ("content".equals(uri.scheme, ignoreCase = true)) {
        Log.d("getPercorsoFile", "URI is content2")
        getDataColumn(context, uri, null, null) ?: copyFileToInternalStorage(context, uri)
    } else if ("file".equals(uri.scheme, ignoreCase = true)) {
        Log.d("getPercorsoFile", "URI is file ${uri.path}")
        return uri.path
    } else {
        Log.d("getPercorsoFile", "URI is null or unknown: $uri")
        return null
    }
    return null
}



private fun getDataColumn(context: Context, uri: Uri, selection: String?, selectionArgs: Array<String>?): String? {
    var cursor: Cursor? = null
    val column = "_data"
    val projection = arrayOf(column)
    try {
        cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
        if (cursor != null && cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndexOrThrow(column)
            return cursor.getString(columnIndex)
        }
    } finally {
        cursor?.close()
    }
    return null
}

private fun copyFileToInternalStorage(context: Context, uri: Uri): String? {
    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
    val fileName = getFileName(context, uri)
    val outputFile = File(context.filesDir, fileName ?: "tempFile")

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
        Log.e("getPercorsoFile", "Error copying file to internal storage: ${e.message}")
        e.printStackTrace()
        return null
    }
}

private fun getFileName(context: Context, uri: Uri): String? {
    var result: String? = null
    if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                result = it.getString(it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
            }
        }
    }
    if (result == null) {
        result = uri.path
        val cut = result?.lastIndexOf('/')
        if (cut != -1) {
            result = result?.substring(cut!! + 1)
        }
    }
    return result
}