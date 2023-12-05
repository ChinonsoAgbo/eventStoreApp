package com.example.veranstaltung1.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.veranstaltung1.Constants
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// @Entity makes sure that the Data class will create a Database in our Room Database
@Entity(tableName = Constants.TABLE_NAME,indices = [Index(value=["id"], unique =true )] )
data class EventData(

    @PrimaryKey(autoGenerate = true)  val id: Int? = null,
    @ColumnInfo(name = "eventNote")       val eventNote: String,
    @ColumnInfo(name = "title")     val title: String,
    @ColumnInfo(name = "eventDate")   val eventDate: String, // CREATE a func to hold event date and remove the null stype
    @ColumnInfo(name = "eventTime")   val eventTime: String, // CREATE a func to hold event date
    @ColumnInfo(name = "dateUpdated")   val dateUpdated: String = getDateCreated(),
    @ColumnInfo(name = "imageUri")     val imageUri: String? = null

)



// Date Date formatting

fun getDateCreated(): String {
    return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
}

fun EventData.getDay(): String{
    if (this.dateUpdated.isEmpty()) return ""
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return LocalDateTime.parse(this.dateUpdated,formatter ).toLocalDate().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"))
}
