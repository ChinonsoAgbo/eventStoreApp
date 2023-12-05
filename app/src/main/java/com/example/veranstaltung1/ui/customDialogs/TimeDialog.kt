package com.example.veranstaltung1.ui.customDialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowTimeDialog(showTime: MutableState<Boolean>, onTimeSelected:(String) -> Unit){
    val germanTimeFormatter =
        remember { SimpleDateFormat("HH:mm", Locale.GERMANY) }

    val stateGermanTime = rememberTimePickerState(is24Hour = true)


    if (showTime.value) {
        Dialog(onDismissRequest = {
            showTime.value = false
        }) {
            Column(
                modifier = Modifier.background(MaterialTheme.colorScheme.background)
                    .padding(16.dp)
            ) {
                TimePicker(
                    state = stateGermanTime,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TimePickerDefaults.colors(),
                    layoutType = TimePickerDefaults.layoutType()
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                      val  calender =
                            Calendar.getInstance() // new Calender instance
                        calender.set(
                            Calendar.HOUR_OF_DAY, stateGermanTime.hour
                        )
                        calender.set(Calendar.MINUTE, stateGermanTime.minute)
                        calender.isLenient = false
                        /* pass the selected time to the variable to store the new state of the time */
                        onTimeSelected (germanTimeFormatter.format(calender.timeInMillis) )// pass the time in the fun
                                showTime.value = false
                    }, modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Confirm")
                }
            }
        }
    }

}