package com.kerosene.absolutecinema.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocalLibrary
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.kerosene.absolutecinema.R

sealed class NavigationItem(
    val titleResId: Int,
    val icon: ImageVector,
) {

    object Home : NavigationItem(
        titleResId = R.string.navigation_item_home,
        icon = Icons.Outlined.Home
    )

    object Search : NavigationItem(
        titleResId = R.string.navigation_item_search,
        icon = Icons.Outlined.Search
    )

    object Library : NavigationItem(
        titleResId = R.string.navigation_item_library,
        icon = Icons.Outlined.LocalLibrary
    )
}