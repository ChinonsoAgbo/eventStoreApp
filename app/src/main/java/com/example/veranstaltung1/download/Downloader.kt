package com.example.veranstaltung1.download

interface Downloader {
    fun downloadFile(url: String): Long // use this too check if the download manager is finished
}