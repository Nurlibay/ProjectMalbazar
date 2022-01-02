package uz.texnopos.malbazar.data.retrofit

import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import uz.texnopos.malbazar.data.models.*

interface ApiInterface {

    @POST("api/register")
    fun registerUser(@Body user: RegisterUser): Observable<GenericResponse<UserToken>>

    @POST("api/login")
    fun loginUser(@Body user: LoginUser): Observable<GenericResponse<UserToken>>

    @GET("api/animal")
    fun getLastEightAnimals(): Observable<GenericResponse<LastAnimals>>

    @GET("api/myads/{userId}")
    fun getUserAds(@Path("userId") id: Int): Observable<GenericResponse<List<Animal>>>

    @GET("api/animal/{id}")
    fun getRecommendations(@Path("id") id: Int): Observable<GenericResponse<AnimalInfo>>

    @GET("api/search")
    fun searchAnimal(
        @Query("query") query: String,
        @Query("city_id") city_id: String,
        @Query("category_id") category_id: String
    ): Observable<GenericResponse<SearchResult>>

    @GET("api/category")
    fun getAllCategory(): Observable<GenericResponse<List<Category>>>

    @GET("api/city")
    fun getAllCity(): Observable<GenericResponse<List<City>>>

    @Multipart
    @POST("api/animal")
    suspend fun addAnimal(
        @PartMap partMap: HashMap<String, RequestBody>,
        @Part img1: MultipartBody.Part?,
        @Part img2: MultipartBody.Part?,
        @Part img3: MultipartBody.Part?
    ): Response<GenericResponse<Any>>
}