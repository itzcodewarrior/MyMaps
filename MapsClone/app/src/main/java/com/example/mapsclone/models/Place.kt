package com.example.mapsclone.models

import java.io.Serializable

data class Place(val name: String, val description: String, val latitude: Double, val longitude: Double): Serializable
