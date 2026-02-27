package com.example.hcr0_4.ui.schermate.menuParametriVitali

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hcr0_4.CardType
import com.example.hcr0_4.HcrTopAppBar
import com.example.hcr0_4.R
import com.example.hcr0_4.data.Misurazione
import com.example.hcr0_4.data.ParametroVitale
import com.example.hcr0_4.ui.AppViewModelProvider
import com.example.hcr0_4.ui.navigazione.NavigationDestination
import com.example.hcr0_4.ui.schermate.home.DateSelectionRow
import com.example.hcr0_4.ui.schermate.home.ExpandableButton
import com.example.hcr0_4.ui.schermate.home.HomeViewModel
import com.example.hcr0_4.ui.theme.components.ButtonSelector
import com.example.hcr0_4.ui.theme.components.GraficoParametriVitali
import com.example.hcr0_4.ui.theme.components.IconButtonWithIcon
import com.example.hcr0_4.ui.theme.components.Riga
import com.example.hcr0_4.ui.theme.components.SottoSottoTitolo
import com.example.hcr0_4.ui.theme.components.SottoTitolo
import com.example.hcr0_4.ui.theme.components.getIcon
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.Locale
import java.time.format.TextStyle

object StoricoPvDestination : NavigationDestination {
    override val route = "StoricoPv"
    override val titleRes = R.string.action_add
    const val idPvArg= "idParametroVitale"
    val routeWithArgs = "$route/{$idPvArg}"
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoricoParametriVitaliScreen(
    viewModel: StoricoParametriVitaliViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val storicoUiState by viewModel.storicoUiState.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val weeklyData by viewModel.weeklyData.collectAsState()
    var accumulatedDrag by remember { mutableStateOf(0f) }
    var isGestureEnabled by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()
    val periodoSelected = remember { mutableStateOf("Giorno") }
    val maxMisurazione by viewModel.maxMisurazione.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                val dragThreshold = 100f // Define a threshold for the drag amount

                detectHorizontalDragGestures { change, dragAmount ->
                    if (isGestureEnabled) {
                        accumulatedDrag += dragAmount
                        if (accumulatedDrag < -dragThreshold) {
                            when (periodoSelected.value) {
                                "Giorno" -> viewModel.onNavigateDays(1)
                                "Settimana" -> viewModel.onNavigateWeeks(1)
                                "Mese" -> viewModel.onNavigateMonths(1)
                            }
                            accumulatedDrag = 0f
                            isGestureEnabled = false
                            coroutineScope.launch {
                                delay(100) // Delay in milliseconds
                                isGestureEnabled = true
                            }
                        } else if (accumulatedDrag > dragThreshold) {
                            when (periodoSelected.value) {
                                "Giorno" -> viewModel.onNavigateDays(-1)
                                "Settimana" -> viewModel.onNavigateWeeks(-1)
                                "Mese" -> viewModel.onNavigateMonths(-1)
                            }
                            accumulatedDrag = 0f
                            isGestureEnabled = false
                            coroutineScope.launch {
                                delay(100) // Delay in milliseconds
                                isGestureEnabled = true
                            }
                        }
                        change.consume()
                    }
                }
            }
    ) {
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            HcrTopAppBar(title = storicoUiState.parametro.nomeParametro, canNavigateBack = true, navigateUp = navigateBack)
        }
        Row {
            ButtonSelector(items = listOf("Giorno", "Settimana"), elementoSelezionato = periodoSelected.value) {
                periodoSelected.value = it
                viewModel.updateWeeklyData()
            }
        }

        DateSelectionRow(
            tipo = periodoSelected.value,
            selectedDate = selectedDate,
            onDateSelected = viewModel::onDateSelected,
            onNavigateMonths = viewModel::onNavigateMonths
        )

        Spacer(modifier = Modifier.height(16.dp))
        AnimatedVisibility(
            visible = true,
            enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
            exit = slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
        ) {
            StoricoBody(
                misurazioniPv = storicoUiState.misurazioniList,
                max = maxMisurazione,
                tipoPeriodo = periodoSelected.value,
                pv = storicoUiState.parametro,
                weeklyData = weeklyData
            )
        }
    }
}

@Composable
fun StoricoBody(
    modifier: Modifier = Modifier,
    max: Float,
    tipoPeriodo: String,
    pv: ParametroVitale,
    weeklyData: List<Pair<String, String>>,
    misurazioniPv: List<Misurazione>
) {
    val scrollState = rememberScrollState()
    val datiGrafico = mutableListOf<Pair<String, Float>>()

    Column(
        modifier = Modifier.verticalScroll(scrollState)
    ) {
        if (tipoPeriodo == "Giorno") {
            if (misurazioniPv.isEmpty()) {
                Text("Nessuna misurazione")
            } else {
                misurazioniPv.forEach { misurazione ->
                    val label = misurazione.ora.format(DateTimeFormatter.ofPattern("HH:mm"))
                    val value = misurazione.valore
                    datiGrafico.add(label to value.toFloat())
                }
            }
        } else if (tipoPeriodo == "Settimana") {
            weeklyData.forEach { (label, value) ->
                val valueDouble = value.toDoubleOrNull() ?: 0.0
                datiGrafico.add(label to valueDouble.toFloat())
            }
        }

//        if (datiGrafico.isNotEmpty()) {
//            GraficoParametriVitali(
//                lista = datiGrafico,
//                max = max.toDouble(),
//                tipoPeriodo = tipoPeriodo
//            )
//        }

        if (tipoPeriodo == "Giorno") {
            if (misurazioniPv.isNotEmpty()) {
                misurazioniPv.forEach { misurazione ->
                    val label = misurazione.ora.format(DateTimeFormatter.ofPattern("HH:mm"))
                    val value = misurazione.valore
                    Spacer(modifier = Modifier.height(36.dp))
                    StoricoUiElement(
                        label = label,
                        value = value,
                        pv = pv
                    )
                }
            }
        } else if (tipoPeriodo == "Settimana") {
            weeklyData.forEach { (label, value) ->
                val valueDouble = value.toDoubleOrNull() ?: 0.0
                Spacer(modifier = Modifier.height(36.dp))
                StoricoUiElement(
                    label = label,
                    value = valueDouble,
                    pv = pv
                )
            }
        }
    }
}
@Composable
fun StoricoUiElement(
    label: String,
    value: Double,
    modifier: Modifier = Modifier,
    pv: ParametroVitale
) {

        Column(
            modifier = modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                getIcon(pv.nomeParametro)
                SottoSottoTitolo(
                    label
                )
            }
            if(value == 0.0) {
                SottoSottoTitolo(
                    "Nessuna misurazione",
                )
            } else
            SottoSottoTitolo("${pv.nomeParametro} - ${if (value % 1 == 0.0) value.toInt() else value} ${pv.unitaMisura}"
            )
        }

}