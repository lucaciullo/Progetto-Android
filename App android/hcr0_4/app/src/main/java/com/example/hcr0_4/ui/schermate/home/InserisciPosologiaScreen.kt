package com.example.hcr0_4.ui.schermate.home

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hcr0_4.HcrTopAppBar
import com.example.hcr0_4.R
import com.example.hcr0_4.ui.AppViewModelProvider
import com.example.hcr0_4.ui.navigazione.NavigationDestination
import com.example.hcr0_4.ui.theme.components.ButtonSelector
import com.example.hcr0_4.ui.theme.components.CampoDiTesto
import com.example.hcr0_4.ui.theme.components.ExpandableCardFunction
import com.example.hcr0_4.ui.theme.components.InserisciOra
import com.example.hcr0_4.ui.theme.components.MultiSelectorButton
import com.example.hcr0_4.ui.theme.components.Pulsante
import com.example.hcr0_4.ui.theme.components.SottoSottoTitolo
import com.example.hcr0_4.ui.theme.components.SottoTitolo
import com.example.hcr0_4.ui.theme.components.getIcon
import kotlinx.coroutines.launch
import java.time.LocalTime

object InserisciPosologiaDestination : NavigationDestination {
    override val route = "inserisciPosologia"
    override val titleRes = R.string.action_add
    const val terapiaDetailsArg = "terapiaDetails"
    val routeWithArgs = "$route/{$terapiaDetailsArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InserisciPosologia(
    navigateToHome: () -> Unit,
    viewModel: InserisciPosologiaViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onNavigateUp: (String) -> Unit,
    canNavigateBack: Boolean = true
) {
    val coroutineScope = rememberCoroutineScope()
    Log.d("InserisciPosologia", "Siamo in posologia, terapiaDetailsString:  ${viewModel.terapiaDetailsString}")
    val posologiaDetailMutableList = remember {
        mutableStateListOf<PosologiaDetails>().apply {
            addAll(viewModel.posologiaUiState.posologiaDetailsList)
        }
    }

    BackHandler {
        val updatedTerapiaData = viewModel.terapiaDetailsString
        Log.d("InserisciPosologia", "BackHandler attivato ${viewModel.terapiaDetailsString}")
        onNavigateUp(updatedTerapiaData)
    }

    Scaffold(
        topBar = {
            HcrTopAppBar(
                title = "Inserisci dati",
                canNavigateBack = canNavigateBack,
                navigateUp = {
                    Log.d("InserisciPosologia", "Navigate back Ui posologia details ${viewModel.terapiaDetailsString}")
                    onNavigateUp(viewModel.terapiaDetailsString) }
            )

        },floatingActionButton = {
            if(viewModel.posologiaUiState.isEntryValid){
                Pulsante(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.saveAll()
                            navigateToHome()
                        }
                    }
                ) {
                    SottoSottoTitolo(text = "Salva")
                }
            }

        },
    ) { innerPadding ->
        InserisciPosologiaBody(
            posologiaDetails = posologiaDetailMutableList,
            terapiaDetails = viewModel.terapiaDetails,
            onValueChange = { updatedList ->
                posologiaDetailMutableList.clear()
                posologiaDetailMutableList.addAll(updatedList)
                viewModel.updateUiStatePosologia(updatedList)
            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}

@Composable
fun InserisciPosologiaBody(
    posologiaDetails: MutableList<PosologiaDetails>,
    terapiaDetails: TerapiaDetails,
    onValueChange: (List<PosologiaDetails>) -> Unit = { },
    modifier: Modifier
) {
    val bottonePremuto = remember { mutableIntStateOf(1) }
    val elementoSelezionato = remember { mutableStateOf("${bottonePremuto.intValue}x") }
    // Log per debug



    // Aggiorna posologiaDetails quando bottonePremuto cambia
    LaunchedEffect(bottonePremuto.intValue) {
        val currentSize = posologiaDetails.size
        val newSize = bottonePremuto.intValue

        // Aggiungi nuove posologie se il nuovo valore è maggiore dell'attuale

        if (bottonePremuto.intValue - posologiaDetails.size >=  1) {
            posologiaDetails.add(PosologiaDetails(ora = LocalTime.now()))
        }
        if (bottonePremuto.intValue - posologiaDetails.size >= 1) {

            posologiaDetails.add(PosologiaDetails(ora = LocalTime.now()))
        }
        if (newSize < currentSize) {
            for (i in currentSize - 1 downTo newSize) {
                posologiaDetails.removeAt(i)
            }
        }


        onValueChange(posologiaDetails.toList())

    }

    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
    ) {
        if (terapiaDetails.frequenza != "Pers.") {

            ButtonSelector(
                turnOff = false,
                elementoSelezionato = elementoSelezionato.value,
                items = listOf("1x", "2x", "3x", "+"),
                defaultValue = ""
            ) { selectedButton ->
                bottonePremuto.intValue = when (selectedButton) {
                    "1x" -> 1
                    "2x" -> 2
                    "3x" -> 3
                    "+" ->{
                        Log.d("InserisciPosologiaBody", "Ui posologia details + ${bottonePremuto.intValue}")
                        bottonePremuto.intValue +1

                    }
                    else -> 0
                }
            }
        }

        posologiaDetails.forEachIndexed { index, posologiaDetail ->

            InserisciPosologiaUiElements(
                posologiaDetail = posologiaDetail,
                terapiaDetail = terapiaDetails,
                elimina = {

                    if (bottonePremuto.intValue > 1) {
                        Button(
                            onClick = {

                                posologiaDetails.removeAt(index)
                                bottonePremuto.intValue--
                                elementoSelezionato.value = "${bottonePremuto.intValue}x"
                                onValueChange(posologiaDetails.toList())


                            }

                        ) {
                            Text(text = "Elimina")
                        }
                    }
                },
                onValueChange = { updatedDetail ->
                    posologiaDetails[index] = updatedDetail
                    onValueChange(posologiaDetails.toList())
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

    }
}


@Composable
fun InserisciPosologiaUiElements(
    posologiaDetail: PosologiaDetails,
    terapiaDetail: TerapiaDetails,
    modifier: Modifier = Modifier,
    onValueChange: (PosologiaDetails) -> Unit = { },
    elimina: @Composable () -> Unit
) {
    val settimana = listOf("Lu", "Ma", "Me", "Gi", "Ve", "Sa", "Do")

    // Log per debug

    ExpandableCardFunction(
        espansa = true,
        icon = getIcon(terapiaDetail.tipo),
        titolo = {
            InserisciOra(
                label = "",
                time = posologiaDetail.ora,
                onTimeChange = {
                    onValueChange(posologiaDetail.copy(ora = it))
                }
            )
        },
        sottotitolo = { SottoTitolo(text = terapiaDetail.nomeTerapia) },
        sottosottotitolo = {
            SottoSottoTitolo(
                text = " ${if (posologiaDetail.dose % 1 == 0.0) posologiaDetail.dose.toInt() else posologiaDetail.dose} ${terapiaDetail.forma}"
            )
        },
        boxContent = {
            if(terapiaDetail.frequenza == "Sett.") {



                    MultiSelectorButton(
                        items = settimana,
                        listaSelezionati = posologiaDetail.giorniSettimana,
                    ) { selectedItems ->
                        onValueChange(posologiaDetail.copy(giorniSettimana = selectedItems))
                    }


            }
            val doseString = remember { mutableStateOf(posologiaDetail.dose.toString()) }
            CampoDiTesto(
                text = if(doseString.value == "0.0") "" else doseString.value,
                onTextChange = {
                    val correctedInput = it.replace(',', '.')
                    doseString.value = correctedInput
                    val dose = correctedInput.toDoubleOrNull() ?: 0.0
                    onValueChange(posologiaDetail.copy(dose = dose))
                },
                placeholder = "Inserisci quantita'",
                tastierinoNumerico = true
            )
        },
        pulsanteFinaledestro = {
            elimina()
        }

    )

}


//fun calcolaOrari(
//    numeroFasce: Int,
//    oraInizio: LocalTime = LocalTime.of(6, 0),
//    oraFine: LocalTime = LocalTime.of(24, 0)
//): List<LocalTime> {
//    if (numeroFasce <= 0 || oraInizio.isAfter(oraFine)) return emptyList()
//    if (numeroFasce == 1) return listOf(oraInizio)
//
//    val durataTotale = Duration.between(oraInizio, oraFine)
//    val intervallo = durataTotale.dividedBy(numeroFasce - 1)
//
//    val orari = mutableListOf<LocalTime>()
//    var tempoCorrente = oraInizio
//
//    for (i in 0 until numeroFasce) {
//        orari.add(tempoCorrente)
//        tempoCorrente = tempoCorrente.plus(intervallo)
//    }
//
//    // Assicurati che l'ultimo orario non superi l'oraFine
//    if (orari.last() != oraFine && orari.size > 1) {
//        orari[orari.size - 1] = oraFine
//    }
//
//    return orari
//}
