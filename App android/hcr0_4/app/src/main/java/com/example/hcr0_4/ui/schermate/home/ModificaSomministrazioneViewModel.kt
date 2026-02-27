package com.example.hcr0_4.ui.schermate.home//package com.example.hcr0_4.ui.crud.modifica
//
//
//import android.util.Log
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.setValue
//import androidx.lifecycle.SavedStateHandle
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.hcr0_4.data.SomministrazioniRepository
//import kotlinx.coroutines.flow.filterNotNull
//import kotlinx.coroutines.flow.first
//import kotlinx.coroutines.launch
//
///**
// * ViewModel to retrieve and update an somministrazione from the [SomministrazionesRepository]'s data source.
// */
//class ModificaSomministrazioneViewModel(
//    savedStateHandle: SavedStateHandle,
//    private val somministrazioneRepository: SomministrazioniRepository
//) : ViewModel() {
//
//
//    /**
//     * Holds current somministrazione ui state
//     */
//    var somministrazioneUiState by mutableStateOf(SomministrazioneUiState())
//        private set
//
//    private val somministrazioneId: Int = checkNotNull(savedStateHandle[ModificaSomministrazioneDestination.somministrazioneIdArg])
////    private val somministrazioneId: Int = savedStateHandle["idSomministrazione"]!!
//
//    init {
//        Log.d("ModificaSomministrazioneViewModel", "Init somministrazioneId: ${somministrazioneId}")
//        viewModelScope.launch {
//            somministrazioneUiState = somministrazioneRepository.getSomministrazione(somministrazioneId)
//                .filterNotNull()
//                .first()
//                .toSomministrazioneUiState(true)
//        }
//    }
//
//
//    suspend fun updateSomministrazione() {
//        if (validateInput(somministrazioneUiState.somministrazioneDetails)) {
//            somministrazioneRepository.updateSomministrazione(somministrazioneUiState.somministrazioneDetails.toSomministrazione())
//        }
//    }
//    suspend fun deleteSomministrazione(){
//        somministrazioneRepository.deleteSomministrazione(somministrazioneUiState.somministrazioneDetails.toSomministrazione())
//
//    }
//
//    /**
//     * Updates the [somministrazioneUiState] with the value provided in the argument. This method also triggers
//     * a validation for input values.
//     */
//    fun updateUiState(somministrazioneDetails: SomministrazioneDetails) {
//        somministrazioneUiState =
//            SomministrazioneUiState(somministrazioneDetails = somministrazioneDetails, isEntryValid = validateInput(somministrazioneDetails))
//    }
//
//    private fun validateInput(uiState: SomministrazioneDetails = somministrazioneUiState.somministrazioneDetails): Boolean {
//        return with(uiState) {
//            true
//        }
//    }
//}
