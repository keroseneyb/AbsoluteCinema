package com.kerosene.absolutecinema.presentation.utils

import kotlin.coroutines.cancellation.CancellationException

object Constants {
    const val UNKNOWN_ERROR = "Unknown error"
}

suspend inline fun <T> handleApiCall(
    crossinline apiCall: suspend () -> T,
    crossinline onLoading: () -> Unit = {},
    crossinline onSuccess: (T) -> Unit,
    crossinline onError: (String) -> Unit
) {
    onLoading()
    runCatching {
        apiCall()
    }.fold(
        onSuccess = { onSuccess(it) },
        onFailure = { e ->
            if (e is CancellationException) throw e
            onError(e.message ?: Constants.UNKNOWN_ERROR)
        }
    )
}