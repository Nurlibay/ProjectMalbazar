package uz.texnopos.malbazar.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import uz.texnopos.malbazar.data.helper.Resource
import uz.texnopos.malbazar.data.models.AnimalsCategory
import uz.texnopos.malbazar.data.retrofit.ApiInterface

class AnimalsCategortViewModel(private val api: ApiInterface) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private var _animalsCategory: MutableLiveData<Resource<AnimalsCategory>> = MutableLiveData()
    val animalsCategory: MutableLiveData<Resource<AnimalsCategory>>
        get() = _animalsCategory

    fun categoryAnimal() {
        _animalsCategory.value = Resource.loading()
        compositeDisposable.add(
            api.getAnimalsCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _animalsCategory.value = Resource.success(it)
                    }, {
                        _animalsCategory.value = Resource.error(it.message)
                    }
                )
        )
    }

}