package uz.texnopos.malbazar

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
         val modules = listOf(dataModule, viewModelModule)
        startKoin {
            androidLogger()
            androidContext(this@App)
            androidFileProperties()
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