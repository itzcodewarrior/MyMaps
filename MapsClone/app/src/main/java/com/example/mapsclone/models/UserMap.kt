package com.example.mapsclone.models

import java.io.Serializable

data class UserMap(val title:String,val places: List<Place>,var isBookmark: Boolean = false): Serializable


