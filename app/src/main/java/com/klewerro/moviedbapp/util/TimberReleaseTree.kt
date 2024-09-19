package com.klewerro.moviedbapp.util

import android.util.Log
import timber.log.Timber

class TimberReleaseTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        when (priority) {
            Log.INFO, Log.WARN -> Log.println(priority, tag, message)
            Log.ERROR -> {
                Log.println(priority, tag, message)
                // Eg. crashlytics
            }
            else -> return
        }
    }
}
