package com.example.myitsuser.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class BusInfo(
    val name: String = "",
    val from: String = "",
    val to: String = "",
    val price: String = ""
)