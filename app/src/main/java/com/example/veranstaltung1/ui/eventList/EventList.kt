package com.example.veranstaltung1.ui.eventList

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.AlertDialog

import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.veranstaltung1.Constants
import com.example.veranstaltung1.Constants.orPlaceHolderList
import com.example.veranstaltung1.R
import com.example.veranstaltung1.model.EventData
import com.example.veranstaltung1.model.getDay
import com.example.veranstaltung1.ui.EventsViewModel
import com.example.veranstaltung1.ui.GenericAppBar
import com.example.veranstaltung1.ui.theme.Veranstaltung1Theme
import com.example.veranstaltung1.ui.theme.noteBGBlue
import com.example.veranstaltung1.ui.theme.noteBGYellow

/**
 * Composable function representing the screen for displaying a list of events.
 *
 * @param navController The navigation controller for navigating between screens.
 * @param viewModel The ViewModel responsible for managing event-related data.
 *
 * This composable function displays a list of events, allowing users to search, add, and delete events.
 * It includes a search bar, a list of events, and a floating action button for adding new events.
 * Users can delete events by long-pressing on an event item, and a confirmation dialog is shown before deletion.
 *
 * @param openDialog A mutable state to control the visibility of the delete confirmation dialog.
 * @param deleteText A mutable state to hold the text content of the delete confirmation dialog.
 * @param eventsToDelete A mutable state to hold the list of events selected for deletion.
 *
 * @see SearchBar
 * @see EventList
 * @see DeleteDialog
 */
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EventList(navController: NavController, viewModel: EventsViewModel) {

    val openDialog = remember { mutableStateOf(false) }
    val deleteText = remember { mutableStateOf("") }
    val eventsQuery = remember { mutableStateOf("") }
    val eventsToDelete = remember { mutableStateOf(listOf<EventData>()) }
    val events = viewModel.events.observeAsState()
    val context = LocalContext.current

    Veranstaltung1Theme { // App base theme
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                topBar = {
                    GenericAppBar(
                        title = stringResource(R.string.save_your_events),
                        onIconClick = {
                            if (events.value?.isNotEmpty() == true) {
                                openDialog.value = true
                                deleteText.value = "Are you sure you want to delete all events ?"
                                eventsToDelete.value = events.value ?: emptyList()
                            } else {
                                Toast.makeText(context, "No Events found.", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(
                                    id = R.drawable.note_delete
                                ),
                                contentDescription = stringResource(R.string.delet_event),
                                tint = Color.Black
                            )
                        },
                        iconState = remember { mutableStateOf(true) }

                    )
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { navController.navigate(Constants.NAVIGATION_EVENTS_CREATE) /* Navigate to event create*/
                        }) {

                       Icon(  imageVector = Icons.Default.Add,
                           contentDescription = "Add" ,
                           tint = Color.Black)
                    }
                }
            ) {
                Column() {
                    SearchBar(eventsQuery)
                    EventList(
                        events = events.value.orPlaceHolderList(),
                        query = eventsQuery,
                        openDialog = openDialog,
                        deleteText = deleteText,
                        navController = navController,
                        eventsToDelete = eventsToDelete
                    )
                }
                DeleteDialog(
                    openDialog = openDialog,
                    text = deleteText,
                    action = {
                        eventsToDelete.value.forEach {
                            viewModel.deleteEvents(it)
                        }
                    },
                    eventsToDelete = eventsToDelete
                )
            }
        }
    }

}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(query: MutableState<String>) {
    Column(Modifier.padding(top = 12.dp, start = 12.dp, end = 12.dp, bottom = 0.dp)) {
        TextField(
            value = query.value,
            placeholder = { Text("Search..") },
            maxLines = 1,
            onValueChange = { query.value = it },
            modifier = Modifier
                .background(Color.White)
                .clip(RoundedCornerShape(12.dp))
                .fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                Color.Black,
            ),
            trailingIcon = {
                AnimatedVisibility(
                    visible = query.value.isNotEmpty(),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    IconButton(onClick = { query.value = "" }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.icon_cross),
                            contentDescription = stringResource(
                                R.string.clear_search
                            )
                        )
                    }
                }

            })

    }
}

@Composable
fun EventList(
    events: List<EventData>,
    openDialog: MutableState<Boolean>,
    query: MutableState<String>,
    deleteText: MutableState<String>,
    navController: NavController,
    eventsToDelete: MutableState<List<EventData>>,
) {
    var previousHeader = ""
    LazyColumn(
        contentPadding = PaddingValues(12.dp),
        modifier = Modifier.background(MaterialTheme.colorScheme.primary)
    ) {
        val queriedEvents = if (query.value.isEmpty()){
            events
        } else {
            events.filter { it.eventNote.contains(query.value) || it.title.contains(query.value) }
        }
        itemsIndexed(queriedEvents) { index, events ->
            if (events.getDay() != previousHeader) {
                Column(
                    modifier = Modifier
                        .padding(6.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = events.getDay(), color = Color.Black)
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                )
                previousHeader =  events.getDay()
            }


            EventListItem(
                events,
                openDialog,
                deleteText = deleteText ,

                navController,

                eventsToDelete = eventsToDelete,

                eventBackGround = if (index % 2 == 0) {
                    noteBGYellow
                } else noteBGBlue
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
            )
        }
    }
}





@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EventListItem(
    events: EventData,
    openDialog: MutableState<Boolean>,

    deleteText: MutableState<String>,
    navController: NavController,

    eventsToDelete: MutableState<List<EventData>>,
    eventBackGround: Color,
) {


    return Box(modifier = Modifier
        .height(120.dp)
        .clip(RoundedCornerShape(12.dp))) {
        Column(
            modifier = Modifier
                .background(eventBackGround)
                .fillMaxWidth()
                .height(120.dp)
                .combinedClickable(interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = false), // You can also change the color and radius of the ripple
                    onClick = {
                        if (events.id != 0) {
                            navController.navigate(Constants.eventDetailNavigation(events.id ?: 0))
                        }
                    },
                    onLongClick = {
                        if (events.id != 0) {
                            openDialog.value = true
                            deleteText.value = "Are you sure you want to delete this event ?"
                            eventsToDelete.value = mutableListOf(events)
                        }
                    }
                )

        ) {
            Row(){
                if (events.imageUri != null && events.imageUri.isNotEmpty()){
                    // load firs image into view
                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest
                                .Builder(LocalContext.current)
                                .data(data = Uri.parse(events.imageUri))
                                .build()
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth(0.3f),
                        contentScale = ContentScale.Crop
                    )
                }

                Column() {
                    Text(
                        text = events.title,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                    Text(
                        text = "Date: ${events.eventDate}   time: ${events.eventTime}",
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                    Text(
                        text = events.eventNote,
                        color = Color.Black,
                        maxLines = 3,
                        modifier = Modifier.padding(12.dp)
                    )
                    Text(
                        text = events.dateUpdated,
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            }

        }
    }
}



@Composable

fun DeleteDialog(
    openDialog: MutableState<Boolean>,
    text: MutableState<String>,
    action: () -> Unit,
    eventsToDelete: MutableState<List<EventData>>,

) {

            if (openDialog.value) {

                AlertDialog(
                    onDismissRequest = {
                        openDialog.value = false
                    },
                    title = {
                        Text(text = "Delete Events")
                    },
                    text = {
                        Text("Are sure you want to delete this event ?")
                    },
                    confirmButton = {
                        Button(

                            onClick = {
                                action.invoke()
                                openDialog.value = false
                                eventsToDelete.value = mutableListOf()

                            }) {
                            Text("Yes")
                        }
                    },
                    dismissButton = {
                        Button(

                            onClick = {
                                openDialog.value = false
                                eventsToDelete.value = mutableListOf()



                            }) {
                            Text("No")
                        }
                    }
                )
            }
        }





