package com.example.veranstaltung1.persistenceDataModel

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.veranstaltung1.model.EventData

@Database(entities = [
    EventData::class], version = 2)
abstract class EventsDatabase: RoomDatabase() {

    abstract fun EventDao(): EventsDao
}