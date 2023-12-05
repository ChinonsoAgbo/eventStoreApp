package com.example.veranstaltung1
/**
 * MainActivity hosts the main navigation graph for the Veranstaltung1 app.
 *
 * This activity initializes the ViewModel, sets up the Compose UI, and defines the
 * navigation graph using the Jetpack Navigation Compose library.
 *
 * @author Your Name
 * @version 1.0
 * @since 2023-12-05
 */

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.veranstaltung1.ui.EventsViewModel
import com.example.veranstaltung1.ui.EventsViewModelFactory
import com.example.veranstaltung1.ui.createEvent.CreateEventScreen
import com.example.veranstaltung1.ui.editEvent.EditEventScreen
import com.example.veranstaltung1.ui.eventDetail.EventDetailScreen
import com.example.veranstaltung1.ui.eventList.EventList
import com.example.veranstaltung1.ui.theme.Veranstaltung1Theme
/**
 * The main entry point for the Veranstaltung1 app.
 */
class MainActivity : ComponentActivity() {
    private lateinit var eventsViewModel: EventsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * retrieve viewModel
          */

        eventsViewModel =  EventsViewModelFactory(EventApplication.getDao()).create(EventsViewModel::class.java)
        setContent {
            Veranstaltung1Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Constants.NAVIGATION_EVENTS_LIST
                    ) {
                        // Notes List
                        composable(Constants.NAVIGATION_EVENTS_LIST){
                            EventList(
                            navController = navController,
                            viewModel = eventsViewModel
                        )}
                        // Event Detail page
                        composable(
                            Constants.NAVIGATION_EVENTS_DETAIL,
                            arguments = listOf(navArgument(Constants.NAVIGATION_EVENTS_ID_ARGUMENT) {
                                type = NavType.IntType
                            })
                        ) { backStackEntry ->
                            backStackEntry.arguments?.getInt(Constants.NAVIGATION_EVENTS_ID_ARGUMENT)
                                ?.let { EventDetailScreen(eventId = it, navController, eventsViewModel) }
                        }

                        // Notes Edit page
                        composable(
                            Constants.NAVIGATION_EVENTS_EDIT,
                            arguments = listOf(navArgument(Constants.NAVIGATION_EVENTS_ID_ARGUMENT) {
                                type = NavType.IntType
                            })
                        ) { backStackEntry ->
                            backStackEntry.arguments?.getInt(Constants.NAVIGATION_EVENTS_ID_ARGUMENT)
                                ?.let { EditEventScreen(eventId = it, navController, eventsViewModel) }
                        }

                        // Create Event Page
                        composable(Constants.NAVIGATION_EVENTS_CREATE) { CreateEventScreen(navController, eventsViewModel) }



                    }


                }
            }
        }
    }
}
