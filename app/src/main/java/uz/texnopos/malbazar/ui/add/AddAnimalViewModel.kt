package uz.texnopos.malbazar.ui.add

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import isNetworkAvailable
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import toMultiPart
import uz.texnopos.malbazar.core.Resource
import uz.texnopos.malbazar.data.models.*
import uz.texnopos.malbazar.data.retrofit.ApiInterface

class AddAnimalViewModel(private val api: ApiInterface): ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private var _getCategory = MutableLiveData<Resource<List<Category>>>()
    val getCategory get() = _getCategory

    private var _addAnimal = MutableLiveData<Resource<GenericResponse<Any>>>()
    val addAnimal get() = _addAnimal

    private var _getCity = MutableLiveData<Resource<List<City>>>()
    val getCity get() = _getCity

    fun addAnimal(addAnimal: AddAnimal) = viewModelScope.launch {
        _addAnimal.value = Resource.loading()
        if (isNetworkAvailable()) {
            try {
                val partMap = HashMap<String, RequestBody>()
                addAnimal.apply {
                    partMap["title"] = title.toRequestBody()
                    partMap["description"] = description.toRequestBody()
                    partMap["city_id"] = cityId.toString().toRequestBody()
                    partMap["category_id"] = categoryId.toString().toRequestBody()
                    partMap["user_id"] = userId.toString().toRequestBody()
                    partMap["phone"] = phone.toRequestBody()
                    partMap["price"] = price.toRequestBody()
                }
                val img1 = addAnimal.img1?.toMultiPart("img1")
                val img2 = addAnimal.img2?.toMultiPart("img2")
                val img3 = addAnimal.img3?.toMultiPart("img3")
                val response = api.addAnimal(partMap, img1, img2, img3)
                if (response.isSuccessful) {
                    if (response.body()!!.success) {
                        _addAnimal.value = Resource.success(response.body()!!)
                    } else _addAnimal.value = Resource.error(response.body()!!.message)
                } else {
                    _addAnimal.value = Resource.error(response.message())
                }
            } catch (e: Exception) {
                _addAnimal.value = Resource.error(e.localizedMessage)
            }
        } else _addAnimal.value = Resource.networkError()
    }

    fun getCategory() {
        _getCategory.value = Resource.loading()
        compositeDisposable.add(
            api.getAllCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _getCategory.value = Resource.success(it.data)
                    },
                    {
                        _getCategory.value = Resource.error(it.message)
                    }
                )
        )
    }

    fun getCity() {
        _getCity.value = Resource.loading()
        compositeDisposable.add(
            api.getAllCity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _getCity.value = Resource.success(it.data)
                    },
                    {
                        _getCity.value = Resource.error(it.message)
                    }
                )
        )
    }
}
