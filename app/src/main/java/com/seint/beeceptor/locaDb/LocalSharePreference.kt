package com.seint.beeceptor.locaDb

import android.app.Application

object LocalSharePreference {
    fun saveLastUpdateTime(application: Application, timeInMilSecond: Long) {
            val pref = application.getSharedPreferences("MyPref", 0) // 0 - for private mode
            val editor = pref.edit()
            editor.putLong("lastCheckedMillis", timeInMilSecond)
            editor.commit()


    }

    fun getLastUpdatedTime(application: Application): Long {
        val pref = application.getSharedPreferences("MyPref", 0) // 0 - for private mode
        return pref.getLong("lastCheckedMillis", 0)
    }
}
