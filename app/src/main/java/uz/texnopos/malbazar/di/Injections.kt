package uz.texnopos.malbazar.di

import com.google.gson.GsonBuilder
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import uz.texnopos.malbazar.data.retrofit.ApiInterface
import uz.texnopos.malbazar.ui.main.MainViewModel
import uz.texnopos.malbazar.ui.profile.login.LoginViewModel
import uz.texnopos.malbazar.ui.profile.register.RegisterViewModel

const val baseUrl = "http://test.malbazar.uz"

val dataModule = module {
    single {
        GsonBuilder()
            .setLenient()
            .create()
    }
    single {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(get()))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }
    single {
        get<Retrofit>().create(ApiInterface::class.java)
    }
}
val viewModelModule = module {
    viewModel { RegisterViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { MainViewModel(get()) }
}