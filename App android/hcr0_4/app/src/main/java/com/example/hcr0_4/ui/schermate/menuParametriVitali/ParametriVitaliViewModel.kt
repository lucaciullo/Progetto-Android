package com.example.hcr0_4.ui.schermate.menuParametriVitali

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import com.example.hcr0_4.data.Misurazione
import com.example.hcr0_4.data.MisurazioniRepository
import com.example.hcr0_4.data.ParametriVitaliRepository
import com.example.hcr0_4.data.ParametroVitale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate


class ParametriVitaliViewModel(
    private val pvRepository: ParametriVitaliRepository,
    private val misurazioniRepository: MisurazioniRepository
) : ViewModel() {

    val listaMisurazioni: StateFlow<listaMisurazioni> =
        misurazioniRepository.getMisurazioniByData(LocalDate.now())
            .map { listaMisurazioni(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = listaMisurazioni()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    val pvUiState: StateFlow<PvUiState> = pvRepository.getAllParametriVitali()
        .map { PvUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = PvUiState()
        )
}

data class listaMisurazioni(val misurazioniList: List<Misurazione> = listOf())
data class PvUiState(val pvList: List<ParametroVitale> = listOf())




data class Measurement(
    val value: Float,
    val dateTime: String // Puoi utilizzare una data e ora nel formato che preferisci
)

