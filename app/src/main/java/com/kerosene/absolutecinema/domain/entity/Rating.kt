package com.kerosene.absolutecinema.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Rating(
    val kp: Double,
) : Parcelable
