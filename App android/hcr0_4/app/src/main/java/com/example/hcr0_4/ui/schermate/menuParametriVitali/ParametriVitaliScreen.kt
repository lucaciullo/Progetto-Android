package com.example.hcr0_4.ui.schermate.menuParametriVitali


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.hcr0_4.HcrTopAppBar
import com.example.hcr0_4.R
import com.example.hcr0_4.data.Misurazione
import com.example.hcr0_4.data.ParametroVitale
import com.example.hcr0_4.ui.AppViewModelProvider
import com.example.hcr0_4.ui.navigazione.NavigationDestination
import com.example.hcr0_4.ui.theme.components.ExpandableCardFunction
import com.example.hcr0_4.ui.theme.components.Pulsante
import com.example.hcr0_4.ui.theme.components.Riga
import com.example.hcr0_4.ui.theme.components.SottoSottoTitolo
import com.example.hcr0_4.ui.theme.components.SottoTitolo
import com.example.hcr0_4.ui.theme.components.Titolo
import com.example.hcr0_4.ui.theme.components.aggiustaDouble
import com.example.hcr0_4.ui.theme.components.getIcon


object ParametriVitaliDestination : NavigationDestination {
    override val route = "ParametriVitali"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParametriVitaliScreen(

    navigateToAggiungiMisurazione: (Int) -> Unit,
    navigateToStoricoPv: (Int) -> Unit,
    navigateToInserisciPv: () -> Unit,
    modifier: Modifier = Modifier,
    viewModelPv: ParametriVitaliViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val pvUiState by viewModelPv.pvUiState.collectAsState()
    val misurazioniState by viewModelPv.listaMisurazioni.collectAsState()


    Scaffold(
        topBar = {
            Column {
                HcrTopAppBar(
                    title = "Parametri Vitali",
                    canNavigateBack = false
                )
            }
        },
        floatingActionButton = {
            Pulsante(
                onClick = navigateToInserisciPv,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.item_entry_title),
                    tint = Color.White
                )
            }
        }
    ) { innerPadding ->


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {


            Spacer(modifier = Modifier.height(16.dp))

            // List of Vital Parameter Cards

            PvBody(
                pvList = pvUiState.pvList,
                allMisurazioni = misurazioniState.misurazioniList,
                onAggiungiMisurazioneClick = navigateToAggiungiMisurazione,
                onStoricoClick = navigateToStoricoPv,
                modifier = modifier.fillMaxSize()
            )

        }

    }

}

@Composable
private fun PvBody(
    pvList: List<ParametroVitale>,
    allMisurazioni: List<Misurazione>,
    onAggiungiMisurazioneClick: (Int) -> Unit,
    onStoricoClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        if (pvList.isEmpty()) {
            Text(
                text = "Non ci sono parametri vitali",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
            )
        } else {
            val scrollableState = rememberScrollState()
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollableState)
            ) {
                pvList.forEach { pV ->

                    HcrPv(
                        pV = pV,
                        misurazioniPv = allMisurazioni.filter { it.idParametro == pV.idParametro },
                        onAggiungiMisurazioneClick = onAggiungiMisurazioneClick,
                        onStoricoClick = onStoricoClick
                    )
                }


                Spacer(modifier = Modifier.height(16.dp))
                Spacer(modifier = Modifier.height(72.dp))


            }

        }
    }
}

@Composable
private fun HcrPv(
    pV: ParametroVitale,
    misurazioniPv: List<Misurazione>,
    modifier: Modifier = Modifier,
    onAggiungiMisurazioneClick: (Int) -> Unit,
    onStoricoClick: (Int) -> Unit
) {

    ExpandableCardFunction(
        titolo = { Titolo(text = pV.nomeParametro) },
        sottotitolo = {
            Riga {
                SottoTitolo(
                    text = misurazioniPv.firstOrNull()?.let {
                        "${aggiustaDouble(it.valore)} ${pV.unitaMisura}"
                    } ?: "Nessuna misurazione"
                )
            }
        },
        icon = getIcon(tipo = pV.nomeParametro),
        pulsanteFinaledestro = {
            Pulsante(
                onClick = {
                    onAggiungiMisurazioneClick(pV.idParametro)
                },
                testoPulsante = "Nuova mis."
            )
        },
        pulsanteFinaleSinistro = {
            Pulsante(
                onClick = {
                    onStoricoClick(pV.idParametro)
                },
                testoPulsante = "Storico"
            )
        }
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        if (misurazioniPv.isNotEmpty()) {
            val totale = aggiustaDouble(misurazioniPv.sumOf { it.valore })
            val min = aggiustaDouble(misurazioniPv.minOf { it.valore })
            val max = aggiustaDouble(misurazioniPv.maxOf { it.valore })
            val media = aggiustaDouble(misurazioniPv.map { it.valore }.average())

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    horizontalAlignment = Alignment.Start
                ) {
                    SottoSottoTitolo(text = if (pV.valoriAccumulati) totale else media)
                    Text(text = if (pV.valoriAccumulati) "Tot" else "Media")
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SottoSottoTitolo(text = min)
                    Text(text = "Min")
                }
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    SottoSottoTitolo(text = max)
                    Text(text = "Max")
                }
            }
        }

    }
}



@Composable
fun HeaderSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = "Your daily goals", style = MaterialTheme.typography.titleMedium)
            // Additional header content here
        }
    }
}
fun getPrimaMisurazione(misurazioni: List<Misurazione>, idParametro: Int): String {
    var primaMisurazione = "Nessuna misurazione"
    for (misurazione in misurazioni) {
        if (misurazione.idParametro == idParametro) {
            primaMisurazione = "${misurazione.valore} "
            break
        }
    }
    return primaMisurazione
}
