package com.example.hcr0_4.ui.schermate.menuParametriVitali

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hcr0_4.HcrTopAppBar
import com.example.hcr0_4.R
import com.example.hcr0_4.ui.AppViewModelProvider
import com.example.hcr0_4.ui.navigazione.NavigationDestination
import com.example.hcr0_4.ui.theme.components.CampoDiTesto
import com.example.hcr0_4.ui.schermate.menuParametriVitali.InserisciMisurazioniViewModel
import com.example.hcr0_4.ui.theme.components.Pulsante
import kotlinx.coroutines.launch



object InserisciMisurazioniDestination : NavigationDestination {
    override val route = "inserisciMisurazioni"
    override val titleRes = R.string.action_add
    const val idPvArg = "idParametroVitale"
    val routeWithArgs = "$route/{$idPvArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InserisciMisurazioni(
    viewModel: InserisciMisurazioniViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true

) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            HcrTopAppBar(
                title = "Inserisci dati misurazioni",
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    )  {innerPadding ->
        InserisciMisurazioniBody(
            misurazioniUiState = viewModel.misurazioniUiState,
            onValueChangeMisurazioni =  viewModel:: updateUiStateMisurazioni ,
            onSaveClick = {

                //saveTerapia
                coroutineScope.launch {
                    viewModel.saveMisurazioni()
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
fun InserisciMisurazioniBody(
    misurazioniUiState: MisurazioniUiState,
    onValueChangeMisurazioni: (MisurazioniDetails) -> Unit = {  },
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large))

    ) {
        InserisciMisurazioniInputForm(
            misurazioniDetails = misurazioniUiState.misurazioniDetails,
            onValueChangeMisurazioni = onValueChangeMisurazioni,
            modifier = Modifier.fillMaxWidth()
        )

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

@Composable
fun InserisciMisurazioniInputForm(
    misurazioniDetails: MisurazioniDetails,
    modifier: Modifier = Modifier,
    onValueChangeMisurazioni: (MisurazioniDetails) -> Unit = {  },
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        val valoreString = remember { mutableStateOf(misurazioniDetails.valore.toString()) }
        CampoDiTesto(
            text = if(valoreString.value == "0.0") "" else valoreString.value,
           onTextChange = {
    val correctedInput = it.replace(',', '.')
               valoreString.value = correctedInput
    val valore = correctedInput.toDoubleOrNull() ?: 0.0
        onValueChangeMisurazioni(misurazioniDetails.copy(valore = valore))

},
            placeholder = "Inserisci valore",
            tastierinoNumerico = true
        )
    }
}


