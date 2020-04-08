package com.example.myitsuser.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class BusLocation(
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
)