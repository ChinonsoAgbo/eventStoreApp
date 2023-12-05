package com.example.veranstaltung1.ui.editEvent

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.veranstaltung1.Constants
import com.example.veranstaltung1.EventApplication
import com.example.veranstaltung1.R
import com.example.veranstaltung1.download.AndroidDownloader
import com.example.veranstaltung1.model.EventData
import com.example.veranstaltung1.ui.customDialogs.CustomDialog
import com.example.veranstaltung1.ui.EventsViewModel
import com.example.veranstaltung1.ui.customDialogs.FabItem
import com.example.veranstaltung1.ui.GenericAppBar
import com.example.veranstaltung1.ui.customDialogs.MultiFloatingActionButton
import com.example.veranstaltung1.ui.customDialogs.ShowDateDialog
import com.example.veranstaltung1.ui.customDialogs.ShowTimeDialog
import com.example.veranstaltung1.ui.theme.Veranstaltung1Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EditEventScreen(eventId: Int, navController: NavController, viewModel: EventsViewModel) {


    // Initial mutables
    val scope = rememberCoroutineScope()
    val event = remember {
        mutableStateOf(Constants.eventDetailPlaceHolder)
    }
    val currentEventNote = remember { mutableStateOf(event.value.eventNote) }
    val currentTitle = remember { mutableStateOf(event.value.title) }
    val currentPhotos = remember { mutableStateOf(event.value.imageUri) }
    val currentEventDate = remember { mutableStateOf(event.value.eventDate) }
    val currentEventTime = remember { mutableStateOf(event.value.eventTime) }

    val context = LocalContext.current
    val showDatePickerDialog = remember { mutableStateOf(false) }
    val showTimePickerDialog = remember { mutableStateOf(false) }

    val showDialogPhoto =  remember { mutableStateOf(false) }
    var urlString = remember {mutableStateOf("")}
    val downloader = AndroidDownloader(context)

    val saveButtonState = remember { mutableStateOf(false) }

    val getImageRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->

        if (uri != null) {
            EventApplication.getUriPermission(uri)
        }
        currentPhotos.value = uri.toString()
        if (currentPhotos.value != event.value.imageUri) {
            saveButtonState.value = true
        }

    }

    // Inserting the particular argument in the ui
    LaunchedEffect(true) {
        scope.launch(Dispatchers.IO) {
            event.value = viewModel.getEvent(eventId) ?: Constants.eventDetailPlaceHolder
            currentEventNote.value = event.value.eventNote
            currentTitle.value = event.value.title
            currentPhotos.value = event.value.imageUri
            currentEventDate.value = event.value.eventDate
            currentEventTime.value = event.value.eventTime
        }
    }

    // now working with the ui


    Veranstaltung1Theme { // App base theme
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                topBar = {
                    GenericAppBar(
                        title = "Edit Event",
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.save),
                                contentDescription = "save note",
                                tint = Color.Black
                            )
                        },
                        onIconClick = {
                            viewModel.updateNote(
                                EventData(
                                    id = event.value.id,
                                    eventNote = currentEventNote.value,
                                    title = currentTitle.value,
                                    eventDate = currentEventDate.value,
                                    eventTime = currentEventTime.value,
                                    imageUri = currentPhotos.value
                                )
                            )
                            navController.popBackStack() //???
                        },

                        iconState = saveButtonState
                    )
                }, // Another widget

                floatingActionButton = {
                    MultiFloatingActionButton(
                        fabIcon = "✎", // Imoji icon
                        items = arrayListOf(
                            FabItem(
                                icon = "\uD83D\uDD56",
                                label = "Time"
                            ) {
                                Toast.makeText(context, "Button 1 clicked", Toast.LENGTH_LONG).show()
                                showTimePickerDialog.value = true

                            },
                            FabItem(
                                icon = "\uD83D\uDCC5",
                                label = "Date"
                            ) {
                                Toast.makeText(context, "Button 2 clicked", Toast.LENGTH_LONG).show()
                                showDatePickerDialog.value = true
                            },
                            FabItem(
                                icon = "\uD83D\uDEDC",
                                label = "Download photo"
                            ) {
                                showDialogPhoto.value = true
                                Toast.makeText(context, "Use a valid Url to import a photo ", Toast.LENGTH_LONG).show()
                            },
                            FabItem(
                                icon = "\uD83D\uDCC2",
                                label = "Import Photo"
                            ) {
                                // find the image
                                getImageRequest.launch(arrayOf("image/*"))


                            }
                        ),



                        )
                },

                content = {

                    Column(
                        Modifier
                            .padding(12.dp)
                            .fillMaxSize()
                    ) {
                        if (currentPhotos.value != null && currentPhotos.value!!.isNotEmpty()) {
                            // Load the image gere agein
                            Image(
                                painter = rememberAsyncImagePainter(
                                    ImageRequest
                                        .Builder(LocalContext.current)
                                        .data(data = Uri.parse(currentPhotos.value))
                                        .build()
                                ),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.3f)
                                    .padding(6.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Row(
                            Modifier
                                .padding(4.dp)
                                .fillMaxSize()) {
                            Text(text = "Date: $currentEventDate")
                            Text(text = "   Time: $currentEventTime")
                        }
                        // saving changes in the edit
                        TextField(
                            value = currentTitle.value,
                            colors = TextFieldDefaults.textFieldColors(
                                cursorColor = Color.Black,
                                focusedLabelColor = Color.Black,
                            ),
                            onValueChange = { value ->
                                currentTitle.value = value
                                if (currentTitle.value != event.value.title) {
                                    saveButtonState.value = true
                                } else if (currentEventNote.value == event.value.eventNote &&
                                    currentTitle.value == event.value.title
                                ) {
                                    saveButtonState.value = false
                                }
                            },
                            label = { Text(text = "Title") }
                        )
                        Spacer(modifier = Modifier.padding(12.dp))
                        TextField(
                            value = currentEventNote.value,
                            colors = TextFieldDefaults.textFieldColors(
                                cursorColor = Color.Black,
                                focusedLabelColor = Color.Black,
                            ),
                            onValueChange = { value ->
                                currentEventNote.value = value
                                if (currentEventNote.value != event.value.eventNote) {
                                    saveButtonState.value = true
                                } else if (currentEventNote.value == event.value.eventNote &&
                                    currentTitle.value == event.value.title
                                ) {
                                    saveButtonState.value = false
                                }
                            },
                            label = { Text(text = "Body") }
                        )
                    }
                }

            )
            // Pass the selected date
            if (showDatePickerDialog.value) {

                ShowDateDialog(showDate = showDatePickerDialog) { selectedDate ->
                    currentEventDate.value = selectedDate


                }
            }

            // Pass the selected time

            if (showTimePickerDialog.value) {
                ShowTimeDialog(showTime = showTimePickerDialog) { onTimeSelected ->

                    currentEventTime.value = onTimeSelected


                }
            }

            if (showDialogPhoto.value) {
                CustomDialog(
                    value = "",
                    setShowDialog = {
                        showDialogPhoto.value = it // url value
                    },
                ) { value ->
                    urlString.value = value
                    downloader.downloadFile(value)

                }
            }

        }

    }

}
