package com.openclassrooms.realestatemanager.utils

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.orhanobut.logger.Logger

fun String.log() {
    Logger.d(this)
}

fun Any.log() {
    Logger.d(this.toString())
}

fun Throwable.log() {
    Logger.e(this, this.message ?: "")
}


inline fun <reified Activity : AppCompatActivity> Context.openActivity(
        intent: Intent = Intent(this, Activity::class.java)) {
    this.startActivity(intent)
}

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}

fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction { add(frameId, fragment) }
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction { replace(frameId, fragment) }
}

