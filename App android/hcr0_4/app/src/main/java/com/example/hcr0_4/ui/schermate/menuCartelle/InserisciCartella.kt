package com.example.hcr0_4.ui.schermate.menuCartelle

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hcr0_4.HcrTopAppBar
import com.example.hcr0_4.R
import com.example.hcr0_4.ui.AppViewModelProvider
import com.example.hcr0_4.ui.navigazione.NavigationDestination
import com.example.hcr0_4.ui.theme.components.CampoDiTesto
import com.example.hcr0_4.ui.theme.components.Pulsante
import kotlinx.coroutines.launch


object InserisciCartellaDestination : NavigationDestination {
    override val route = "InserisciCartella"
    override val titleRes = R.string.action_add
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InserisciCartella(
    viewModel: InserisciCartellaViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onNavigateUp: () -> Unit,
    navigateBack: () -> Unit,
    canNavigateBack: Boolean = true,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val cartellaUiState = viewModel.cartellaUiState
    val onValueChangeCartella = viewModel::updateUiStateCartella

    val context = LocalContext.current // Ottieni il contesto dell'applicazione


    Scaffold(
        topBar = {
            HcrTopAppBar(
                title = "Inserisci dati terapia",
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        },
        modifier = modifier
    ) { innerPadding ->
        InserisciCartellaBody(
            cartellaUiState = cartellaUiState,
            onValueChange = onValueChangeCartella,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveCartella(context) // Passa il contesto
                    navigateBack()
                }
            },
            modifier = Modifier.padding(innerPadding),
            isEntryValid = cartellaUiState.isEntryValid
        )
    }
}


@Composable
fun InserisciCartellaBody(
    cartellaUiState: CartellaUiState,
    isEntryValid : Boolean,
    onValueChange: (CartellaDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {


    Column(modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        InserisciCartellaUiElements(
            cartellaDetails = cartellaUiState.cartellaDetails,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        if(isEntryValid){
            Pulsante(
                onClick = {
                    onSaveClick()
                }
            ) {
                Text(text = stringResource(R.string.save_action))
            }
        }

    }
}

@Composable
fun InserisciCartellaUiElements(
    cartellaDetails: CartellaDetails,
    onValueChange: (CartellaDetails) -> Unit,
    modifier: Modifier = Modifier
) {
        CampoDiTesto(
            text = cartellaDetails.nome,
            onTextChange = { onValueChange(cartellaDetails.copy(nome = it)) },
            placeholder = "Nome cartella"


        )


}

