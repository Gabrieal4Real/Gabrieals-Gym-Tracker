package org.gabrieal.gymtracker.util.systemUtil

import android.content.Context
import android.content.SharedPreferences as AndroidSharedPreferences
import androidx.core.content.edit

class AndroidPreferences(context: Context) : SharedPreferences {
    private val prefs: AndroidSharedPreferences =
        context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)

    override fun putString(key: String, value: String) {
        prefs.edit { putString(key, value) }
    }

    override fun getString(key: String, defaultValue: String): String {
        return prefs.getString(key, defaultValue) ?: defaultValue
    }

    override fun putInt(key: String, value: Int) {
        prefs.edit { putInt(key, value) }
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        return prefs.getInt(key, defaultValue)
    }

    override fun putBoolean(key: String, value: Boolean) {
        prefs.edit { putBoolean(key, value) }
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return prefs.getBoolean(key, defaultValue)
    }

    override fun clear() {
        prefs.edit { clear() }
    }
}

actual fun providePreferences(context: Any?): SharedPreferences {
    require(context is Context) { "Expected an Android Context" }
    return AndroidPreferences(context)
}
