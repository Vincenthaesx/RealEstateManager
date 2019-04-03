package com.openclassrooms.realestatemanager.models.geocoding

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResultGeocoding {

    @SerializedName("results")
    @Expose
    var results: List<Result>? = null
    @SerializedName("status")
    @Expose
    var status: String? = null

}