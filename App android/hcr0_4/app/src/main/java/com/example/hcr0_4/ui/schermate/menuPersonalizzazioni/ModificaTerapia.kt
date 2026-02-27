package com.example.hcr0_4.ui.schermate.menuPersonalizzazioni

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hcr0_4.R
import com.example.hcr0_4.ui.AppViewModelProvider

import com.example.hcr0_4.ui.navigazione.NavigationDestination

object ModificaTerapiaDestination : NavigationDestination {
    override val route = "modificaTerapia"
    override val titleRes = R.string.edit_item_title
    const val terapiaIdArg = "idTerapia"
    val routeWithArgs = "$route/{$terapiaIdArg}"
}
//
//@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModificaTerapia(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ModificaTerapiaViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

}
//    Log.d("HcrSomministrazione", "Modifica somministrazione ID Somministrazione : ${ModificaSomministrazioneDestination.somministrazioneIdArg}")
//    val coroutineScope = rememberCoroutineScope()
//    val showDialog = remember { mutableStateOf(false) }
//
//    Scaffold(
//        topBar = {
//            HcrTopAppBar(
//                title = stringResource(ModificaTerapiaDestination.titleRes),
//                canNavigateBack = true,
//                navigateUp = onNavigateUp
//            )
//        },
//        modifier = modifier
//    ) { innerPadding ->
//
//        ModificaTerapiaBody(
//            terapiaUiState = viewModel.TerapiaUiState,
//            onValueChangeTerapia = viewModel::updateUiStateTerapia,
//            onModificaClick = {
//                coroutineScope.launch {
//                    viewModel.updateTerapia()
//                    navigateBack()
//                }
//            },
//            onDeleteClick = {
//                showDialog.value = true
//            },
//                modifier = Modifier
//                    .padding(
//                        start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
//                        top = innerPadding.calculateTopPadding()
//                    )
//        )
//
//
//        if (showDialog.value) {
//            DeleteConfirmationDialog(
//                onDeleteConfirm = {
//                    coroutineScope.launch {
//                        viewModel.deleteTerapia()
//                        showDialog.value = false
//                        navigateBack()
//                    }
//                },
//                onDeleteCancel = { showDialog.value = false }
//            )
//        }
//    }
//}
//
//@Composable
//fun ModificaTerapiaBody(
//    terapiaUiState: TerapiaUiState,
//    onValueChangeTerapia: (TerapiaDetails) -> Unit = {  },
//    onModificaClick: () -> Unit,
//    onDeleteClick: () -> Unit = {  },
//    modifier: Modifier = Modifier
//) {
//    Column(
//        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
//        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large))
//
//    ) {
//        ModificaTerapiaInputForum(
//            terapiaDetails = terapiaUiState.terapiaDetails,
//            onValueChangeTerapia = onValueChangeTerapia,
//            modifier = Modifier.fillMaxWidth()
//        )
//        Button(
//            onClick = onModificaClick,
//            enabled = terapiaUiState.isEntryValid,
//            shape = MaterialTheme.shapes.small,
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Modifica")
//        }
//        Button(onClick = onDeleteClick,) {
//            Text("Elimina")
//        }
//
//    }
//}
//
//@Composable
//fun ModificaTerapiaInputForum(
//    terapiaDetails: TerapiaDetails,
//    modifier: Modifier = Modifier,
//    onValueChangeTerapia: (TerapiaDetails) -> Unit,
//    enabled: Boolean = true
//) {
//    Column(
//        modifier = modifier,
//        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
//    ) {
//        ModificaTerapia(
//            terapiaDetails = terapiaDetails,
//            onValueChange = onValueChangeTerapia
//        )
//        if (enabled) {
//            Text(
//                text = stringResource(R.string.required_fields)
//            )
//        }
//    }
//}
//
//@Composable
//fun ModificaTerapia(
//    terapiaDetails: TerapiaDetails,
//    onValueChange: (TerapiaDetails) -> Unit
//) {
//    val statoTerapia = listOf("Attivo", "Disattivo", "Sospeso")
//    val tipiAvviso = listOf("Notifica", "Sveglia", "Off")
//
//
//    DropdownMenuItems(
//        selectedValue = terapiaDetails.statoTerapia,
//        items = statoTerapia,
//        onItemSelected = {item ->
//            onValueChange(terapiaDetails.copy(statoTerapia = item))
//        },
//        testo = "Stato Terapia:"
//    )
//
//
//
//
//    // Tipo Avviso Dropdown
//    DropdownMenuItems(
//        selectedValue = terapiaDetails.tipoAvviso,
//        items = tipiAvviso,
//        onItemSelected = { item ->
////            selectedTipoAvviso = item
//            onValueChange(terapiaDetails.copy(tipoAvviso = item))
//        },
//        testo = "Tipo Avviso: "
//    )
//
//
//}
//
