package uz.texnopos.malbazar.preferences

import Constants.mySharedPreferences
import android.content.Context
import android.content.SharedPreferences
import uz.texnopos.malbazar.App.Companion.getAppInstance

class SharedPrefUtils {
    private val mSharedPreferences: SharedPreferences = getAppInstance()
        .getSharedPreferences(mySharedPreferences, Context.MODE_PRIVATE)
    private var mSharedPreferencesEditor: SharedPreferences.Editor = mSharedPreferences.edit()

    init {
        mSharedPreferencesEditor.apply()
    }

    fun setStringValue(key: String, value: String) {
        mSharedPreferencesEditor.putString(key, value).apply()
    }

    fun setIntValue(key: Int, value: Int) {
        mSharedPreferencesEditor.putInt(key.toString(), value).apply()
    }

    fun getStringValue(key: String, defaultValue: String = ""): String? {
        return mSharedPreferences.getString(key, defaultValue)
    }

    fun getIntValue(key: Int, defaultValue: Int = 0): Int {
        return mSharedPreferences.getInt(key.toString(), defaultValue)
    }
}