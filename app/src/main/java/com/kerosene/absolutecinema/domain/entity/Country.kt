package com.kerosene.absolutecinema.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Country(
    val name: String,
): Parcelable
