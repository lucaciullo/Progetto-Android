package com.example.hcr0_4.ui.schermate.home//package com.example.hcr0_4.ui.crud.modifica
//
//import android.util.Log
//import androidx.compose.foundation.layout.calculateStartPadding
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Scaffold
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalLayoutDirection
//import androidx.compose.ui.res.stringResource
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.hcr0_4.HcrTopAppBar
//import com.example.hcr0_4.R
//import com.example.hcr0_4.ui.AppViewModelProvider
//import com.example.hcr0_4.ui.crud.inserisci.posologia.InserisciSomministrazioneBody
//import com.example.hcr0_4.ui.navigation.NavigationDestination
//import com.example.hcr0_4.ui.theme.components.ConfirmationDialog
//
//import kotlinx.coroutines.launch
//import java.time.LocalDate
//import java.time.LocalTime
//
//object ModificaSomministrazioneDestination : NavigationDestination {
//    override val route = "modificaSomministrazione"
//    override val titleRes = R.string.edit_item_title
//    const val somministrazioneIdArg = "idSomministrazione"
//    val routeWithArgs = "$route/{$somministrazioneIdArg}"
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ModificaSomministrazione(
//    navigateBack: () -> Unit,
//    onNavigateUp: () -> Unit,
//    modifier: Modifier = Modifier,
//    viewModel: SomministrazioneViewModel = viewModel(factory = AppViewModelProvider.Factory)
//) {
//    Log.d("HcrSomministrazione", "Modifica somministrazione ID Somministrazione : ${ModificaSomministrazioneDestination.somministrazioneIdArg}")
//    val coroutineScope = rememberCoroutineScope()
//    val showDialog = remember { mutableStateOf(false) }
//
//    Scaffold(
//        topBar = {
//            HcrTopAppBar(
//                title = stringResource(ModificaSomministrazioneDestination.titleRes),
//                canNavigateBack = true,
//                navigateUp = onNavigateUp
//            )
//        },
//        modifier = modifier
//    ) { innerPadding ->
//
//        InserisciSomministrazioneBody(
//            somministrazioneUiState = viewModel.somministrazioneUiState,
//            onValueChangeSomministrazione = viewModel::updateUiState,
//            onSaveClick = {
//                coroutineScope.launch {
//                    viewModel.somministrazioneUiState.somministrazioneDetails.copy(
//                        dataPresa = LocalDate.now(),
//                        oraPresa = LocalTime.now()
//                    )
//                    viewModel.updateSomministrazione()
//                    navigateBack()
//                }
//            },
//            onDeleteClick = {
//                showDialog.value = true
//            },
//            modifier = Modifier
//                .padding(
//                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
//                    top = innerPadding.calculateTopPadding()
//                )
//        )
//
//
//
//        if (showDialog.value) {
//            ConfirmationDialog(
//                onConfirm = {
//                    coroutineScope.launch {
//                        viewModel.deleteSomministrazione()
//                        showDialog.value = false
//                        navigateBack()
//                    }
//                },
//                onCancel = { showDialog.value = false }
//            )
//        }
//    }
//}
//
//
