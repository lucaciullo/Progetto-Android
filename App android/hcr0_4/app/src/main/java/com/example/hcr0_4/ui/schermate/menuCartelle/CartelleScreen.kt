package com.example.hcr0_4.ui.schermate.menuCartelle


import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hcr0_4.ui.theme.*
import androidx.compose.foundation.lazy.LazyListState

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.core.content.FileProvider
import com.example.hcr0_4.R
import com.example.hcr0_4.data.Referto
import com.example.hcr0_4.ui.AppViewModelProvider
import com.example.hcr0_4.ui.schermate.menuCartelle.InserisciRefertoDestination.idCartella
import com.example.hcr0_4.ui.theme.components.CampoDiTesto
import com.example.hcr0_4.ui.theme.components.ExpandableCardFunction
import com.example.hcr0_4.ui.theme.components.Pulsante
import com.example.hcr0_4.ui.theme.components.Titolo
import com.example.hcr0_4.ui.theme.components.getIcon


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FolderScreen(
    modifier: Modifier = Modifier,
    viewModel: FolderViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateToInserisciCartella: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    val cartelleUiState by viewModel.cartelleUiState.collectAsState()

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
            TopAppBar(
                title = {
                    Text(text = "Cartelle", textAlign = TextAlign.Center)
                }
            )
        },
        floatingActionButton = {
            Pulsante(
                onClick = navigateToInserisciCartella
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.item_entry_title),
                    tint = Color.White
                )
            }
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            CampoDiTesto(
                text = searchQuery,
                onTextChange = { searchQuery = it },
                placeholder = "Search",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            val filteredItems =
                cartelleUiState.cartelleList.filter {
                    it.nome.contains(searchQuery, ignoreCase = true)
                }

            val scrollState = LazyListState()
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = scrollState
            ) {
                items(filteredItems) { item ->
                    ExpandableCardFunction(
                        icon = getIcon(tipo = item.nome),
                        titolo = { Titolo(text = item.nome) },
                        boxContent = {
                            Column {
                                FileBody(item.idCartella)
                            }
                        },
                        pulsanteFinaledestro = {
                            Pulsante(
                                onClick = {
                                    viewModel.selectCartella(item)
                                    pickFileLauncher.launch(arrayOf("*/*"))
                                },
                                testoPulsante = "Inserisci file"
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                        }
                    )
                }
            }
        }
    }
}
@Composable
fun FileBody(
    idCartella: Int,
) {
    val viewModel: FolderViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val refertiUiState by viewModel.listaReferti.collectAsState()
    refertiUiState.refertiList.forEach {
        if (it.idCartella == idCartella) {
            File(referto = it)
        }
    }
}

@Composable
fun File(referto: Referto) {
    val context = LocalContext.current
    val viewModel: FolderViewModel = viewModel(factory = AppViewModelProvider.Factory)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = referto.nomeFile, modifier = Modifier.weight(1f))
        Pulsante(
            onClick = {
                val file = java.io.File(referto.percorsoFfile)
                if (file.exists()) {
                    val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        setDataAndType(uri, context.contentResolver.getType(uri))
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    context.startActivity(intent)

                    // Verifica se il file è un XML
                    if (viewModel.isFileXml(file)) {
                        // Verifica se il file è compatibile
                        if (viewModel.isFileCompatible(file)) {
                            // Estrai le informazioni di terapia e posologia
                            val (terapiaDetails, posologiaDetailsList) = viewModel.extractTerapiaAndPosologia(file)

                            // Salva la terapia e le posologie
                            viewModel.saveTerapiaAndPosologie(terapiaDetails, posologiaDetailsList, referto.idReferto)
                        } else {
                            Toast.makeText(context, "File non compatibile", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "File non è un XML", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "File non trovato", Toast.LENGTH_SHORT).show()
                }
            },
            testoPulsante = "Apri file"
        )
    }
}

