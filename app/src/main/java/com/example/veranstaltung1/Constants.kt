package com.example.veranstaltung1

import com.example.veranstaltung1.model.EventData


object Constants {
    const val NAVIGATION_EVENTS_LIST = "eventsList"
    const val NAVIGATION_EVENTS_CREATE = "eventsCreated"
    const val  NAVIGATION_EVENTS_DETAIL = "eventsDetail/{eventId}"
    const val NAVIGATION_EVENTS_EDIT = "eventEdit/{eventId}"
    const val NAVIGATION_EVENTS_ID_ARGUMENT = "eventId"
    const val TABLE_NAME = "Events"
    const val DATABASE_NAME ="EventsDatabase"

    fun eventDetailNavigation(eventId:Int) = "eventDetail/$eventId"

    fun eventEditNavigation(eventId:Int) = "eventEdit/$eventId"

    fun List<EventData>?.orPlaceHolderList(): List<EventData> {
        fun placeHolderList(): List<EventData> {
            return listOf(EventData(id = 0, title = "No Event Found", eventNote = "Please create an Event.", eventDate = " ", eventTime = " ", dateUpdated = ""))
        }
        return if (this != null && this.isNotEmpty()){
            this
        } else placeHolderList()
    }

    val eventDetailPlaceHolder = EventData(eventNote = "Cannot find note details", id = 0, title = "Cannot find note details", eventDate = " ", eventTime = " ")
}