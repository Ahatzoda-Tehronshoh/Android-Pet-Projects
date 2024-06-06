package com.example.messenger.model

data class Message(var sent: Boolean = false, val text: String = "", val date: Long = 0L)
