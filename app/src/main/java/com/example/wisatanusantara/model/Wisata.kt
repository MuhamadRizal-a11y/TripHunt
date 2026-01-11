package com.example.wisatanusantara.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Wisata(
    val name: String,
    val description: String,
    val photo: Int,
    val location: String,
    val rating: String,
    val price: String,
    val hours: String
) : Parcelable