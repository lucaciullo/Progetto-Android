package com.example.hcr0_4.ui.schermate.menuPersonalizzazioni


import android.util.Log
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material3.Text

import androidx.compose.runtime.Composable

import androidx.compose.runtime.getValue

import androidx.lifecycle.viewmodel.compose.viewModel


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.hcr0_4.ui.AppViewModelProvider
import com.example.hcr0_4.R
import com.example.hcr0_4.data.Posologia
import com.example.hcr0_4.data.Terapia
import com.example.hcr0_4.ui.navigazione.NavigationDestination
import com.example.hcr0_4.ui.theme.HcrGreen
import com.example.hcr0_4.ui.theme.components.ExpandableCardFunction
import com.example.hcr0_4.ui.theme.components.Pulsante
import com.example.hcr0_4.ui.theme.components.SottoTitolo
import com.example.hcr0_4.ui.theme.components.Titolo
import com.example.hcr0_4.ui.theme.components.getIcon
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter


object PersonalizzazioniDestination : NavigationDestination {
    override val route = "personalizzazioni"
    override val titleRes = R.string.app_name
}

/**
 * Entry route for Personalizzazioni screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalizeScreen(
    modifier: Modifier = Modifier,
    viewModel: PersonalizzazioniViewModel = viewModel(factory = AppViewModelProvider.Factory)

) {
    val PersonalizzazioniUiState by viewModel.personalizazzioniUiState.collectAsState()



Column(
    verticalArrangement = Arrangement.Center
) {


    TopAppBar(
        title = {
            Text(
                text = "Personalizzazioni",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    )


    val coroutineScope = rememberCoroutineScope()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        PersonalizzazioniBody(
            terapiaList = PersonalizzazioniUiState.terapiaList, // Pass somministrazioneList here
            posologiaList = PersonalizzazioniUiState.posologiaList,
            updateTerapia = {
                coroutineScope.launch {
                    viewModel.updateTerapia(it)
                }
            },
            onEliminaClick ={ terapia ->
                coroutineScope.launch {
                    viewModel.eliminaTerapia(terapia)
                }
            },
            modifier = modifier.fillMaxSize()
        )
    }
}

}

@Composable
private fun PersonalizzazioniBody(
    terapiaList: List<Terapia>,
    posologiaList: List<Posologia>,
    updateTerapia: (Terapia) -> Unit,
    onEliminaClick: (Terapia) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {

        if (terapiaList.isEmpty()) {
            Text(
                text = "Non esistono terapie",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        } else {
            Column {

                //dobbiamo creare 3 expandable card principali una per le medicine, una per le diete e una per gli esercizi, dentro ognuna di queste ci saranno le varie terapie

                val listaTerapie = terapiaList.filter { it.tipo == "MEDICINA" }
                val listaDiete = terapiaList.filter { it.tipo == "DIETA" }
                val listaEsercizi = terapiaList.filter { it.tipo == "ESERCIZIO" }
                if (listaTerapie.isNotEmpty()) {
                    ExpandableCardFunction(icon = getIcon(tipo = "MEDICINA"), titolo = { Titolo(text = "Terapie") }, boxContent = {
                        TerapieList(
                            terapieList = listaTerapie,
                            updateTerapia = updateTerapia,
                            posologiaList = posologiaList,
                            onEliminaClick = onEliminaClick
                        )
                    })
                }


                if (listaDiete.isNotEmpty()) {
                    ExpandableCardFunction(icon = getIcon(tipo = "DIETA"), titolo = { Titolo(text = "Diete") }, boxContent = {
                        TerapieList(
                            terapieList = listaDiete,
                            updateTerapia = updateTerapia,
                            posologiaList = posologiaList,
                            onEliminaClick = onEliminaClick



                        )
                    })
                }

                if (listaEsercizi.isNotEmpty()) {
                    ExpandableCardFunction(icon = getIcon(tipo = "ESERCIZIO"),titolo = { Titolo(text = "Esercizi") }, boxContent = {
                        TerapieList(
                            terapieList = listaEsercizi,
                            updateTerapia = updateTerapia,
                            posologiaList = posologiaList,
                            onEliminaClick = onEliminaClick


                        )
                    })
                }


            }
        }
    }
}



@Composable
private fun TerapieList(
    terapieList: List<Terapia>,
    posologiaList: List<Posologia>,
    updateTerapia: (Terapia) -> Unit,
    onEliminaClick: (Terapia) -> Unit = {},

    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(items = terapieList, key = { it.idTerapia }) { terapia ->
            HcrTerapie(
                terapia = terapia,
                posologiaList = posologiaList.filter { it.idTerapia == terapia.idTerapia },
                updateTerapia = updateTerapia,
                onEliminaClick = onEliminaClick


            )
        }
    }
}

// Update the `HcrTerapie` composable function

@Composable
private fun HcrTerapie(
    terapia: Terapia,
    posologiaList: List<Posologia>,
    updateTerapia: (Terapia) -> Unit,
    onEliminaClick: (Terapia) -> Unit = {}
) {
    Log.d("Hcr", "Entra in HcrTerapie")

    val coroutineScope = rememberCoroutineScope()

    ExpandableCardFunction(
        icon = null,
        titolo = {
            SottoTitolo(terapia.nomeTerapia)
        },
        sottotitolo = {




            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween, // Align boxes to the ends
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Column {
                        Text(text = terapia.statoTerapia)
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(text = terapia.tipoAvviso)
                    }
                }
                Box(
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Row {
                        IconButton(
                            onClick = {
                                val newStatoTerapia = toggleStatoTerapia(terapia.statoTerapia)
                                coroutineScope.launch {
                                    updateTerapia(terapia.copy(statoTerapia = newStatoTerapia))
                                }
                            },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                painter = getIcon(tipo = terapia.statoTerapia),
                                contentDescription = null,
                                modifier = Modifier.size(30.dp),
                                tint = HcrGreen
                            )
                        }
                        IconButton(
                            onClick = {
                                val newTipoAvviso = rotateTipoAvviso(terapia.tipoAvviso)
                                coroutineScope.launch {
                                    updateTerapia(terapia.copy(tipoAvviso = newTipoAvviso))
                                }
                            },
                            modifier = Modifier.size(48.dp) // Increase button size
                        ) {
                            Icon(
                                painter = getIcon(tipo = terapia.tipoAvviso),
                                contentDescription = null,
                                modifier = Modifier.size(36.dp),
                                tint = HcrGreen
                            )
                        }
                    }
                }
            }
        },
        boxContent = {},
        pulsanteFinaledestro = {
            Pulsante(
                testoPulsante = "Elimina",
                onClick = {
                    //navigateTo(NavigationDestination.InserisciTerapiaScreen(terapia))
                    onEliminaClick(terapia)

                }
            )
        }
    )
}

fun convertiGiornoSettimana(giorno: String): String {
    return when (giorno) {
        "Lu" -> "Lunedi'"
        "Ma" -> "Martedi'"
        "Me" -> "Mercoledi'"
        "Gi" -> "Giovedi'"
        "Ve" -> "Venerdi'"
        "Sa" -> "Sabato"
        "Do" -> "Domenica"
        else -> giorno
    }
}
fun traduzioneGiorniDellaSettimana(posologiaList: List<Posologia>): String {
    val giorniMap = mutableMapOf<String, MutableList<String>>()


    posologiaList.forEach { posologia ->
        val giorniList = posologia.giorniSettimana
    .removeSurrounding("[", "]") // Remove square brackets
    .replace(" ", "") // Remove potential spaces
    .split(",") // Convert string to list
        Log.d("traduzioneGiorniDellaSettimana", "Processing posologia: ${posologia.giorniSettimana} -> $giorniList")
        giorniList.forEach { giorno ->
            val giornoCompleto = convertiGiornoSettimana(giorno)
            Log.d("traduzioneGiorniDellaSettimana", "Processing giorno: $giorno -> $giornoCompleto")
            val oraFormattata = posologia.oraPosologia.format(DateTimeFormatter.ofPattern("HH:mm"))
            if (giorniMap.containsKey(giornoCompleto)) {
                giorniMap[giornoCompleto]?.add(oraFormattata)
            } else {
                giorniMap[giornoCompleto] = mutableListOf(oraFormattata)
            }
            Log.d("traduzioneGiorniDellaSettimana", "Updated giorniMap: $giorniMap")
        }
    }

    val giorniOrdine = listOf("Lunedi'", "Martedi'", "Mercoledi'", "Giovedi'", "Venerdi'", "Sabato", "Domenica")
    val result = StringBuilder()

    giorniOrdine.forEach { giorno ->
        if (giorniMap.containsKey(giorno)) {
            result.append("$giorno alle ${giorniMap[giorno]?.joinToString(" e alle ")} ")
            Log.d("traduzioneGiorniDellaSettimana", "Appending to result: $giorno alle ${giorniMap[giorno]?.joinToString(" e alle ")}")
        }
    }

    val finalResult = result.toString().trim()
    Log.d("traduzioneGiorniDellaSettimana", "Final result: $finalResult")
    return " "+finalResult
}
fun rotateTipoAvviso(currentTipoAvviso: String): String {
    return when (currentTipoAvviso) {
        "Off" -> "Notifica"
        "Notifica" -> "Sveglia"
        else -> "Off"
    }
}

fun toggleStatoTerapia(currentStatoTerapia: String): String {
    return if (currentStatoTerapia == "Attiva") "Disattiva" else "Attiva"
}
