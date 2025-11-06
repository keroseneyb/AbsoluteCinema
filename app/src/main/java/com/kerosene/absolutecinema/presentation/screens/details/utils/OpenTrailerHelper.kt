package com.kerosene.absolutecinema.presentation.screens.details.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.kerosene.absolutecinema.presentation.di.qualifers.AppContext
import javax.inject.Inject

class OpenTrailerHelper @Inject constructor(
    @AppContext private val context: Context,
) {
    fun openTrailer(url: String) {
        if (url.isEmpty()) return

        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.e("OpenTrailerHelper", "Error: ${e.message}")
        }
    }
}