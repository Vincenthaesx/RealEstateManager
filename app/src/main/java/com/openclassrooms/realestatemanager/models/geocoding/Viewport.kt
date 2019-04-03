package com.openclassrooms.realestatemanager.models.geocoding

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Viewport {

    @SerializedName("northeast")
    @Expose
    var northeast: Northeast_? = null
    @SerializedName("southwest")
    @Expose
    var southwest: Southwest_? = null

}