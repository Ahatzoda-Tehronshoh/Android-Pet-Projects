package com.example.bisanj.screens.extensions.utils

import com.example.bisanj.R
import com.example.bisanj.SettingFonChangeFragment
import com.example.bisanj.screens.Subject

const val SHARED_PREFERENCES_KEY = "shared preferences key"
const val SHARED_PREFERENCES_NOTIFICATION_KEY = "shared preferences notification key"

var countOfTests = 10
    set(value) {
        field = when (value) {
            10 -> value
            20 -> value
            else -> 30
        }
    }

var duringOfTest = 0
    set(value) {
        field = when (value) {
            5 -> value
            10 -> value
            15 -> value
            else -> 0
        }
    }

var fon = SettingFonChangeFragment.BLUE
var isNotificationSet = false

var showSecond = false

val SUBJECT_LIST = listOf(
    Subject("Математика", R.drawable.math_icon),
    Subject("География", R.drawable.geograthy_icon),
    Subject("Биология", R.drawable.biology_icon),
    Subject("Таърих", R.drawable.history_icon),
    Subject("Физика", R.drawable.physic_icon),
    Subject("Англисӣ", R.drawable.english_icon),
    Subject("Забони тоҷикӣ", R.drawable.tajik_icon),
    Subject("Адабиёт", R.drawable.adab_icon),
    Subject("Русӣ", R.drawable.russian_icon),
    Subject("Ҳуқук", R.drawable.geograthy_icon),
    Subject("Химия", R.drawable.english_icon),
)
