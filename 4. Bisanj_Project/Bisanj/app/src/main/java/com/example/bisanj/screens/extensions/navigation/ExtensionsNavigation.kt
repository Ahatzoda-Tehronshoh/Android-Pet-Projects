package com.example.bisanj.screens.extensions.navigation

import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.bisanj.R


fun Fragment.findTopNavNavController(): NavController {
    val topLevelHost =
        requireActivity().supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment?
    return topLevelHost?.navController ?: findNavController()
}

fun Fragment.finChildNavController(): NavController {
    val topLevelHost =
        childFragmentManager.findFragmentById(R.id.menu_fragment_container_view) as NavHostFragment?
    return topLevelHost?.navController ?: findNavController()
}

fun NavController.navigateSafely(@IdRes actionId: Int) {
    currentDestination?.getAction(actionId)?.let { navigate(actionId) }
}

fun NavController.navigateSafely(directions: NavDirections) {
    currentDestination?.getAction(directions.actionId)?.let { navigate(directions) }
}

fun Fragment.overrideOnBackPressed(onBackPressed: OnBackPressedCallback.() -> Unit) =
    requireActivity().onBackPressedDispatcher.addCallback(this) {
        onBackPressed()
    }