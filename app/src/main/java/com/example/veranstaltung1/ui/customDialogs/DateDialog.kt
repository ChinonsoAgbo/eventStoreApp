package com.example.veranstaltung1.ui.customDialogs

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowDateDialog(showDate: MutableState<Boolean>, onDateSelected:(String) ->Unit) {
    val germanCalender = Calendar.getInstance(TimeZone.getTimeZone("Europe/Berlin"))


    val stateGermanDate = rememberDatePickerState(
        initialSelectedDateMillis = germanCalender.timeInMillis

    )

    var selectedGermanDate by remember {
        mutableLongStateOf(
            LocalDateTime.now(ZoneId.of("Europe/Berlin"))
                .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        )
    }
    val germanDateFormatter =
        remember { SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY) }

    // Date picker for canada
    if (showDate.value) {
        DatePickerDialog(
            onDismissRequest = {
                showDate.value = false},
            confirmButton = {
                TextButton(onClick = {
                    showDate.value = false
                    selectedGermanDate = stateGermanDate.selectedDateMillis!!
                    onDateSelected (germanDateFormatter.format(selectedGermanDate))
                }) {
                    Text(text = "confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDate.value = false
                }) {
                    Text(text = "Cancel")
                }
            },
        ) {
            DatePicker(state = stateGermanDate)
        }
    }


}
