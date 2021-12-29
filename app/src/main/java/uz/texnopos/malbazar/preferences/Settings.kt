package uz.texnopos.malbazar.preferences

import uz.texnopos.malbazar.App

fun getSharedPreferences(): SharedPrefUtils {
    return if (App.sharedPrefUtils == null) {
        App.sharedPrefUtils = SharedPrefUtils()
        App.sharedPrefUtils!!
    } else App.sharedPrefUtils!!
}

var token: String?
    set(value) = getSharedPreferences().setStringValue(Constants.TOKEN, value!!)
    get() = getSharedPreferences().getStringValue(Constants.TOKEN)

var userId: Int?
    set(value) = getSharedPreferences().setIntValue(Constants.USERID, value!!)
    get() = getSharedPreferences().getIntValue(Constants.USERID)

fun isSignedIn(): Boolean = !token.isNullOrEmpty()