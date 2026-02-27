package com.example.hcr0_4.ui.schermate.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hcr0_4.SomministrazioneStatus
import com.example.hcr0_4.data.Somministrazione
import com.example.hcr0_4.data.SomministrazioneDettagliata
import com.example.hcr0_4.data.SomministrazioniRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

class HomeViewModel(private val somministrazioniRepository: SomministrazioniRepository) : ViewModel() {

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate

var somministrazioneGenerator: SomministrazioneGenerator = SomministrazioneGenerator(somministrazioniRepository)
    private set


fun onNavigateDays(offset: Int) {
        _selectedDate.value = _selectedDate.value.plusDays(offset.toLong())
    }

    fun quanteNeRimangono(){

    }

    fun onNavigateYears(offset: Int) {
        _selectedDate.value = _selectedDate.value.plusYears(offset.toLong())
    }

    // Add this code to the HomeViewModel class

//    private val _currentMonth = MutableStateFlow(LocalDate.now().withDayOfMonth(1))
//    val currentMonth: StateFlow<LocalDate> = _currentMonth



    // Add this function to the HomeViewModel class
    private fun updateSomministrazioneStatus(somministrazioneDList: List<SomministrazioneDettagliata>): List<SomministrazioneDettagliata> {
        return somministrazioneDList.map { somministrazione ->
            somministrazione.copy(
                statoSomministrazione = if(

                    somministrazione.statoSomministrazione != SomministrazioneStatus.COMPLETATA.toString() &&
                    (somministrazione.dataSomministrazione.isBefore(LocalDate.now()) ||
                            (somministrazione.dataSomministrazione.isEqual(LocalDate.now()) && somministrazione.oraSomministrazione.isBefore(LocalTime.now()))
                    )

                ) {

                    SomministrazioneStatus.NON_COMPLETATA.toString()

                } else {
                    somministrazione.statoSomministrazione

                },
                dosePresa = somministrazione.dose,
                formaPresa = somministrazione.forma,
            )
        }
    }
    fun updateSomministrazione(somministrazione: Somministrazione){
        viewModelScope.launch {
            try {
                somministrazioniRepository.updateSomministrazione(somministrazione)
                // Optionally, update the UI state if needed
            } catch (e: Exception) {
            }
        }
    }

    fun onPresaClick(somministrazioneDettagliata: SomministrazioneDettagliata) {
        val statoSomministrazione = if (somministrazioneDettagliata.statoSomministrazione == SomministrazioneStatus.COMPLETATA.toString()) {
            SomministrazioneStatus.PROGRAMMATA.toString()
        } else {
            SomministrazioneStatus.COMPLETATA.toString()
        }

        val updatedSomministrazioneDettagliata = if (statoSomministrazione == SomministrazioneStatus.COMPLETATA.toString()) {
            somministrazioneDettagliata.copy(
                dataPresa = LocalDate.now(),
                oraPresa = LocalTime.now(),
                statoSomministrazione = statoSomministrazione,
                formaPresa = somministrazioneDettagliata.forma,
                dosePresa = somministrazioneDettagliata.dose
            )
        } else {
            somministrazioneDettagliata.copy(
                dataPresa = null,
                oraPresa = null,
                statoSomministrazione = statoSomministrazione,
                formaPresa = "",
                dosePresa = 0.0
            )
        }

        val updatedSomministrazione = convertToSomministrazione(updatedSomministrazioneDettagliata)
        viewModelScope.launch {
            aggiornaSomministrazione(updatedSomministrazione)
        }
    }

    fun convertToSomministrazione(somministrazioneDettagliata: SomministrazioneDettagliata): Somministrazione {
        return Somministrazione(
            idSomministrazione = somministrazioneDettagliata.idSomministrazione,
            dataSomministrazione = somministrazioneDettagliata.dataSomministrazione,
            oraSomministrazione = somministrazioneDettagliata.oraSomministrazione,
            statoSomministrazione = somministrazioneDettagliata.statoSomministrazione,
            dataPresa = somministrazioneDettagliata.dataPresa,
            oraPresa = somministrazioneDettagliata.oraPresa,
            idPosologia = somministrazioneDettagliata.idPosologia,
            formaPresa = somministrazioneDettagliata.formaPresa,
            dosePresa = somministrazioneDettagliata.dosePresa
        )
    }
    suspend fun aggiornaSomministrazione(somministrazione: Somministrazione) {
        viewModelScope.launch {
            try {
                somministrazioniRepository.updateSomministrazione(somministrazione)
                // Optionally, update the UI state if needed
            } catch (e: Exception) {
            }
        }
    }

    // Update the homeUiState to use the new function
    @OptIn(ExperimentalCoroutinesApi::class)
    val homeUiState: StateFlow<HomeUiState> = selectedDate
        .flatMapLatest { date ->
            somministrazioniRepository.getSomministrazioneByData(date)
                .map { HomeUiState(updateSomministrazioneStatus(it)) }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = HomeUiState()
        )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    fun onDateSelected(date: LocalDate) {
        _selectedDate.value = date
    }

    fun onNavigateMonths(offset: Int) {
        _selectedDate.value = _selectedDate.value.plusMonths(offset.toLong())

    }


}


data class HomeUiState(val somministrazioneDList: List<SomministrazioneDettagliata> = listOf())
