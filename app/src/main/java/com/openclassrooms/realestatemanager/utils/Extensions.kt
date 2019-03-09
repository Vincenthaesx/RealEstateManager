package com.openclassrooms.realestatemanager.utils

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.orhanobut.logger.Logger

inline fun <reified T : Any> Any.safeCast(action : (T) -> Unit) {
    if (this is T){
        action.invoke(this)
    }
}

fun String.log() {
    Logger.d(this)
}

fun Any.log() {
    Logger.d(this.toString())
}

fun Throwable.log() {
    Logger.e(this, this.message?:"")
}


inline fun <reified Activity : AppCompatActivity> Context.openActivity(
        intent: Intent = Intent(this, Activity::class.java)) {
        this.startActivity(intent)
}
