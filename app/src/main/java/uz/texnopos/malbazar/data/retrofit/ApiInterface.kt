package uz.texnopos.malbazar.data.retrofit

import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import uz.texnopos.malbazar.data.models.*

interface ApiInterface {

  @POST("api/register")
  fun registerUser(@Body user: RegisterUser): Observable<GenericResponse<UserToken>>

  @POST("api/login")
  fun loginUser(@Body user: LoginUser): Observable<GenericResponse<UserToken>>

  @GET("api/category")
  fun getAnimalsCategory(): Observable<AnimalsCategory>
}