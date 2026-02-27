package com.example.hcr0_4.ui.schermate.menuPersonalizzazioni

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hcr0_4.data.Posologia
import com.example.hcr0_4.data.PosologieRepository
import com.example.hcr0_4.data.Terapia
import com.example.hcr0_4.data.TerapieRepository
import com.example.hcr0_4.ui.schermate.home.PosologiaUiState

import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel to retrieve all items in the Room database.
 */
class PersonalizzazioniViewModel(
    private val terapieRepository: TerapieRepository,
    private val posologieRepository: PosologieRepository
) : ViewModel() {

    val personalizazzioniUiState: StateFlow<PersonalizzazioniUiState> =
        combine(
            terapieRepository.getAllTerapie(),
            posologieRepository.getAllPosologie()
        ) { terapie, posologie ->
            PersonalizzazioniUiState(terapiaList = terapie, posologiaList = posologie)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = PersonalizzazioniUiState()
            )



    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    suspend fun updateTerapia(terapia: Terapia) {
        viewModelScope.launch {
            terapieRepository.updateTerapia(terapia)
        }
    }

    suspend fun eliminaTerapia(terapia: Terapia) {
        viewModelScope.launch {
            terapieRepository.deleteTerapia(terapia)
        }
    }

}



/**
 * Ui State for HomeScreen
 */
data class PersonalizzazioniUiState(
    val terapiaList: List<Terapia> = listOf(),
    val posologiaList: List<Posologia> = listOf()
)


