package com.example.myitsdriver.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class BusLocation(
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
)