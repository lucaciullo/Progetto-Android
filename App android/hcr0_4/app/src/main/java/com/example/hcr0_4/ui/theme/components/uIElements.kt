@file:OptIn(ExperimentalLayoutApi::class)

package com.example.hcr0_4.ui.theme.components


import androidx.compose.ui.text.input.ImeAction
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.hcr0_4.R
import com.example.hcr0_4.ui.theme.*
//importa i miei colori
import java.time.LocalDate
import java.time.LocalTime
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import java.time.format.DateTimeFormatter

import com.himanshoe.charty.bar.model.BarData
// Import the necessary classes
import com.himanshoe.charty.common.ChartDataCollection
import com.himanshoe.charty.common.config.AxisConfig
import com.himanshoe.charty.bar.BarChart
@Composable
fun InserisciData(
    label: String ="",
    date: LocalDate,
    onDateChange: (LocalDate) -> Unit,
    onValueChange: (LocalDate) -> Unit = {},
    formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yy") // Default to month format
) {
    Row {
        Text(label, style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier)
        DateInput(
            date = date,
            onDateChange = { newDate ->
                onDateChange(newDate)
                onValueChange(newDate)
            },
            formatter = formatter
        )
    }
}

@Composable
fun DateInput(date: LocalDate, onDateChange: (LocalDate) -> Unit, formatter: DateTimeFormatter) {
    val context = LocalContext.current
    Pulsante(
        onClick = {
            DatePickerDialog(context, { _, year, month, dayOfMonth ->
                onDateChange(LocalDate.of(year, month + 1, dayOfMonth))
            }, date.year, date.monthValue - 1, date.dayOfMonth).show()
        },
        testoPulsante = date.format(formatter)
    )
}

@Composable
fun MyTextField(testo: String, modifier: Modifier =Modifier) {
    var text by remember { mutableStateOf("") }

    TextField(
        value = testo,
        onValueChange = { text = it },
        label = { Text("Inserisci i dati qui") }
    )
}

@Composable
fun InserisciOra(
    label: String,
    time: LocalTime,
    onTimeChange: (LocalTime) -> Unit,
    onValueChange: (LocalTime) -> Unit = {},
) {

    Row {
        Text(label)
        Spacer(modifier = Modifier)
        TimeInput(
            time = time,
            onTimeChange = { newTime ->
                onTimeChange(newTime)
                onValueChange(newTime)
            }
        )
    }
}

@Composable
fun TimeInput(time: LocalTime, onTimeChange: (LocalTime) -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val formattedTime = String.format("%02d:%02d", time.hour, time.minute)

    Pulsante(onClick = {
        val current = time
        TimePickerDialog(context, { _, hourOfDay, minute ->
            onTimeChange(LocalTime.of(hourOfDay, minute))
        }, current.hour, current.minute, true).show()

    },
        colors = ButtonDefaults.buttonColors(containerColor = HcrGreen),
        testoPulsante = formattedTime
    )
}

@Composable
fun IconButtonWithIcon(
    tint: Color = Color.White,
    tipo: String,
    onClick: () -> Unit
) {
    Pulsante(onClick = onClick) {
        Icon(
            painter = getIcon(tipo),
            contentDescription = null,
            tint = tint // Apply the tint color to the icon
        )
    }
}




// Classe per creare una card espandibile


// Funzione per creare una card espandibile
@Composable
fun ExpandableCardFunction(
    icon: Painter? = painterResource(id = R.drawable.logo_trasparente), // Icona da visualizzare nella card
    titolo: @Composable () -> Unit, // Titolo della card
    sottotitolo: @Composable (() -> Unit)? = null, // Sottotitolo della card (opzionale)
    sottosottotitolo: @Composable (() -> Unit)? = null, // Sottosottotitolo della card (opzionale)
    espansa: Boolean = false,
    pulsanteFinaledestro: @Composable (() -> Unit)? = null,
    pulsanteFinaleSinistro: @Composable (() -> Unit)? = null,
    azioneQuandoSiEspandeLaCard: () -> Unit = {}, // Azione da eseguire quando la card viene espansa
    modifier: Modifier = Modifier, // Modificatori per personalizzare la card

    scrollare: Boolean = false, // Flag per abilitare/disabilitare lo scrolling della card
    colore: Color = Black, // Colore di sfondo della card
    boxContent: @Composable () -> Unit // Contenuto della card

) {


    // Utilizza remember per mantenere lo stato di espansione della card
    var isExpanded by remember { mutableStateOf(espansa) }
    val rotationAngle by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f)

    // Funzione per gestire il click sulla card e cambiare lo stato di espansione
    val onToggle = { isExpanded = !isExpanded }
    LaunchedEffect(isExpanded){
        azioneQuandoSiEspandeLaCard()
    }
    // Definisce la card
    Card(
        modifier = Modifier
            .padding(10.dp)
            .clickable(onClick = onToggle) // La card è cliccabile per espandere/contrarre
            .border(
                BorderStroke(1.dp, Color.White),
                RoundedCornerShape(16.dp)
            )// Aggiunge un bordo bianco
            .fillMaxWidth() // La card occupa tutta la larghezza disponibile
            .heightIn(max = 2000.dp), // L'altezza massima della card è lo schermo intero

        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp), // Imposta l'ombreggiatura della card
        colors = CardDefaults.cardColors(
            containerColor = colore
        ) // Imposta il colore di sfondo della card
    ) {
        // Contenuto della card
        Column(Modifier.padding(15.dp)) {
            // Righe contenenti l'icona, il titolo e, opzionalmente, il sottotitolo
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 5.dp)
            ) {
                if (icon != null){
                    Icon(
                        painter = icon, contentDescription = null, Modifier.size(40.dp), tint = Color.White
                    ) // Icona
                    Spacer(modifier = Modifier.width(16.dp)) // Spazio tra l'icona e il titolo

                }
                Box(Modifier.weight(1f)) {
                    titolo()
                } // Titolo
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_down),
                    contentDescription = null,
                    modifier = Modifier
                        .rotate(rotationAngle)
                        .clickable { onToggle() },
                )
            }

            // Opzionalmente, visualizza il sottotitolo
            if (sottotitolo != null) {
                Box(modifier = Modifier.padding(top = 7.dp)) {
                    sottotitolo()
                }
            }

            // Opzionalmente, visualizza il sottosottotitolo
            if (sottosottotitolo != null) {
                Box(modifier = Modifier.padding(top = 5.dp)) {
                    sottosottotitolo()
                }
            }

            // AnimatedVisibility per animare l'apertura e la chiusura della card
            AnimatedVisibility(
                visible = isExpanded, // Cambiato per invertire il valore visibile
                enter = expandVertically(animationSpec = tween(150)), // Animazione fadeIn quando la card diventa visibile
                exit = shrinkVertically(animationSpec = tween(150)) // Animazione fadeOut quando la card diventa invisibile
            ) {
                // Contenuto della card all'interno di un Box per abilitare lo scrolling se necessario

                    Column {
                        boxContent() // Contenuto della card senza scrolling
                        Spacer(modifier = Modifier.height(10.dp))
                        if (pulsanteFinaledestro != null || pulsanteFinaleSinistro != null) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = if (pulsanteFinaledestro != null && pulsanteFinaleSinistro != null) {
                                    Arrangement.SpaceBetween
                                } else {
                                    Arrangement.End
                                }
                            ) {
                                if (pulsanteFinaleSinistro != null && pulsanteFinaledestro != null) {
                                    pulsanteFinaleSinistro()
                                    pulsanteFinaledestro()
                                } else if (pulsanteFinaledestro != null) {
                                    pulsanteFinaledestro()
                                } else if (pulsanteFinaleSinistro != null) {
                                    pulsanteFinaleSinistro()
                                }
                            }
                        }
                    }

            }

        }

    }


}

@Composable
fun Titolo(
    text: String
){
    Text(text, style = MaterialTheme.typography.displayMedium)

}
@Composable
fun SottoTitolo(
    text: String
){
    Text(text, style = MaterialTheme.typography.headlineMedium)

}
@Composable
fun SottoSottoTitolo(
    text: String
){
    Text(text, style = MaterialTheme.typography.headlineSmall, color = Color.White  )

}

@Composable
fun getIcon(tipo: String): Painter {
    return when(tipo){
        "MEDICINA" -> painterResource(id = R.drawable.pill_ic)
        "ESERCIZIO" -> painterResource(id = R.drawable.exercise_ic)
        "Piani di allenamento" -> painterResource(id = R.drawable.running)

        "DIETA" -> painterResource(id = R.drawable.meal_ic)
        "Diete" -> painterResource(id = R.drawable.weighing_scale)

        "Referti" -> painterResource(id = R.drawable.health_report)
        "Passi" -> painterResource(id = R.drawable.footsteps)
        "Attiva" -> painterResource(id = R.drawable.pause)
        "Disattiva" -> painterResource(id = R.drawable.play_button)
        "Notifica" -> painterResource(id = R.drawable.bellgreen)
        "Sveglia" -> painterResource(id = R.drawable.clock_alarm)
        "Off" -> painterResource(id = R.drawable.no_notification)
        "Battito cardiaco" -> painterResource(id = R.drawable.cardiogram)
        "Ossigenazione nel sangue" -> painterResource(id = R.drawable.lungs)
        "Temperatura" -> painterResource(id = R.drawable.temperature)
        else -> painterResource(id = R.drawable.tinywow_alvin_logo_sfumato_58026275)
    }
}




@Composable
fun DropdownMenuItems(
    selectedValue: String,
    items: List<String>,
    onItemSelected: (String) -> Unit,
    testo: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onValueChange: (String) -> Unit = { }
) {
    var expanded by remember { mutableStateOf(false) }
    Row {
        Text(testo, style = MaterialTheme.typography.titleMedium)
        Box(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .background(Color.Black)
                .clip(RoundedCornerShape(8.dp)) // Aggiunge bordi arrotondati
                .border(border = BorderStroke(4.dp, Color.Gray)) // Aggiunge un bordo
        ) {
            Text(
                text = selectedValue,
                color = Color.White,
                modifier = Modifier
                    .clickable { expanded = true }
                    .padding(8.dp)
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item) },
                        onClick = {
                            onItemSelected(item)
                            onValueChange(item)
                            expanded = false
                        },
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .background(Color.Black)
                            .clip(RoundedCornerShape(4.dp)) // Aggiunge bordi arrotondati
                            .border(border = BorderStroke(1.dp, Color.Black)) // Aggiunge un bordo
                    )
                }
            }
        }
    }
}


@Composable
 fun ConfirmationDialog(
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = { /* Do nothing */ },
        title = { Text(stringResource(R.string.attention)) },
        text = { Text(text = "Sei sicuro?") },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text(text = stringResource(R.string.no))
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = "Si")
            }
        }
    )
}


@Composable
fun TextFieldAndDropdownMenu(
    textFieldValue: String = "",
    onTextFieldValueChange: (String) -> Unit,
    dropdownSelectedValue: String,
    dropdownItems: List<String>,
    onDropdownItemSelected: (String) -> Unit,
    textFieldLabel: String = "Quantita'",
    dropdownLabel: String = "",
    textFieldWeight: Float = 0.4f,
    dropDownWeight: Float = 1f

) {

    val textFieldValue = remember { mutableStateOf(textFieldValue) }
    Row{
        TextField(
            value = textFieldValue.value,
            onValueChange = {
                textFieldValue.value = it
                onTextFieldValueChange
            },
            label = { Text(textFieldLabel) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .weight(textFieldWeight)
                .padding(horizontal = 2.dp)
        )

        DropdownMenuItems(
            selectedValue = dropdownSelectedValue,
            items = dropdownItems,
            onItemSelected = onDropdownItemSelected,
            testo = dropdownLabel,
            modifier = Modifier
                .weight(dropDownWeight)
                .padding(horizontal = 2.dp)
        )
    }
}


@Composable
fun Pulsante(
    colors: ButtonColors = ButtonDefaults.buttonColors(containerColor = HcrGreen),
    onClick: () -> Unit = {},
    testoPulsante: String = "",
    content: @Composable () -> Unit = {
        Text(
            text = testoPulsante,
            style = MaterialTheme.typography.titleMedium,
            color = Color.White // Ensure the text color is always white
        )
    }
) {
    // Modificatore per i pulsanti
    val buttonModifier = Modifier
        .padding(4.dp)
        .border(
            1.dp,
            Color.White,
            RoundedCornerShape(22.dp)
        ) // Bordo bianco intorno ai pulsanti con angoli stondati
        .height(48.dp) // Altezza fissa per i pulsanti
        .widthIn(min = 0.dp) // Larghezza minima per i pulsanti

    Button(onClick = onClick, colors = colors, modifier = buttonModifier) {
        content()
    }
}


@Composable
fun MultiSelectorButton(
    turnOff: Boolean = true,
    multiSelector: Boolean = true,
    listaSelezionati: List<String> = listOf(),
    items: List<String>,
    onSelectionChanged: (List<String>) -> Unit
) {
    var selectedItems by remember { mutableStateOf(listaSelezionati.filter { it.isNotBlank() }) }

    val buttonColors = remember { mutableStateMapOf<String, Color>().apply { items.forEach { put(it, Color.Black) } } }



    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        mainAxisAlignment = FlowMainAxisAlignment.Center // Allineamento centrale
    ) {
        items.forEach { item ->
            val isSelected = selectedItems.contains(item)
            val buttonColor = buttonColors[item] ?: Color.Black

            Pulsante(
                onClick = {
                    selectedItems =  if (isSelected) {
                        Log.d("hcr", "isSelected: $isSelected")
                        if (turnOff) {

                            onSelectionChanged(listOf(item))
                            selectedItems - item
                        } else {
                            selectedItems // Keep the item selected even if it's already selected
                        }
                    } else {
                        if (multiSelector) {
                            selectedItems + item
                        } else {
                            listOf(item)
                        }
                    }
                    onSelectionChanged(selectedItems)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) HcrGreen else buttonColor
                ),
                testoPulsante = item
            )
        }
    }

    // Effetto per aggiornare dinamicamente la lista degli elementi selezionati
    LaunchedEffect(listaSelezionati) {
        if (listaSelezionati != selectedItems) {
            selectedItems = listaSelezionati.filter { it.isNotBlank() }
            onSelectionChanged(selectedItems)
        }

    }
}

@Composable
fun ButtonSelector(
    turnOff: Boolean = true,
    items: List<String>,
    elementoSelezionato: String = "",
    defaultValue: String = "",
    onSelectionChanged: (String) -> Unit
) {
    val selectedValue = remember { mutableStateOf(elementoSelezionato.ifBlank { defaultValue }) }

    MultiSelectorButton(
        turnOff = turnOff,
        multiSelector = false,
        items = items,
        listaSelezionati = listOf(selectedValue.value)
    ) { selectedItems ->
        val newValue = selectedItems.firstOrNull() ?: defaultValue
        if (selectedValue.value != newValue || selectedValue.value == "+") {
            selectedValue.value = newValue
            onSelectionChanged(newValue)
        }
    }

    // Effetto per aggiornare dinamicamente il valore selezionato
    LaunchedEffect(elementoSelezionato) {
        if (elementoSelezionato.isNotBlank() && elementoSelezionato != selectedValue.value) {
            selectedValue.value = elementoSelezionato
            onSelectionChanged(elementoSelezionato)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampoDiTesto(
    text: String,
    onTextChange: (String) -> Unit,
    placeholder: String ="",
    tastierinoNumerico: Boolean = false,
    modifier: Modifier = Modifier
) {
    val keyboardOptions = if (tastierinoNumerico) {
        KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done)
    } else {
        KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done)
    }

    TextField(
        value = text,
        onValueChange = onTextChange,
        placeholder = { Text(placeholder) },
        keyboardOptions = keyboardOptions,
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White) // Increased corner radius
            .border(BorderStroke(1.dp, Color.Gray), RoundedCornerShape(16.dp)), // Increased corner radius
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Black,
            focusedIndicatorColor = HcrGreen,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}


fun aggiustaDouble(value: Double):String{
    return if(value == 0.0) "" else if(value%1 ==0.0) value.toInt().toString() else value.toString()
}

@Composable
fun Riga(content: @Composable () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
content()
    }
}

@Composable
fun GraficoParametriVitali(
    lista: List<Pair<String, Float>>, // Using Float for consistency with the bar chart
    max: Double,
    tipoPeriodo: String
) {
    // Determine X-axis labels based on the data type
    val xAxisLabels = when (tipoPeriodo) {
        "Settimana" -> listOf("lu", "ma", "me", "gi", "ve", "sa", "do")
        "Giorno" -> (0..23).map { it.toString() } // 0 to 23 for hours of the day
        else -> listOf() // Empty list for other data types
    }

    // Prepare data for the chart
    val chartDataCollection = ChartDataCollection(
        data = lista.map { BarData(it.first.toFloatOrNull() ?: 0f, it.second) } // Convert String to Float
    )

    // Display the bar chart
    BarChart(
        dataCollection = chartDataCollection,
        barColor = Color.Blue,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(200.dp),
        axisConfig = AxisConfig(
            axisColor = Color.Gray,
            showAxes = true,
            axisStroke = 1f,
            minLabelCount = 5,
            showGridLabel = true,
            showGridLines = true
        )
    )

    // Display the data as text for debugging purposes
    Text(text = lista.toString())
}
