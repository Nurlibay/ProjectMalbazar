package uz.texnopos.malbazar.data.retrofit

import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import uz.texnopos.malbazar.data.models.*

interface ApiInterface {

  @POST ("api/animal")
  fun addAnimal(@Body animal:AddAnimal): Observable<GenericResponse<List<AddAnimal>>>

  @POST("api/register")
  fun registerUser(@Body user: RegisterUser): Observable<GenericResponse<UserToken>>

  @POST("api/login")
  fun loginUser(@Body user: LoginUser): Observable<GenericResponse<UserToken>>

  @GET("api/animal")
  suspend fun getLastEightAnimals(): Observable<GenericResponse<LastAnimals>>
}