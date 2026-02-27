package com.example.hcr0_4.ui.schermate.menuParametriVitali


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.hcr0_4.data.Misurazione
import com.example.hcr0_4.data.MisurazioniRepository
import java.time.LocalDate
import java.time.LocalTime

class InserisciMisurazioniViewModel(
    private val misurazioni: MisurazioniRepository,
    savedStateHandle: SavedStateHandle

): ViewModel(){

    var misurazioniUiState by mutableStateOf(MisurazioniUiState())
        private set

    private val idParametro: Int = checkNotNull(savedStateHandle[InserisciMisurazioniDestination.idPvArg])
    fun updateUiStateMisurazioni(misurazioniDetails: MisurazioniDetails){
        misurazioniUiState = misurazioniUiState.copy(
            misurazioniDetails = misurazioniDetails,
            isEntryValid = validateMisurazioniInput(misurazioniDetails)
        )
    }

    suspend fun saveMisurazioni(){
        if(misurazioniUiState.isEntryValid){
            misurazioni.insertMisurazione(misurazioniUiState.misurazioniDetails.toMisurazioni(idParametro))
        }
    }


    private fun validateMisurazioniInput(details: MisurazioniDetails): Boolean {
        // Implement your validation logic here
        if (
            details.valore > 0
        ) {
            return true
        }
        return false
    }
}




data class MisurazioniDetails(
    val idMisurazione: Int = 0,
    val idParametro: Int = 0,
    val valore: Double = 0.0,
    val data: LocalDate = LocalDate.now(),
    val ora: LocalTime = LocalTime.now()
)

public data class MisurazioniUiState(
    val misurazioniDetails: MisurazioniDetails = MisurazioniDetails(),
    val isEntryValid: Boolean = false
)

fun MisurazioniDetails.toMisurazioni(idParametro: Int): Misurazione = Misurazione(
    idMisurazione = idMisurazione,
    idParametro = idParametro,
    valore = valore,
    data = data,
    ora = ora
)

fun Misurazione.toMisurazioniDetails(idParametro: Int): MisurazioniDetails = MisurazioniDetails(
    idMisurazione = idMisurazione,
    idParametro = idParametro,
    valore = valore,
    data =data,
    ora = ora
)