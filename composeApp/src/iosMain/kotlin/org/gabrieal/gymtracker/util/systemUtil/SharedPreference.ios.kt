package org.gabrieal.gymtracker.util.systemUtil

import platform.Foundation.NSUserDefaults

class IOSPreferences : SharedPreferences {
    private val userDefaults = NSUserDefaults.standardUserDefaults()

    override fun putString(key: String, value: String) {
        userDefaults.setObject(value, key)
    }

    override fun getString(key: String, defaultValue: String): String {
        return userDefaults.stringForKey(key) ?: defaultValue
    }

    override fun putInt(key: String, value: Int) {
        userDefaults.setInteger(value.toLong(), key)
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        return userDefaults.integerForKey(key).toInt()
    }

    override fun putBoolean(key: String, value: Boolean) {
        userDefaults.setBool(value, key)
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return userDefaults.boolForKey(key)
    }

    override fun clear() {
        userDefaults.dictionaryRepresentation().keys.forEach { key ->
            userDefaults.removeObjectForKey(key as String)
        }
    }
}

actual fun providePreferences(): SharedPreferences {
    return IOSPreferences()
}
