package com.example.bisanj.screens

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Test(
    val test: String,
    val mapOfVariants: Map<Int, String>,
    var answer: Int,
    var choosingAnswer: Int
    ) : Parcelable
