package com.openclassrooms.realestatemanager.utils

import android.content.Context
import android.net.wifi.WifiManager
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    val dateFormat = "dd/MM/yyyy"

    val API_KEY =  "AIzaSyCPwec8XpQW3rbXeT9-1b15ibSiGLcrlPM"

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

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param context
     * @return
     */
    fun isInternetAvailable(context: Context): Boolean {
        val wifi = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wifi.isWifiEnabled
    }
}
