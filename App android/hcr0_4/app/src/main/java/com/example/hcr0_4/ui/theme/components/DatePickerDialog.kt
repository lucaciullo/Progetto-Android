// DatePickerDialog.kt
package com.example.hcr0_4.ui.theme.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

@Composable
fun ShowDatePickerDialog(
    onDateSelected: (LocalDate) -> Unit,
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current
    val currentDate = LocalDate.now()
    val year = currentDate.year
    val month = currentDate.monthValue - 1
    val day = currentDate.dayOfMonth

    val datePickerDialog = remember {
        DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDayOfMonth ->
            val selectedDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDayOfMonth)
            onDateSelected(selectedDate)
        }, year, month, day)
    }

    LaunchedEffect(Unit) {
        datePickerDialog.show()
    }

    DisposableEffect(Unit) {
        onDispose {
            datePickerDialog.dismiss()
        }
    }
}



