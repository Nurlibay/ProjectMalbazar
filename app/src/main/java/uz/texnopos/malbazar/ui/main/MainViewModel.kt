package uz.texnopos.malbazar.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import uz.texnopos.malbazar.core.Resource
import uz.texnopos.malbazar.data.model.LastAnimals
import uz.texnopos.malbazar.data.retrofit.ApiInterface

class MainViewModel(private val api: ApiInterface) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private var _lastAnimals: MutableLiveData<Resource<LastAnimals>> = MutableLiveData()
    val lastAnimals: MutableLiveData<Resource<LastAnimals>> get() = _lastAnimals

    fun lastAnimals() {
        _lastAnimals.value = Resource.loading()
        compositeDisposable.add(
            api.getLastEightAnimals()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _lastAnimals.value = Resource.success(it.data)
                    }, {
                        _lastAnimals.value = Resource.error(it.message)
                    }
                )
        )
    }
}