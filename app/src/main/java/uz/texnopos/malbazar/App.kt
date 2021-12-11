package uz.texnopos.malbazar

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import uz.texnopos.malbazar.di.dataModule
import uz.texnopos.malbazar.di.viewModelModule
import uz.texnopos.malbazar.preferences.SharedPrefUtils

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        appInstance=this
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