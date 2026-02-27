// In `app/src/main/java/com/example/hcr0_4/ui/schermate/menuParametriVitali/StoricoParametriVitaliViewModel.kt`
package com.example.hcr0_4.ui.schermate.menuParametriVitali

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import com.example.hcr0_4.data.Misurazione
import com.example.hcr0_4.data.MisurazioniRepository
import com.example.hcr0_4.data.ParametriVitaliRepository
import com.example.hcr0_4.data.ParametroVitale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.Locale

class StoricoParametriVitaliViewModel(
    private val pvRepository: ParametriVitaliRepository,
    private val misurazioniRepository: MisurazioniRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val idParametro: Int = savedStateHandle[InserisciMisurazioniDestination.idPvArg] ?: throw IllegalArgumentException("idParametro is required")

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    private val _maxMisurazione = MutableStateFlow(0f)
    val maxMisurazione: StateFlow<Float> = _maxMisurazione

    private val _weeklyData = MutableStateFlow<List<Pair<String, String>>>(emptyList())
    val weeklyData: StateFlow<List<Pair<String, String>>> = _weeklyData

    init {
        viewModelScope.launch {
            _maxMisurazione.value = withContext(Dispatchers.IO) {
                misurazioniRepository.getMaxMisurazioniPv(idParametro)
            }
        }
    }

    fun onNavigateDays(offset: Int) {
        _selectedDate.value = _selectedDate.value.plusDays(offset.toLong())
        updateWeeklyData() // Refresh data when date changes
    }

    fun onNavigateWeeks(offset: Int) {
        _selectedDate.value = _selectedDate.value.plusWeeks(offset.toLong())
        updateWeeklyData() // Refresh data when week changes
    }

    fun onNavigateMonths(offset: Int) {
        _selectedDate.value = _selectedDate.value.plusMonths(offset.toLong())
        updateWeeklyData() // Refresh data when month changes
    }

    fun onDateSelected(date: LocalDate) {
        _selectedDate.value = date
        updateWeeklyData() // Refresh data when a new date is selected
    }

 fun updateWeeklyData() {
    viewModelScope.launch {
        val date = _selectedDate.value
        val startOfWeek = date.with(WeekFields.of(Locale.getDefault()).firstDayOfWeek)
        val endOfWeek = startOfWeek.plusDays(6)
        val weekData = mutableListOf<Pair<String, String>>()

        generateSequence(startOfWeek) { it.plusDays(1) }
            .takeWhile { it <= endOfWeek }
            .forEach { day ->
                val dayMeasurementsFlow: Flow<List<Misurazione>> = withContext(Dispatchers.IO) {
                    misurazioniRepository.getAllMisurazioneByParametroVitale(idParametro, day)
                }
                val dayMeasurements = dayMeasurementsFlow.first() // Collect the flow to get the list
                val dayValue = if (dayMeasurements.isNotEmpty()) {
                    val parametroFlow = withContext(Dispatchers.IO) {
                        pvRepository.getParametroVitale(idParametro)
                    }
                    val parametro = parametroFlow.first()
                    if (parametro!!.valoriAccumulati) {
                        dayMeasurements.sumOf { it.valore }.toString()
                    } else {
                        dayMeasurements.map { it.valore }.average().toString()
                    }
                } else {
                    "Nessuna misurazione"
                }
                val dayLabel = day.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault()) + " " +
                        day.format(DateTimeFormatter.ofPattern("dd-MM"))
                weekData.add(dayLabel to dayValue)
            }

        _weeklyData.value = weekData
    }
}
    @OptIn(ExperimentalCoroutinesApi::class)
    val storicoUiState: StateFlow<StoricoUiState> = selectedDate
        .flatMapLatest { date ->
            val misurazioniFlow = misurazioniRepository.getAllMisurazioneByParametroVitale(idParametro, date)
            val parametroFlow = pvRepository.getParametroVitale(idParametro)

            combine(misurazioniFlow, parametroFlow) { misurazioni, parametro ->
                StoricoUiState(misurazioni, parametro!!)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = StoricoUiState()
        )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class StoricoUiState(val misurazioniList: List<Misurazione> = listOf(), val parametro: ParametroVitale = ParametroVitale(nomeParametro = "", unitaMisura = "", idParametro = 0, valoriAccumulati = false))