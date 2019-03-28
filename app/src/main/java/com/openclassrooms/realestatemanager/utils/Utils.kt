package com.openclassrooms.realestatemanager.utils

import java.text.SimpleDateFormat
import java.util.*

object Utils {

    private const val dateFormat = "dd/MM/yyyy"

    const val API_KEY = "key=AIzaSyCPwec8XpQW3rbXeT9-1b15ibSiGLcrlPM"

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @return
     */
//    val todayDate: String
//        get() {
//            val dateFormat = SimpleDateFormat("yyyy/MM/dd")
//            return dateFormat.format(Date())
//        }

    fun getTodayDate(): String {
        val df = SimpleDateFormat(dateFormat, Locale.getDefault())
        return df.format(Date())
    }

    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param dollars
     * @return
     */
    fun convertDollarToEuro(dollars: Int): Int {
        return Math.round(dollars * 0.812).toInt()
    }

}
