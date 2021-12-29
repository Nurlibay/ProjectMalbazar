package uz.texnopos.malbazar.ui.add

import android.net.Uri
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import uz.texnopos.malbazar.data.helper.Resource
import uz.texnopos.malbazar.data.models.AddAnimal
import uz.texnopos.malbazar.data.retrofit.ApiInterface
import java.io.File

class AddAnimalViewModel(private val api: ApiInterface) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private var _addAnimal: MutableLiveData<Resource<List<AddAnimal>>> = MutableLiveData()
    val addAnimal: MutableLiveData<Resource<List<AddAnimal>>> get() = _addAnimal

    fun addAnimal(
        title: String,
        description: String,
        cityId: Int,
        userId: Int,
        categoryId: Int,
        phone: String,
        price: String,
        img1: File,
        img2: File,
        img3: File
    ) {

        val titleRequestBody: RequestBody =
            title.toRequestBody("text/plain".toMediaTypeOrNull())

        val descriptionRequestBody: RequestBody =
            description.toRequestBody("text/plain".toMediaTypeOrNull())

        val img1RequestBody: MultipartBody.Part =
            MultipartBody.Part.createFormData(
                "file", img1.name,
                img1.asRequestBody("image/*".toMediaTypeOrNull())
            )

        _addAnimal.value = Resource.loading()
        compositeDisposable.add(
            api.addAnimal(
                titleRequestBody, descriptionRequestBody, 1000, 1, 5,
                "8480805", 10, 20, img1RequestBody
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _addAnimal.value = Resource.success(it.data)
                    }, {
                        _addAnimal.value = Resource.error(it.message)
                    }
                )
        )
    }
}