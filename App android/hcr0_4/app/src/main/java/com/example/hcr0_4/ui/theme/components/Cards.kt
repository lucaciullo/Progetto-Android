package com.example.hcr0_4.ui.theme.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.hcr0_4.R
import androidx.compose.ui.unit.sp
import com.example.hcr0_4.CardType
import com.example.hcr0_4.TaskStatus





data class CardItem(
    val id: Int = 0,
    val type: CardType,
    val title: String,
    val description: String,
    val status: TaskStatus,
    val time: String? = null,
    val quantity: String? = null,
    val box: @Composable () -> Unit,
)



@Composable
fun DropdownMenuStatus(currentStatus: TaskStatus, onStatusSelected: (TaskStatus) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedStatus by remember { mutableStateOf(currentStatus) }

    Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
        Button(onClick = { expanded = true }) {
            Text(text = selectedStatus.name)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            TaskStatus.values().forEach { status ->
                DropdownMenuItem(
                    text = { Text(text = status.name) },onClick = {
                    selectedStatus = status
                    expanded = false
                    onStatusSelected(status)
                })
            }
        }
    }
}







@Composable
fun CardList(cards: List<CardItem>, onStatusSelected: (CardItem, TaskStatus) -> Unit) {
    LazyColumn {
        items(cards) { card ->
            when (card.type) {
                CardType.MEDICINA -> TaskExpandableCard(cardItem = card) { newStatus ->
                    onStatusSelected(card, newStatus)
                }
                CardType.DIETA -> TaskExpandableCard(cardItem = card) { newStatus ->
                    onStatusSelected(card, newStatus)
                }
                CardType.ESERCIZIO -> TaskExpandableCard(cardItem = card) { newStatus ->
                    onStatusSelected(card, newStatus)
                }
            }
        }
    }
}




@Composable
fun TaskExpandableCard(
    cardItem: CardItem,
    onStatusSelected: (TaskStatus) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val mainIconRes = when (cardItem.type) {
        CardType.MEDICINA -> R.drawable.pill_ic
        CardType.DIETA -> R.drawable.meal_ic
        CardType.ESERCIZIO -> R.drawable.exercise_ic
        else -> R.drawable.home_ic
    }

    val rotationAngle by animateFloatAsState(targetValue = if (expanded) 180f else 0f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { expanded = !expanded }
    ) {
        val scrollState = rememberScrollState()
        Column(
            modifier =Modifier
                .padding(16.dp)
                .scrollable(scrollState, orientation = Orientation.Vertical)
        )

        {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(id = mainIconRes),
                    contentDescription = null,
                    modifier = Modifier.size(64.dp)
                )
                Column(modifier = Modifier.padding(start = 16.dp)
                    .scrollable(scrollState, orientation = Orientation.Vertical)
                ) {
                    Text(
                        text = "${cardItem.time}",
                        fontSize = 36.sp,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${cardItem.title}",
                        fontSize = 24.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Quantity: ${cardItem.quantity}")
                    Spacer(modifier = Modifier.height(8.dp))

                }
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_down),
                    contentDescription = null,
                    modifier = Modifier
                        .align(alignment = Alignment.Top)
                        .rotate(rotationAngle)
                        .clickable { expanded = !expanded },
                )
            }


            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = cardItem.description)
//                DropdownMenuStatus(cardItem.status, onStatusSelected)
//                Button(onClick = {
//                    Log.d("Hcr", "Modifica")
//                    idTerapiaArg.idTerapia = cardItem.id
//                    cardItem.onModificaCLick}) {
//                    Text(text = "Modifica")
//                }

                cardItem.box

            }
        }
    }
}

//@Preview
//@Composable
//fun CardListPreview() {
//    val sampleCards = listOf(
//        CardItem(type = CardType.MEDICINE, title = "ASPIRINA", description = ".", status = TaskStatus.TAKEN, time = "12:00", quantity = "2 cpr"),
//        CardItem(type = CardType.MEAL, title = "POLLO E RISO", description = "",status = TaskStatus.TAKEN, time = "12:00", quantity = "200g pollo e 150g riso"),
//        CardItem(type = CardType.EXERCISE, title = "CORSA",  description = "",status = TaskStatus.TAKEN, time = "12:00", quantity = "30 min"),
//        CardItem(type = CardType.MEDICINE, title = "TACHIPIRINA", description = "", status = TaskStatus.TAKEN, time = "12:00", quantity = "1 bustina")
//    )
//
//    CardList(cards = sampleCards, onStatusSelected = { _, _ -> })
//}
//
