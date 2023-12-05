package com.example.veranstaltung1.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.veranstaltung1.model.EventData
import com.example.veranstaltung1.persistenceDataModel.EventsDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat


/**
 * ViewModel for managing events in the application.
 *
 * This ViewModel provides data operations for retrieving, updating, creating, and deleting events.
 *
 * @param db The DAO (Data Access Object) for events to perform database operations.
 */

class EventsViewModel(
    private val db: EventsDao // create the object of our database

):ViewModel() {

    /**
     * LiveData containing a list of all events in the database.
     */
    val events: LiveData<List<EventData>> = db.getEvents() // Get all Events in the Database

    /**
     * Delete the event in the database
     */
    fun deleteEvents(event: EventData) {
        viewModelScope.launch(Dispatchers.IO) {
            db.deleteEvent(event) // Implemte a scope delay before deleting the data
        }
    }

    fun updateNote(event: EventData) {
        viewModelScope.launch(Dispatchers.IO) {
            db.updateEvent(event)
        }
    }


    /**
     * Create a new event in the database with the provided information.
     *
     * @param title The title of the event.
     * @param eventNote The description or notes related to the event.
     * @param eventDate The date of the event.
     * @param eventTime The time of the event.
     * @param image An optional parameter representing the image URI associated with the event.
     */

    fun createEvent(
        title: String,
        eventNote: String,
        eventDate: String,
        eventTime: String,
        image: String? = null
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            db.insertEvent(
                EventData(
                    title = title,
                    eventNote = eventNote,
                    eventDate = eventDate,
                    eventTime = eventTime,
                    imageUri = image
                )
            )

        }
    }

    /**
     * Get an event from the database by its ID.
     *
     * @param eventId The ID of the event to retrieve.
     * @return The EventData object representing the event, or null if not found.
     */
    suspend fun getEvent(eventId: Int): EventData? {
        return db.getEventById(eventId)
    }





}

/**
 * ViewModelFactory for creating instances of the EventsViewModel.
 *
 * This factory is used to create an instance of the EventsViewModel with the provided DAO.
 *
 * @param db The DAO (Data Access Object) for events to be used by the ViewModel.
 */
class EventsViewModelFactory(
    private val db: EventsDao,
): ViewModelProvider.NewInstanceFactory(){

    /**
     * Create a new instance of the specified ViewModel class.
     *
     * @param modelClass The class of the ViewModel to be created.
     * @return A new instance of the EventsViewModel.
     */
    override fun <T: ViewModel> create(modelClass: Class<T>):T{
        return EventsViewModel( db =db )as T
    }
}