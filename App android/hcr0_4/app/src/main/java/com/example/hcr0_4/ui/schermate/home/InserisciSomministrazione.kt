package com.example.hcr0_4.ui.schermate.home//package com.example.hcr0_4.ui.crud.inserisci.posologia
//
//import android.util.Log
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.stringResource
//import com.example.hcr0_4.R
//import com.example.hcr0_4.ui.navigation.NavigationDestination
//import com.example.hcr0_4.ui.theme.components.DropdownMenuItems
//import com.example.hcr0_4.ui.theme.components.InserisciData
//import com.example.hcr0_4.ui.theme.components.InserisciOra
//
//
//object InserisciSomministrazioneDestination : NavigationDestination {
//    override val route = "InserisciSomministrazione"
//    override val titleRes = R.string.action_add
//
//}
//
//
//
//
//
//@Composable
//fun InserisciSomministrazioneBody(
//    somministrazioneUiState: SomministrazioneUiState,
//    onValueChangeSomministrazione: (SomministrazioneDetails) -> Unit = {  },
//    onSaveClick: () -> Unit,
//    onDeleteClick: () -> Unit = {  },
//    modifier: Modifier = Modifier
//) {
//    Log.d("HcrSomministrazione", "Inserisci somministrazione body: ")
//    Column(modifier = modifier) {
//        InserisciSomministrazioneInputForm(
//            somministrazioneDetails = somministrazioneUiState.somministrazioneDetails,
//            onValueChangeSomministrazione = onValueChangeSomministrazione,
//            modifier = Modifier.fillMaxWidth()
//        )
//        Button(
//            onClick = onSaveClick,
//            enabled = somministrazioneUiState.isEntryValid,
//            shape = MaterialTheme.shapes.small,
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text(text = stringResource(R.string.save_action))
//        }
//        Button(onClick = onDeleteClick,) {
//            Text("Elimina")
//        }
//    }
//}
//
//@Composable
//fun InserisciSomministrazioneInputForm(
//    somministrazioneDetails: SomministrazioneDetails,
//    modifier: Modifier = Modifier,
//    onValueChangeSomministrazione: (SomministrazioneDetails) -> Unit = {  },
//    enabled: Boolean = true
//) {
//    Log.d("HcrSomministrazione", "Inserisci somministrazioneInputForm: ")
//    Column(
//        modifier = modifier) {
//
//            InserisciSomministrazioneUiElements(somministrazioneDetails, onValueChangeSomministrazione)
//
//            if (enabled) {
//            Text(
//                text = stringResource(R.string.required_fields)
//            )
//        }
//    }
//}
//
//
//
//
//
//@Composable
//fun InserisciSomministrazioneUiElements(
//    somministrazioneDetails: SomministrazioneDetails,
//    onValueChange: (SomministrazioneDetails) -> Unit
//) {
//    Log.d("HcrSomministrazione", "Inserisci somministrazioneSomministrazione: ")
//
//    val tipiStatoSomministrazione = listOf("Programmato", "Completato", "Non Completato", "Annullato", "In corso")
//
//    // Somministrazione - Data
//    InserisciData(
//        label = "Data Somministrazione",
//        date = somministrazioneDetails.dataSomministrazione,
//        onDateChange = {
//            onValueChange(somministrazioneDetails.copy(dataSomministrazione = it))
//        }
//    )
//
//
//    // Somministrazione - Ora
//    InserisciOra(
//        label = "Ora Somministrazione",
//        time = somministrazioneDetails.oraSomministrazione,
//        onTimeChange = {
//            onValueChange(somministrazioneDetails.copy(oraSomministrazione = it))
//        }
//    )
//
//    // Somministrazione - Stato Dropdown
//    DropdownMenuItems(
//        selectedValue = somministrazioneDetails.statoSomministrazione,
//        items = tipiStatoSomministrazione,
//        onItemSelected = { item ->
//            onValueChange(somministrazioneDetails.copy(statoSomministrazione = item))
//        },
//        testo = "Tipo somministrazione: "
//    )
//}
//
