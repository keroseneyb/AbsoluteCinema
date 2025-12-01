package com.kerosene.absolutecinema.presentation.navigation.utils

import androidx.navigation.NavController
import androidx.navigation.NavGraph

fun NavController.popToStartDestinationOfGraph(graphRoute: String) {
    val graph = graph.findNode(graphRoute) as? NavGraph

    val startId = graph?.startDestinationId
    if (startId != null) {
        popBackStack(startId, inclusive = false)
    }
}