package org.gabrieal.gymtracker.util.systemUtil

interface SharedPreferences {
    fun putString(key: String, value: String)
    fun getString(key: String, defaultValue: String = ""): String
    fun putInt(key: String, value: Int)
    fun getInt(key: String, defaultValue: Int = 0): Int
    fun putBoolean(key: String, value: Boolean)
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean
    fun clear()
}

expect fun providePreferences(context: Any?): SharedPreferences

//getCurrentContext().let {
//    println(providePreferences(it).getString("username"))
//    providePreferences(it).putString("username", "Yellow")
//    println(providePreferences(it).getString("username"))
//    ShowToast(providePreferences(it).getString("username"))
//}