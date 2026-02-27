package com.example.hcr0_4.ui.schermate.menuParametriVitali

import com.example.hcr0_4.ui.theme.components.DropdownMenuItems


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hcr0_4.HcrTopAppBar
import com.example.hcr0_4.R
import com.example.hcr0_4.ui.AppViewModelProvider
import com.example.hcr0_4.ui.navigazione.NavigationDestination
import com.example.hcr0_4.ui.theme.HcrGreen
import com.example.hcr0_4.ui.theme.components.ButtonSelector
import com.example.hcr0_4.ui.theme.components.CampoDiTesto
import com.example.hcr0_4.ui.theme.components.Pulsante

import kotlinx.coroutines.launch


object InserisciPvDestination : NavigationDestination {
    override val route = "inserisciPv"
    override val titleRes = R.string.action_add
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InserisciPv(
    viewModel: InserisciParametriVitaliViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true

) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            HcrTopAppBar(
                title = "Inserisci dati Parametro Vitale",
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    )  {innerPadding ->
        InserisciPvBody(
            pVUiState = viewModel.parametriVitaliUiState,
            onValueChangePv =  viewModel:: updateUiStateParametriVitali ,
            onSaveClick = {

                //saveTerapia
                coroutineScope.launch {
                    viewModel.saveParametriVitali()
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

@Composable
fun InserisciPvBody(
    pVUiState: ParametriVitaliUiState,
    onValueChangePv: (ParametriVitaliDetails) -> Unit = {  },
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large))

    ) {
        InserisciPvInputForm(
            pVDetails = pVUiState.parametriVitaliDetails,
            onValueChangePv = onValueChangePv,
            modifier = Modifier.fillMaxWidth()
        )
        if(pVUiState.isEntryValid) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                Pulsante(
                    onClick = onSaveClick,

                    testoPulsante = "Salva"
                )
            }
        }

    }
}

@Composable
fun InserisciPvInputForm(
    pVDetails: ParametriVitaliDetails,
    modifier: Modifier = Modifier,
    onValueChangePv: (ParametriVitaliDetails) -> Unit = {  },
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        InserisciPvPv(pVDetails, onValueChangePv)

    }
}

@Composable
fun InserisciPvPv(
    pVDetails: ParametriVitaliDetails,
    onValueChange: (ParametriVitaliDetails) -> Unit
) {
    val unitaMisura = listOf("Passi", "Bpm", "O2", "Temperatura", "Pressione", "Glicemia", "Peso", "Altezza", "SpO2", "Saturazione", "Respiri", "Saturazione O2")
    CampoDiTesto(
        text = pVDetails.nomeParametro,
        onTextChange = { onValueChange(pVDetails.copy(nomeParametro = it)) },
        placeholder = "Nome Parametro vitale"
    )


    ButtonSelector(items = unitaMisura,
        elementoSelezionato = pVDetails.unitaMisura,
        onSelectionChanged = { onValueChange(pVDetails.copy(unitaMisura = it)) })





}

