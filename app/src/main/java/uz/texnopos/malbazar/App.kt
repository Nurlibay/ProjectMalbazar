package uz.texnopos.malbazar

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import uz.texnopos.malbazar.core.preferences.SharedPrefUtils
import uz.texnopos.malbazar.di.dataModule
import uz.texnopos.malbazar.di.viewModelModule

class App : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        appInstance = this
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        val modules = listOf(dataModule, viewModelModule)
        startKoin {//use AndroidLogger as Koin Logger - default Level
            androidLogger()
            //use the Android context given there
            androidContext(this@App)
            //load properties from assets/koin.properties file
            androidFileProperties()
            //module list
            koin.loadModules(modules)
        }
    }

    companion object {
        private lateinit var appInstance: App
        var sharedPrefUtils: SharedPrefUtils? = null
        fun getAppInstance(): App {
            return appInstance
        }
    }
}