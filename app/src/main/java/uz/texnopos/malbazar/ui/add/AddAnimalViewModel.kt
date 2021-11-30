package uz.texnopos.malbazar.ui.add

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import uz.texnopos.malbazar.data.helper.Resource
import uz.texnopos.malbazar.data.models.AddAnimal
import uz.texnopos.malbazar.data.retrofit.ApiInterface

class AddAnimalViewModel(private val api: ApiInterface): ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private var _addAnimal: MutableLiveData<Resource<List<AddAnimal>>> = MutableLiveData()
    val addAnimal: MutableLiveData<Resource<List<AddAnimal>>>
        get() = _addAnimal

    fun addAnimal(
        title: String,
        description: String,
        cityId: Int,
        userId: Int,
        categoryId: Int,
        phone: String,
        price: String,
        img1: String,
        img2: String,
        img3: String
    ) {
        _addAnimal.value = Resource.loading()
        compositeDisposable.add(
            api.addAnimal(
                animal = AddAnimal(
                    title,
                    description,
                    cityId,
                    categoryId,
                    userId,
                    phone,
                    price,
                    img1,
                    img2,
                    img3
                )
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