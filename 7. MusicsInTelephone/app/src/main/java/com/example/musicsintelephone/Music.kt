package com.example.musicsintelephone

import android.graphics.Bitmap

data class Music(
    val id: Int,
    val artist: String,
    val title: String,
    val data: String,
    val displayName: String,
    val duration: Int,
    val imageBitMap: Bitmap? = null,
    var isPlaying: Boolean,
    var isOnPause: Boolean = false
)