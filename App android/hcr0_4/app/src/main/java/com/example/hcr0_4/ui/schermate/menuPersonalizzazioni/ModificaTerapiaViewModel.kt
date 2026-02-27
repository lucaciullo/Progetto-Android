package com.example.hcr0_4.ui.schermate.menuPersonalizzazioni

import com.example.hcr0_4.data.TerapieRepository


import androidx.lifecycle.ViewModel

/**
 * ViewModel to retrieve and update an Terapia from the [TerapiasRepository]'s data source.
 */
class ModificaTerapiaViewModel(
    private val terapiaRepository: TerapieRepository
) : ViewModel() {

}
//
//
//    /**
//     * Holds current Terapia ui state
//     */
//    var TerapiaUiState by mutableStateOf(TerapiaUiState())
//        private set
//
////    private val TerapiaId: Int = savedStateHandle["idTerapia"]!!
//
//    init {
//        Log.d("ModificaTerapiaViewModel", "Init TerapiaId: ${idTerapiaArg.idTerapia}")
//        viewModelScope.launch {
//            TerapiaUiState = terapiaRepository.getTerapia(
//                idTerapiaArg.idTerapia)
//                .filterNotNull()
//                .first()
//                .toTerapiaUiState(true)
//        }
//    }
//
//
//    suspend fun updateTerapia() {
//        if (validateInput(TerapiaUiState.terapiaDetails)) {
//            terapiaRepository.updateTerapia(TerapiaUiState.terapiaDetails.toTerapia())
//        }
//    }
//    suspend fun deleteTerapia(){
//        terapiaRepository.deleteTerapia(TerapiaUiState.terapiaDetails.toTerapia())
//
//    }
//
//    /**
//     * Updates the [TerapiaUiState] with the value provided in the argument. This method also triggers
//     * a validation for input values.
//     */
//    fun updateUiStateTerapia(TerapiaDetails: TerapiaDetails) {
//        TerapiaUiState =
//            TerapiaUiState(terapiaDetails = TerapiaDetails, isEntryValid = validateInput(TerapiaDetails))
//    }
//
//    private fun validateInput(uiState: TerapiaDetails = TerapiaUiState.terapiaDetails): Boolean {
//        return with(uiState) {
//            true
//        }
//    }
//}
