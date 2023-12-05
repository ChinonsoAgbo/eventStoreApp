package com.example.veranstaltung1

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.room.Room
import com.example.veranstaltung1.persistenceDataModel.EventsDao
import com.example.veranstaltung1.persistenceDataModel.EventsDatabase

/*
EventApp: This will run as soon our app is been created and use this to cereate an instance of our  Application concept which we will use to crreate our data base
 */
class EventApplication: Application(){
    private var db: EventsDatabase? = null

    init{ instance = this }

    private fun getDb(): EventsDatabase {

        // Reurn the Data base for this app if not null
        return if (db !=null){
            db!!
        }else{
            // initialize a new database
            db = Room.databaseBuilder(
                instance!!.applicationContext,
                EventsDatabase::class.java, Constants.DATABASE_NAME
            ).fallbackToDestructiveMigration()// remove in prod --> If Data changes  in Event Database and we wouldnt save this data and restore the data
                .build()
            return db!!
        }
    }

    companion object {
        private var instance: EventApplication? = null

        // Get our Event Doa so we can get oBJect to delete note Expl
        fun getDao(): EventsDao {
            return instance!!.getDb().EventDao()
        }


        // used for getting URI Permission bc we need permission when we relaunches
        fun getUriPermission(uri: Uri) {
            instance!!.applicationContext.contentResolver.takePersistableUriPermission(
                uri, // When get Permission it will be persisited
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }
    }

}