package com.example.hcr0_4.ui.schermate.home

import android.widget.Space
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextAlign
import com.example.hcr0_4.CardType
import com.example.hcr0_4.HcrTopAppBar
import com.example.hcr0_4.LocalDateAdapter
import com.example.hcr0_4.R
import com.example.hcr0_4.SomministrazioneStatus
import com.example.hcr0_4.data.Somministrazione
import com.example.hcr0_4.data.SomministrazioneDettagliata
import com.example.hcr0_4.ui.AppViewModelProvider
import com.example.hcr0_4.ui.schermate.menuParametriVitali.ParametriVitaliDestination
import com.example.hcr0_4.ui.theme.components.*
import com.example.hcr0_4.ui.navigazione.NavigationDestination
import com.google.gson.GsonBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import com.example.hcr0_4.ui.theme.*
import java.time.DayOfWeek

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigateToInserisciTerapia: (String) -> Unit,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()

//    LaunchedEffect(Unit) {
//        viewModel.somministrazioneGenerator.aggiungiSomministrazioni()
//    }

    Scaffold(
        topBar = {
            Column {
                HcrTopAppBar(
                    title = stringResource(ParametriVitaliDestination.titleRes),
                    canNavigateBack = false
                )
            }
        },
        floatingActionButton = {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ExpandableButton(
            button1 = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Medicina")
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButtonWithIcon(tipo = CardType.MEDICINA.toString()) {
                        navigateToInserisciTerapiaTipo(
                            navigateToInserisciTerapia = navigateToInserisciTerapia,
                            tipo = CardType.MEDICINA
                        )
                    }

                }
            },
            button2 = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Dieta")
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButtonWithIcon(tipo = CardType.DIETA.toString()) {
                        navigateToInserisciTerapiaTipo(
                            navigateToInserisciTerapia = navigateToInserisciTerapia,
                            tipo = CardType.DIETA
                        )
                    }
                }
            },
            button3 = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Esercizio")
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButtonWithIcon(tipo = CardType.ESERCIZIO.toString()) {
                        navigateToInserisciTerapiaTipo(
                            navigateToInserisciTerapia = navigateToInserisciTerapia,
                            tipo = CardType.ESERCIZIO
                        )
                    }
                }
            },
            modifier = Modifier.padding(
                end = WindowInsets.safeDrawing.asPaddingValues()
                    .calculateEndPadding(LocalLayoutDirection.current)
            )
        )
    }
}
    ) { innerPadding ->


        var accumulatedDrag by remember { mutableStateOf(0f) }
        var isGestureEnabled by remember { mutableStateOf(true) }
        val coroutineScope = rememberCoroutineScope()

        Column(
            modifier = Modifier
                .fillMaxSize()

                .padding(innerPadding)
                .pointerInput(Unit) {
                    val dragThreshold = 100f // Define a threshold for the drag amount

                    detectHorizontalDragGestures { change, dragAmount ->
                        if (isGestureEnabled) {
                            accumulatedDrag += dragAmount
                            if (accumulatedDrag < -dragThreshold) {
                                viewModel.onNavigateDays(1)
                                accumulatedDrag = 0f
                                isGestureEnabled = false
                                coroutineScope.launch {
                                    delay(100) // Delay in milliseconds
                                    isGestureEnabled = true
                                }
                            } else if (accumulatedDrag > dragThreshold) {
                                viewModel.onNavigateDays(-1)
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
            DateSelectionRow(
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
                HomeBody(
                    somministrazioneDList = homeUiState.somministrazioneDList,
                    onModificaClick = viewModel::updateSomministrazione,
                    onPresaClick = viewModel::onPresaClick,
                    modifier = modifier.fillMaxSize(),
                    selectedDate = selectedDate,
                    contentPadding = PaddingValues(0.dp) // Ensure contentPadding is set correctly
                )
            }
        }
    }
}

private fun navigateToInserisciTerapiaTipo(
    navigateToInserisciTerapia: (String) -> Unit,
    tipo: CardType
){
    val terapiaDetails: TerapiaDetails = TerapiaDetails()

    val updateTerapiaDetails = terapiaDetails.copy(tipo = tipo.toString())

// Create a custom instance of Gson
    val gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter)
        .create()

// Use the custom Gson instance for serialization
    val terapiaDetailsJson: String = gson.toJson(updateTerapiaDetails)
// Use the custom Gson instance for deserialization
    navigateToInserisciTerapia(terapiaDetailsJson)

}

@Composable
private fun HomeBody(
    somministrazioneDList: List<SomministrazioneDettagliata>,
    onModificaClick: (Somministrazione) -> Unit,
    onPresaClick: (SomministrazioneDettagliata) -> Unit,
    modifier: Modifier = Modifier,
    selectedDate: LocalDate = LocalDate.now(),

    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier

    ) {
        if (somministrazioneDList.isEmpty()) {
            Text(
                text = "Non esistono somministrazioni",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(contentPadding),
            )
        } else {
            HcrList(
                somministrazioneDList = somministrazioneDList,
                onModificaClick = onModificaClick,
                onPresaClick = onPresaClick,
                selectedDate = selectedDate,
                contentPadding = contentPadding
            )
        }
    }
}

@Composable
fun MeasureCardHeight(somministrazioneDList: List<SomministrazioneDettagliata>, onHeightMeasured: (Int) -> Unit) {
    var isMeasured by remember { mutableStateOf(false) }

    if (!isMeasured) {
        Box(
            modifier = Modifier
                .onGloballyPositioned { coordinates ->
                    onHeightMeasured(coordinates.size.height)
                    isMeasured = true
                }
        ) {
            somministrazioneDList.forEach{ somministrazioneD ->
                HcrUiElements(
                    somministrazioneD = somministrazioneD,
                    onPresaClick = {}
                )
            }

        }
    }
}

@Composable
private fun HcrList(
    somministrazioneDList: List<SomministrazioneDettagliata>,
    onModificaClick: (Somministrazione) -> Unit,
    onPresaClick: (SomministrazioneDettagliata) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    selectedDate: LocalDate = LocalDate.now()
) {
    val (somministrazioniInCorso, somministrazioniNonInCorso) = remember(somministrazioneDList) {
        val inCorso = somministrazioneDList.filter { it.statoSomministrazione == SomministrazioneStatus.PROGRAMMATA.toString() }
        val nonInCorso = somministrazioneDList.filter { it.statoSomministrazione != SomministrazioneStatus.PROGRAMMATA.toString() }
        inCorso to nonInCorso
    }

    val scrollState = rememberScrollState()

    var cardHeight by remember { mutableStateOf(0) }
    if (somministrazioniNonInCorso.isNotEmpty()) {
        MeasureCardHeight(
            somministrazioneDList = somministrazioniNonInCorso,
            onHeightMeasured = { height ->
                cardHeight = height
            }
        )
    }

    val lastNonInCorsoIndex = somministrazioneDList.indexOfLast { it.statoSomministrazione != SomministrazioneStatus.PROGRAMMATA.toString() }
    val scrollToPosition = cardHeight * (lastNonInCorsoIndex + 1)

    LaunchedEffect(selectedDate, cardHeight) {
        if (selectedDate == LocalDate.now() && lastNonInCorsoIndex != -1) {
            scrollState.animateScrollTo(scrollToPosition)
        }
    }

    LaunchedEffect(selectedDate) {
        if (selectedDate == LocalDate.now()) {
            scrollState.scrollTo(0)
        }
    }

    Column(
        modifier = modifier
            .padding(contentPadding)
            .verticalScroll(scrollState)
            .fillMaxSize()
    ) {
        somministrazioniNonInCorso.forEach { somministrazioneD ->
            HcrUiElements(
                somministrazioneD = somministrazioneD,
                onPresaClick = onPresaClick
            )
        }
        if (selectedDate == LocalDate.now()) {
            DashedDividerWithTime()
        }
        somministrazioniInCorso.forEach { somministrazioneD ->
            HcrUiElements(
                somministrazioneD = somministrazioneD,
                onPresaClick = onPresaClick,
                onModificaClick = onModificaClick
            )
        }
        Spacer(modifier = Modifier.height(72.dp))
    }
}
@Composable
fun HcrUiElements(
    somministrazioneD: SomministrazioneDettagliata,
    onPresaClick: (SomministrazioneDettagliata) -> Unit,
    onModificaClick: (Somministrazione) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    var updatedOra by remember { mutableStateOf(somministrazioneD.oraSomministrazione) }
    var updatedData by remember { mutableStateOf(somministrazioneD.dataSomministrazione) }

    val coloreStatoSomministrazione = remember(somministrazioneD.statoSomministrazione) {
        when (somministrazioneD.statoSomministrazione) {
            SomministrazioneStatus.COMPLETATA.toString() -> HcrGreen
            SomministrazioneStatus.NON_COMPLETATA.toString() -> HcrRed
            SomministrazioneStatus.PROGRAMMATA.toString() -> Color.Black
            SomministrazioneStatus.IN_CORSO.toString() -> HcrYellow
            else -> Color.White
        }
    }
    val dataOraMin = somministrazioneD.dataSomministrazione.atTime(somministrazioneD.oraSomministrazione).minusHours(4)
    val dataOraMax = somministrazioneD.dataSomministrazione.atTime(somministrazioneD.oraSomministrazione).plusHours(4)
    val now = LocalDateTime.now()
    val isWithinRange = now.isAfter(dataOraMin) && now.isBefore(dataOraMax)


    ExpandableCardFunction(
        icon = getIcon(tipo = somministrazioneD.tipo),
        titolo = {
            TitoloOppureInserisciOra(
            somministrazioneD = somministrazioneD,
            updatedOra =  updatedOra,
            isExpanded = isExpanded,
            onTimeChange = { updatedOra = it
            }
        )
        } ,
        azioneQuandoSiEspandeLaCard = {
            isExpanded = !isExpanded
            updatedData = somministrazioneD.dataSomministrazione
            updatedOra = somministrazioneD.oraSomministrazione
        },
        sottotitolo = { SottoTitolo(text = somministrazioneD.nomeTerapia) },
        sottosottotitolo = {
            Column {
                SottoSottoTitolo(
                    text = if (somministrazioneD.dosePresa == 0.0) {
    " ${if (somministrazioneD.dose % 1 == 0.0) somministrazioneD.dose.toInt() else somministrazioneD.dose} ${somministrazioneD.forma}"
} else {
    " ${if (somministrazioneD.dosePresa % 1 == 0.0) somministrazioneD.dosePresa.toInt() else somministrazioneD.dosePresa} ${somministrazioneD.formaPresa}"
}
                )
                if(isWithinRange){
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val showDialog = remember { mutableStateOf(false) }
                        // Mostra il dialogo di conferma se showDialog è true
                        if (showDialog.value) {
                            ConfirmationDialog(
                                onConfirm = {
                                    onPresaClick(somministrazioneD)
                                    showDialog.value = false
                                },
                                onCancel = {
                                    showDialog.value = false
                                }
                            )
                        }
                        Text(
                            text = somministrazioneD.statoSomministrazione,
                            modifier = Modifier.weight(1f)
                        )
                        Switch(
                            checked = if(somministrazioneD.statoSomministrazione == SomministrazioneStatus.COMPLETATA.toString()) true else false,
                            onCheckedChange = { showDialog.value = true }, // Quando lo switch viene premuto, mostra il dialogo
                            modifier = Modifier.padding(start = 8.dp),
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.Black,
                                checkedTrackColor = Color.White
                            )
                        )
                    }
                }
            }
        },
        boxContent = {
            if( somministrazioneD.statoSomministrazione == SomministrazioneStatus.PROGRAMMATA.toString()){

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){

                    Text(text= "Data" )
                    InserisciData(date = updatedData, onDateChange = {updatedData = it })

                }

            }
            if(somministrazioneD.statoSomministrazione == SomministrazioneStatus.COMPLETATA.toString()){
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(text= "Ora")
                    Text(text= somministrazioneD.oraSomministrazione.format(DateTimeFormatter.ofPattern("HH:mm")))
                }
            }




            when(somministrazioneD.tipiDurata){
                "Data fine" -> Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Fino a")
                    Text(text = "${somministrazioneD.dataFine}")
                }
                "N° giorni" -> Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Mancano")
                    Text(text = "${somministrazioneD.durata?.minus(somministrazioneD.aderenza)} giorni")
                }
                "N° di volte" -> Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Mancano")
                    Text(text = "${somministrazioneD.durata?.minus(somministrazioneD.aderenza)} volte")
                }

            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(text= "Da prendere")
                when(somministrazioneD.frequenza){
                    "Giorn."-> Text(text= "Tutti i giorni")
                    "Sett."-> Text(text= " ${somministrazioneD.giorniSettimana}")
                    "Pers."-> {
                        val giorni = if(somministrazioneD.ogniTotGiorni!! > 0) "${somministrazioneD.ogniTotGiorni} giorni" else ""
                        val ore = if(somministrazioneD.ogniTotOre!! > 0) "${somministrazioneD.ogniTotOre} ore" else ""
                        val minuti = if(somministrazioneD.ogniTotMin!! > 0) "${somministrazioneD.ogniTotMin} minuti" else ""
                        Text(text= "Ogni $giorni $ore $minuti")

                    }
                }
            }


            if(!isWithinRange){
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(text= "Stato")
                    Text(text=somministrazioneD.statoSomministrazione)
                }
            }





        },

        colore = coloreStatoSomministrazione,
        modifier = modifier,
        pulsanteFinaledestro = {if(somministrazioneD.statoSomministrazione == SomministrazioneStatus.PROGRAMMATA.toString()){
            PulsanteConfermaModifiche(
                onPresaClick = onModificaClick,
                somministrazioneD = somministrazioneD,
                updatedOra = updatedOra,
                updatedData = updatedData
            )
        }}
    )
}

@Composable
private fun PulsanteConfermaModifiche(
    onPresaClick: (Somministrazione) -> Unit,
    somministrazioneD: SomministrazioneDettagliata,
    updatedOra: LocalTime,
    updatedData: LocalDate
) {
    var showConfirmDialog by remember { mutableStateOf(false) }

    if (showConfirmDialog) {
        ConfirmationDialog(
            onConfirm = {
                val somministrazione = Somministrazione(
                    idSomministrazione = somministrazioneD.idSomministrazione,
                    oraSomministrazione = updatedOra,
                    dataSomministrazione = updatedData,
                    statoSomministrazione = somministrazioneD.statoSomministrazione,
                    dataPresa = somministrazioneD.dataPresa,
                    oraPresa = somministrazioneD.oraPresa,
                    idPosologia = somministrazioneD.idPosologia,
                    dosePresa = somministrazioneD.dosePresa,
                    formaPresa = somministrazioneD.formaPresa
                )
                onPresaClick(somministrazione)


                showConfirmDialog = false
            },
            onCancel = {
                showConfirmDialog = false
            }
        )
    }

    Pulsante(
        onClick = { showConfirmDialog = true },
        testoPulsante = "Conferma"
    )
}


@Composable
fun TitoloOppureInserisciOra(
    somministrazioneD: SomministrazioneDettagliata,
    updatedOra: LocalTime,
    isExpanded: Boolean,
    onTimeChange: (LocalTime) -> Unit = {}
){
    val ora = somministrazioneD.oraSomministrazione.format(DateTimeFormatter.ofPattern("HH:mm"))

    if (!isExpanded && somministrazioneD.statoSomministrazione == SomministrazioneStatus.PROGRAMMATA.toString()) {
        InserisciOra(time = updatedOra, onTimeChange = onTimeChange, label = "")
    } else {
        when (somministrazioneD.statoSomministrazione) {
            SomministrazioneStatus.COMPLETATA.toString() -> {
                Column {
                    Text("Completata alle:")
                    Titolo(" ${somministrazioneD.oraPresa?.format(DateTimeFormatter.ofPattern("HH:mm"))}")
                }
            }
            SomministrazioneStatus.PROGRAMMATA.toString() -> {
                Column {
                    Titolo(ora)
                }
            }
            SomministrazioneStatus.NON_COMPLETATA.toString() -> {
                Column {
                    Text("Non completata")
                    Titolo(ora)
                }
            }
            else -> {
                Column {
                    Titolo(text = "Stato non definito")
                }
            }
        }
    }
}

@Composable
fun DateSelectionRow(
    tipo: String = "Giorno",
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onNavigateMonths: (Int) -> Unit
) {
    val dates = generateDatesForMonth(selectedDate)
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = calculateOffset(selectedDate, dates))
    val monthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())

    LaunchedEffect(selectedDate) {
        val index = calculateOffset(selectedDate, dates)
        val centerOffset = (listState.layoutInfo.visibleItemsInfo.size / 2).coerceAtLeast(0)
        val targetIndex = (index - centerOffset).coerceIn(0, dates.size - 1)
        listState.animateScrollToItem(targetIndex)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp), // Reduced vertical padding
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically // Align items vertically in the center
        ) {
            IconButton(onClick = { onNavigateMonths(-1) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous")
            }
            InserisciData(
                date = selectedDate,
                onDateChange = onDateSelected,
                formatter = monthFormatter
            )
            IconButton(onClick = { onNavigateMonths(1) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next")
            }
        }

        when (tipo) {
            "Giorno" -> {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    state = listState,
                    contentPadding = PaddingValues(horizontal = 4.dp) // Reduced horizontal padding
                ) {
                    items(dates, key = { it.toString() }) { date ->
                        DateChip(date = date, isSelected = date == selectedDate, onDateSelected = onDateSelected)
                    }
                }
            }
            "Settimana" -> {
                Spacer(modifier = Modifier.width(32.dp))

                val startOfWeek = selectedDate.with(DayOfWeek.MONDAY)
                val endOfWeek = selectedDate.with(DayOfWeek.SUNDAY)
                val weekDates = (0..6).map { startOfWeek.plusDays(it.toLong()) }

               Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
//                    items(weekDates, key = { it.toString() }) { date ->
//                        DateChip(date = date, isSelected = date == selectedDate, onDateSelected = onDateSelected)
//                    }
                    Text(text = "${startOfWeek.dayOfMonth} - ${endOfWeek.dayOfMonth}", color = HcrGreen)
                }
            }
            "Mese" -> {
                // Do nothing, only show the month
            }
        }
    }
}

@Composable
fun DateChip(date: LocalDate, isSelected: Boolean, onDateSelected: (LocalDate) -> Unit) {
    TextButton(
        onClick = { onDateSelected(date) },
        colors = ButtonDefaults.textButtonColors(
            contentColor = if (isSelected) HcrGreen else HcrGrigioScala4
        ),
        modifier = Modifier.padding(horizontal = 2.dp) // Reduced horizontal padding
    ) {
        Text(text = date.dayOfMonth.toString(), style = MaterialTheme.typography.titleMedium)
    }
}


fun generateDatesForMonth(date: LocalDate): List<LocalDate> {
    val daysInMonth = date.month.length(date.isLeapYear)
    return List(daysInMonth) { index -> date.withDayOfMonth(index + 1) }
}

fun calculateOffset(selectedDate: LocalDate, dates: List<LocalDate>): Int {
    return dates.indexOfFirst { it == selectedDate }.takeIf { it != -1 } ?: 0
}


@Composable
fun ExpandableButton(
    button1: @Composable () -> Unit,
    button2: @Composable () -> Unit,
    button3: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        if (isExpanded) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.9f))
                    .clickable { isExpanded = false }
            )
        }

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .align(Alignment.BottomEnd)
        ) {
            if (isExpanded) {
                button1()
                Spacer(modifier = Modifier.height(8.dp))
                button2()
                Spacer(modifier = Modifier.height(8.dp))
                button3()
                Spacer(modifier = Modifier.height(8.dp))
            }
            Pulsante(onClick = { isExpanded = !isExpanded }) {
                Icon(
                    imageVector = if (isExpanded) Icons.Default.Clear else Icons.Default.Add,
                    contentDescription = if (isExpanded) "Close" else "Expand",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun DashedDividerWithTime() {
    val currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(modifier = Modifier.weight(1f)) {
            val dashWidth = 10.dp.toPx()
            val dashGap = 5.dp.toPx()
            val totalWidth = size.width
            var currentX = 0f

            while (currentX < totalWidth) {
                drawLine(
                    color = Color.Gray,
                    start = Offset(currentX, size.height / 2),
                    end = Offset(currentX + dashWidth, size.height / 2),
                    strokeWidth = 2.dp.toPx(),
                    cap = StrokeCap.Round
                )
                currentX += dashWidth + dashGap
            }
        }
        Text(
            text = currentTime,
            modifier = Modifier.padding(start = 8.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}