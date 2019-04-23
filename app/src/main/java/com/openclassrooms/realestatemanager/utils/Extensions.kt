package com.openclassrooms.realestatemanager.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.orhanobut.logger.Logger
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

fun String.log() {
    Logger.d(this)
}

fun Any.log() {
    Logger.d(this.toString())
}

fun Throwable.log() {
    Logger.e(this, this.message ?: "")
}

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}

fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction { add(frameId, fragment) }
}

fun doubleToStringNoDecimal(d: Int): String {
    val formatter = NumberFormat.getInstance() as DecimalFormat
    formatter.applyPattern("#,###")
    return formatter.format(d)
}

private val BASE_FORMAT = SimpleDateFormat("dd/MM/yyyy")

fun String.toFRDate() = BASE_FORMAT.parse(this)

fun Date.toFRString() = BASE_FORMAT.format(this)

fun Long.toFRDate() = Date(this)

