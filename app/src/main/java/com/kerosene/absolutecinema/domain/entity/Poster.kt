package com.kerosene.absolutecinema.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Poster(
    val url: String?,
) : Parcelable
