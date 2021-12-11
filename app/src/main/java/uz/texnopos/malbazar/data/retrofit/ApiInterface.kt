package uz.texnopos.malbazar.data.retrofit

import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import uz.texnopos.malbazar.data.models.*

interface ApiInterface {
//
//    @POST("api/animal")
//    fun addAnimal(@Body animal: AddAnimal): Observable<GenericResponse<List<AddAnimal>>>

    @POST("api/register")
    fun registerUser(@Body user: RegisterUser): Observable<GenericResponse<UserToken>>

    @POST("api/login")
    fun loginUser(@Body user: LoginUser): Observable<GenericResponse<UserToken>>

    @GET("api/animal")
    fun getLastEightAnimals(): Observable<GenericResponse<LastAnimals>>

    @GET("api/myads/{userId}")
    fun getUserAds(@Path("userId") id: Int): Observable<GenericResponse<List<Animal>>>

    @GET("api/animal/{id}")
    fun getRecommendations(@Path("id") id: Int): Observable<GenericResponse<Recommendations>>

    @Multipart
    @POST("api/animal")
    fun addAnimal(
        @Body animal: AddAnimal,
        @Part imageFile: MultipartBody.Part
    ): Observable<GenericResponse<List<AddAnimal>>>

}