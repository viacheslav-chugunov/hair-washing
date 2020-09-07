package io.hairwashing.extensions

import android.app.Activity
import android.content.Intent

fun Activity.startActivityWithoutBackStack(newActivity: Class<*>) {
    startActivity(Intent(this, newActivity).apply {
        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
    })
    finish()
}