package com.example.veranstaltung1.persistenceDataModel

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.veranstaltung1.model.EventData

/**
 * Data Access Object (DAO) interface for interacting with the "Events" table in the database.
 * Provides methods for querying, inserting, updating, and deleting events.
 */
@Dao
interface EventsDao {
    /**
     * Retrieves an event from the database based on its ID.
     *
     * @param id The unique identifier of the event.
     * @return The corresponding {@link EventData} object, or null if not found.
     */
    @Query("SELECT * FROM Events WHERE events.id =:id")
    suspend fun getEventById(id:Int): EventData?

    /**
     * Retrieves all events from the database, ordered by the date of the last update.
     *
     * @return A {@link LiveData} containing a list of {@link EventData} objects.
     */
    @Query("SELECT * FROM Events ORDER BY dateUpdated DESC")
    fun getEvents(): LiveData<List<EventData>>

    /**
     * Deletes an event from the database.
     *
     * @param event The {@link EventData} object to be deleted.
     * @return The number of rows affected in the database (should be 1 if successful).
     */

    @Delete
    fun deleteEvent(event:EventData):Int

    /**
     * Updates an existing event in the database.
     *
     * @param event The {@link EventData} object containing updated information.
     * @return The number of rows affected in the database (should be 1 if successful).
     */

    @Update
    fun  updateEvent(event: EventData): Int


    /**
     * Inserts a new event into the database.
     *
     * @param event The {@link EventData} object to be inserted.
     */
    @Insert
    fun insertEvent(event: EventData)
}