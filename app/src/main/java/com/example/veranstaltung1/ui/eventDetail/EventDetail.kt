package com.example.veranstaltung1.ui.eventDetail

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.veranstaltung1.Constants
import com.example.veranstaltung1.Constants.eventDetailPlaceHolder
import com.example.veranstaltung1.R
import com.example.veranstaltung1.ui.EventsViewModel
import com.example.veranstaltung1.ui.GenericAppBar
import com.example.veranstaltung1.ui.theme.Veranstaltung1Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Composable function representing the screen for displaying event details.
 *
 * @param eventId The unique identifier of the event to be displayed.
 * @param navController The navigation controller for navigating between screens.
 * @param viewModel The ViewModel responsible for managing event-related data.
 *
 * This composable function fetches event details using the provided [viewModel] and displays them.
 * The event details include the event title, image, update date, and event note.
 *
 * The composable uses a coroutine to asynchronously fetch event details from the [viewModel].
 * The [GenericAppBar] is used for the top app bar, allowing users to navigate to the event edit screen.
 * The event details are displayed within a [Column] layout, including the event image, title, update date,
 * and event note.
 *
 * @see GenericAppBar
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EventDetailScreen(eventId:Int, navController: NavController, viewModel: EventsViewModel) {

    val scope = rememberCoroutineScope()
    val event = remember { mutableStateOf(eventDetailPlaceHolder) } // This event-remember remember the state of the Detail incase the app was recomposed  and can use this state to update the composable


    // Using a coroutine to collect our Detail
    LaunchedEffect(true){
        scope.launch(Dispatchers.IO){
            event.value = viewModel.getEvent(eventId)?: eventDetailPlaceHolder
        }
    }
    
    Veranstaltung1Theme { // App base theme
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold (
                topBar = {
                    // Call out Generic func from Shared
                    GenericAppBar(
                        title =  event.value.title ,
                        onIconClick = {
                            navController.navigate(Constants.eventEditNavigation(event.value.id ?:0))}, // what d fck happend here
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.edit_note),
                                contentDescription = stringResource(R.string.edit_note_icon),
                                tint= Color.Black,
                            )
                        },
                        iconState =  remember{ mutableStateOf(true) }
                    )
                },

            ){
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (event.value.imageUri != null && event.value.imageUri!!.isNotEmpty()) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                ImageRequest
                                    .Builder(LocalContext.current)
                                    .data(data = Uri.parse(event.value.imageUri))
                                    .build()
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxHeight(0.3f)
                                .fillMaxWidth()
                                .padding(6.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Text(
                        text = event.value.title,
                        modifier = Modifier.padding(top = 24.dp, start = 12.dp, end = 24.dp),
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = event.value.dateUpdated, Modifier.padding(12.dp), color = Color.Gray)
                    Text(text = event.value.eventNote, Modifier.padding(12.dp))

                }


            }

        }
    }

}