package com.example.veranstaltung1.ui.createEvent

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
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
import com.example.veranstaltung1.EventApplication
import com.example.veranstaltung1.R
import com.example.veranstaltung1.download.AndroidDownloader
import com.example.veranstaltung1.ui.EventsViewModel
import com.example.veranstaltung1.ui.GenericAppBar
import com.example.veranstaltung1.ui.customDialogs.CustomDialog
import com.example.veranstaltung1.ui.customDialogs.FabItem
import com.example.veranstaltung1.ui.customDialogs.MultiFloatingActionButton
import com.example.veranstaltung1.ui.customDialogs.ShowDateDialog
import com.example.veranstaltung1.ui.customDialogs.ShowTimeDialog
import com.example.veranstaltung1.ui.theme.Veranstaltung1Theme

/**
 * Composable function for creating a new event screen.
 *
 * This screen allows users to create a new event by providing information such as title,
 * description, date, time, and optional photos.
 *
 * @param navController The navigation controller to handle navigation within the app.
 * @param viewModel The view model associated with events to handle data and business logic.
 */
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CreateEventScreen( navController: NavController, viewModel: EventsViewModel) {



    val currentEventNote = remember { mutableStateOf("") }
    val currentTitle = remember { mutableStateOf("") }
    val currentPhotos = remember { mutableStateOf("") }
    val currentEventDate = remember { mutableStateOf("") }
    val currentEventTime = remember { mutableStateOf("") }

    val context = LocalContext.current
    val showDatePickerDialog = remember { mutableStateOf(false) }
    val showTimePickerDialog = remember { mutableStateOf(false) }

    val showDialogPhoto =  remember { mutableStateOf(false) }
    var urlString = remember { mutableStateOf("") }
    val downloader = AndroidDownloader(context)

    val saveButtonState = remember { mutableStateOf(false) }

    val getImageRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->

        if (uri != null) {
            EventApplication.getUriPermission(uri)
        }
        // assign image uri to the current phot
        currentPhotos.value = uri.toString()


    }



    Veranstaltung1Theme { // App base theme
        // A surface container using the 'background' color from the theme
        Surface(

            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                topBar = {
                    GenericAppBar(
                        title = "Create Event",
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.save),
                                contentDescription = "save note",
                                tint = Color.Black
                            )
                        },
                        onIconClick = {
                            // create the event
                            viewModel.createEvent(
                                currentTitle.value,
                                currentEventNote.value,
                                currentEventDate.value,
                                currentEventTime.value,
                                currentPhotos.value
                            )
                            navController.popBackStack() // navigate after to the main screen
                        },
                        iconState = saveButtonState
                    )
                }, // Another widget

                floatingActionButton = {
                    MultiFloatingActionButton(
                        fabIcon = "✎ ", // Imoji icon
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
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {
                        if ( currentPhotos.value.isNotEmpty()) {
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

                        // saving changes in the edit

                        TextField(
                            value = currentTitle.value,
                            modifier = Modifier

                                .fillMaxWidth(),
                            colors = TextFieldDefaults.textFieldColors(
                                cursorColor = Color.Black,
                                focusedLabelColor = Color.Black,
                            ),
                            onValueChange = { value ->
                                currentTitle.value = value
                                // save button can show when time,date, title, body is not ""
                                    saveButtonState.value = currentTitle.value!="" && currentEventNote.value!=""

                                } ,

                            label = { Text(text = "Title") }
                        )
                        Spacer(modifier = Modifier.padding(12.dp))
                        TextField(
                            value = currentEventNote.value,
                            colors = TextFieldDefaults.textFieldColors(
                                cursorColor = Color.Black,
                                focusedLabelColor = Color.Black,
                            ),
                            modifier = Modifier
                                .fillMaxHeight(0.5f)
                                .fillMaxWidth(),
                            onValueChange = { value ->
                                currentEventNote.value = value
                                saveButtonState.value =
                                    currentTitle.value != "" && currentEventNote.value != "" // save or note save ?
                            },
                            label = { Text(text = "Body") }
                        )
                        Row(



                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.Bottom

                        ) {
                            Text(text = "Date: ${currentEventDate.value}")
                            Text(text = "   Time: ${currentEventTime.value}")
                        }

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