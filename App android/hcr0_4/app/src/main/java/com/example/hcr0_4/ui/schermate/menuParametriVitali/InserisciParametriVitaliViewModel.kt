package com.example.hcr0_4.ui.schermate.menuParametriVitali


import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.hcr0_4.data.*

class InserisciParametriVitaliViewModel(
    private val parametriVitaliRepository: ParametriVitaliRepository
):ViewModel() {

    var parametriVitaliUiState by mutableStateOf(ParametriVitaliUiState())
        private set

    fun updateUiStateParametriVitali(parametriVitaliDetails: ParametriVitaliDetails?) {
        parametriVitaliUiState = parametriVitaliUiState.copy(
            parametriVitaliDetails = parametriVitaliDetails!!,
            isEntryValid = validateParametriVitaliInput(parametriVitaliDetails)
        )
    }

    suspend fun saveParametriVitali() {
        if (parametriVitaliUiState.isEntryValid) {
            if (parametriVitaliUiState.parametriVitaliDetails.unitaMisura == "Passi"){
                parametriVitaliRepository.insertParametroVitale(ParametroVitale(
                    idParametro = 0,
                    nomeParametro = parametriVitaliUiState.parametriVitaliDetails.nomeParametro,
                    unitaMisura = parametriVitaliUiState.parametriVitaliDetails.unitaMisura,
                    valoriAccumulati = true
                ))
            }
            else{
                parametriVitaliRepository.insertParametroVitale(parametriVitaliUiState.parametriVitaliDetails.toParametriVitali())

            }
        }

    }

    private fun validateParametriVitaliInput(details: ParametriVitaliDetails): Boolean {
        // Implement your validation logic here
        if (
            details.nomeParametro.isNotBlank() && details.unitaMisura.isNotBlank()
        ) {
            return true
        }
        return false
    }
}




data class ParametriVitaliDetails(
    val idParametro: Int = 0,
    val nomeParametro: String = "",
    val unitaMisura: String = "",
    val valoriAccumulati: Boolean = false
)

data class ParametriVitaliUiState(
    val parametriVitaliDetails: ParametriVitaliDetails = ParametriVitaliDetails(),
    val isEntryValid: Boolean = false
)

fun ParametriVitaliDetails.toParametriVitali(): ParametroVitale = ParametroVitale(
    idParametro = idParametro,
    nomeParametro = nomeParametro,
    unitaMisura = unitaMisura,
    valoriAccumulati = valoriAccumulati
)

fun ParametroVitale.toParametriVitaliDetails(): ParametriVitaliDetails = ParametriVitaliDetails(
    idParametro = idParametro,
    nomeParametro = nomeParametro,
    unitaMisura = unitaMisura,
    valoriAccumulati = valoriAccumulati

)




