package uz.texnopos.malbazar.core.preferences

import uz.texnopos.malbazar.App
import uz.texnopos.malbazar.core.Constants.TOKEN
import uz.texnopos.malbazar.core.Constants.USERID

fun getSharedPreferences(): SharedPrefUtils {
    return if (App.sharedPrefUtils == null) {
        App.sharedPrefUtils = SharedPrefUtils()
        App.sharedPrefUtils!!
    } else App.sharedPrefUtils!!
}

var token: String?
    set(value) = getSharedPreferences().setValue(TOKEN, value)
    get() = getSharedPreferences().getStringValue(TOKEN)

var userId: Int?
    set(value) = getSharedPreferences().setValue(USERID.toString(), value!!)
    get() = getSharedPreferences().getIntValue(USERID)

fun isSignedIn(): Boolean = !token.isNullOrEmpty()
