package uz.texnopos.malbazar.di

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import uz.texnopos.malbazar.data.retrofit.ApiInterface
import uz.texnopos.malbazar.preferences.token
import uz.texnopos.malbazar.ui.add.AddAnimalViewModel
import uz.texnopos.malbazar.ui.main.MainViewModel
import uz.texnopos.malbazar.ui.main.info.RecommendationViewModel
import uz.texnopos.malbazar.ui.main.search.SearchViewModel
import uz.texnopos.malbazar.ui.myAds.MyAdsViewModel
import uz.texnopos.malbazar.ui.profile.login.LoginViewModel
import uz.texnopos.malbazar.ui.profile.register.RegisterViewModel
import java.util.concurrent.TimeUnit

const val baseUrl = "http://test.malbazar.uz"
const val appTimeOut = 24L

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
            .client(get())
            .build()
    }
    single {
        get<Retrofit>().create(ApiInterface::class.java)
    }
    single {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        //OkHttpClient start
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                var request = chain.request()
                request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
                request.newBuilder().header(
                    "Cache-Control",
                    "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
                ).build()
                chain.proceed(request)
            }

            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                return@addInterceptor chain.proceed(request)
            }
            .addInterceptor(loggingInterceptor)
            .connectTimeout(appTimeOut, TimeUnit.SECONDS)
            .readTimeout(appTimeOut, TimeUnit.SECONDS)
            .writeTimeout(appTimeOut, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
        //OkHttpClient end
    }
}
val viewModelModule = module {
    viewModel { RegisterViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { AddAnimalViewModel(get()) }
    viewModel { RecommendationViewModel(get()) }
    viewModel { MyAdsViewModel(get()) }
    viewModel { SearchViewModel(get()) }
}