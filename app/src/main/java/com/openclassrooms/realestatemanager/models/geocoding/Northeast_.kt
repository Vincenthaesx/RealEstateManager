package com.openclassrooms.realestatemanager.models.geocoding

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Northeast_ {

    @SerializedName("lat")
    @Expose
    var lat: Double? = null
    @SerializedName("lng")
    @Expose
    var lng: Double? = null

}