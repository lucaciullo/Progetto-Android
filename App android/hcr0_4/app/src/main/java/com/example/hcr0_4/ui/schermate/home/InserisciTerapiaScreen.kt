package com.example.hcr0_4.ui.schermate.home

import android.util.Log

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hcr0_4.HcrTopAppBar
import com.example.hcr0_4.LocalDateAdapter
import com.example.hcr0_4.R
import com.example.hcr0_4.ui.AppViewModelProvider
import com.example.hcr0_4.ui.navigazione.NavigationDestination
import com.example.hcr0_4.ui.theme.components.ButtonSelector
import com.example.hcr0_4.ui.theme.components.CampoDiTesto
import com.example.hcr0_4.ui.theme.components.InserisciData
import com.example.hcr0_4.ui.theme.components.Pulsante
import com.example.hcr0_4.ui.theme.components.SottoSottoTitolo
import com.example.hcr0_4.ui.theme.components.SottoTitolo
import com.google.gson.GsonBuilder
import kotlinx.coroutines.launch
import java.time.LocalDate


object InserisciTerapiaDestination : NavigationDestination {
    override val route = "inserisciTerapia"
    override val titleRes = R.string.action_add
    const val terapiaDetailsArg = "terapiaDetails"
    val routeWithArgs = "$route/{$terapiaDetailsArg}"
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InserisciTerapiaScreen(
    navigateToInserisciPosologia: (String) -> Unit,
    viewModel: InserisciTerapiaViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true
) {
    val coroutineScope = rememberCoroutineScope()
    Log.d("InserisciTerapiaScreen", "viewModel.terapiaUiState: ${viewModel.terapiaUiState}")
    Scaffold(
        topBar = {
            HcrTopAppBar(
                title = "Inserisci dati",
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        },
        floatingActionButton = {
            if(viewModel.terapiaUiState.isEntryValid){
                Pulsante(
                    onClick = {
                        coroutineScope.launch {
                            val gson = GsonBuilder()
                                .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter)
                                .create()
                            val terapiaDetailsJson: String = gson.toJson(viewModel.terapiaUiState.terapiaDetails)
                            navigateToInserisciPosologia(terapiaDetailsJson)
                        }
                    }
                ) {
                    SottoSottoTitolo(text = "Avanti")
                }
            }

        },
    ) { innerPadding ->


        InserisciTerapiaBody(
            terapiaUiState = viewModel.terapiaUiState,
            onValueChangeTerapia = viewModel::updateUiStateTerapia,
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        )
    }
}


@Composable
fun InserisciTerapiaBody(
    modifier: Modifier = Modifier,
    terapiaUiState: TerapiaUiState,
    onValueChangeTerapia: (TerapiaDetails) -> Unit = {  }
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement
            .spacedBy(dimensionResource(id = R.dimen.padding_large)),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        InserisciTerapiaUiElemnts(
            terapiaDetails = terapiaUiState.terapiaDetails,
            onValueChange = onValueChangeTerapia
        )

    }
}



// Improve the `InserisciTerapiaUiElemnts` composable for better readability and maintainability
@Composable
fun InserisciTerapiaUiElemnts(
    terapiaDetails: TerapiaDetails,
    onValueChange: (TerapiaDetails) -> Unit
) {
    val tipiAvviso = listOf("Notifica", "Sveglia", "Off")
    val tipiDurata = listOf("Data fine", "N° giorni", "N° di volte")
    val distanzaSezioni = 4.dp

    val nomeTerapia = when (terapiaDetails.tipo) {
        "MEDICINA" -> "Nome farmaco"
        "DIETA" -> "Nome piatto"
        "ESERCIZIO" -> "Nome esercizio"
        else -> "Nome Terapia"
    }

    Spacer(modifier = Modifier.height(distanzaSezioni))
    SottoTitolo("Nome")

    CampoDiTesto(
        text = terapiaDetails.nomeTerapia,
        onTextChange = { onValueChange(terapiaDetails.copy(nomeTerapia = it)) },
        placeholder = nomeTerapia,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    )

    Spacer(modifier = Modifier.height(distanzaSezioni))
    SottoTitolo(text = "Forma")
    val formaTerapia = forma(terapiaDetails.tipo)
    ButtonSelector(
        items = formaTerapia,
        elementoSelezionato = terapiaDetails.forma
    ) { item ->
        onValueChange(terapiaDetails.copy(forma = item))
    }

    Spacer(modifier = Modifier.height(distanzaSezioni))
    SottoTitolo("Inizio")
    InserisciData(
        label = " ",
        date = terapiaDetails.dataInizio,
        onDateChange = {
            onValueChange(terapiaDetails.copy(dataInizio = it))
        }
    )


    Spacer(modifier = Modifier.height(distanzaSezioni))
    SottoTitolo("Durata")
    ButtonSelector(
        items = tipiDurata,
        defaultValue = "",
        elementoSelezionato = terapiaDetails.tipiDurata
    ) {
        onValueChange(terapiaDetails.copy(tipiDurata = it))
    }

    val nGiorni = remember { mutableIntStateOf(if(terapiaDetails.tipiDurata == "N° giorni") terapiaDetails.durata else 0) }
    val nVolte = remember { mutableIntStateOf(if(terapiaDetails.tipiDurata == "N° di volte") terapiaDetails.durata else 0) }

    when (terapiaDetails.tipiDurata) {
        "Data fine" -> {
            InserisciData(
                label = "",
                date = if (terapiaDetails.dataFine.isBefore(terapiaDetails.dataInizio)) terapiaDetails.dataInizio else terapiaDetails.dataFine,
                onDateChange = { onValueChange(terapiaDetails.copy(dataFine = it)) }
            )
        }
        "N° giorni" -> {
            CampoDiTesto(
                text = if (nGiorni.intValue == 0) "" else nGiorni.intValue.toString(),
                onTextChange = {
                    val newValue = it.toIntOrNull()
                    if (newValue != null || it.isEmpty()) {
                        nGiorni.intValue = newValue ?: 0
                        onValueChange(terapiaDetails.copy(durata = newValue ?: 0))
                    }
                },
                placeholder = "Numero di giorni",
                tastierinoNumerico = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
        }
        "N° di volte" -> {
            CampoDiTesto(
                text = if (nVolte.intValue == 0) "" else nVolte.intValue.toString(),
                onTextChange = {
                    val newValue = it.toIntOrNull()
                    if (newValue != null || it.isEmpty()) {
                        nVolte.intValue = newValue ?: 0
                        onValueChange(terapiaDetails.copy(durata = newValue ?: 0))
                    }
                },
                placeholder = "Numero di volte",
                tastierinoNumerico = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
        }
    }




    Spacer(modifier = Modifier.height(distanzaSezioni))
    SottoTitolo("Cadenza")
    val tipiFrequenza = listOf("Giorn.", "Sett.", "Pers.")
    val selectedFrequenza = remember { mutableStateOf("") }

    ButtonSelector(
        items = tipiFrequenza,
        elementoSelezionato = terapiaDetails.frequenza
    ) {
        onValueChange(
            terapiaDetails.copy(
                ogniTotGiorni = when (it) {
                    "Giorn." -> 1
                    "Sett." -> 7
                    else -> 0
                },
                ogniTotMin = 0,
                ogniTotOre = 0,
                frequenza = it
            )

        )
        Log.d("hcr", "selectedFrequenza: $it")

    }

    when (terapiaDetails.frequenza) {

        "Pers." -> {
            onValueChange(terapiaDetails.copy(personalizzata = true))
            Row(modifier = Modifier.fillMaxWidth()) {
                CampoDiTesto(
                    text = if(terapiaDetails.ogniTotGiorni == 0) "" else terapiaDetails.ogniTotGiorni.toString(),
                    onTextChange = {onValueChange(terapiaDetails.copy(ogniTotGiorni = it.toIntOrNull() ?: 0))},
                    placeholder = "Giorni",
                    tastierinoNumerico = true,
                    modifier = Modifier.weight(0.3f)
                )
                CampoDiTesto(
                    text = if(terapiaDetails.ogniTotOre == 0) "" else terapiaDetails.ogniTotOre.toString(),
                    onTextChange = {onValueChange(terapiaDetails.copy(ogniTotOre = it.toIntOrNull() ?: 0))},
                    placeholder = "Ore",
                    tastierinoNumerico = true,
                    modifier = Modifier.weight(0.3f)
                )
                CampoDiTesto(
                    text = if(terapiaDetails.ogniTotMin == 0) "" else terapiaDetails.ogniTotMin.toString(),
                    onTextChange = {onValueChange(terapiaDetails.copy(ogniTotMin = it.toIntOrNull() ?: 0))},
                    placeholder = "Minuti",
                    tastierinoNumerico = true,
                    modifier = Modifier.weight(0.3f)
                )
            }
        }
        else -> {
            onValueChange(
                terapiaDetails.copy(
                    personalizzata = false

                )
            )


        }
    }

    Spacer(modifier = Modifier.height(distanzaSezioni))
    SottoTitolo(text = "Avviso")
    ButtonSelector(
        items = tipiAvviso,
        elementoSelezionato = terapiaDetails.tipoAvviso
    ) {
        onValueChange(terapiaDetails.copy(tipoAvviso = it))
    }
    Spacer(modifier = Modifier.height(distanzaSezioni))
}





fun forma(tipo: String): List<String>{
    return when (tipo) {
        "MEDICINA" -> listOf("Compresse", "Sciroppo", "Iniezione", "Altro", "Non specificato")
        "DIETA" -> listOf("Grammi", "Porzioni", "Calorie", "Altro", "Non specificato")
        "ESERCIZIO" -> listOf("Minuti", "Ore", "Km", "Altro","Ripetizioni", "Non specificato")
        else -> listOf("Non specificato")
    }
}