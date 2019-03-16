package com.pet.kaleidoscope.logic.storage

import android.app.Activity
import android.content.Context

/**
 * @author Dmitry Borodin on 3/14/19.
 */
abstract class BasePreferences(appContext: Context, fileName: String) {

    private var preferences = appContext.getSharedPreferences(fileName, Activity.MODE_PRIVATE)!!

    protected fun getString(key: String, defaultValue: String?): String? {
        return preferences.getString(key, defaultValue)
    }

    protected fun setString(key: String, value: String?) {
        preferences.edit().putString(key, value).apply()
    }

    fun clear(key: String) {
        preferences.edit().remove(key).apply()
    }

    fun clearAllEntries() {
        preferences.edit().clear().apply()
    }
}