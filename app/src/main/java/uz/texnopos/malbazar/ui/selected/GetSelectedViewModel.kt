package uz.texnopos.malbazar.ui.selected

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import uz.texnopos.malbazar.core.Resource
import uz.texnopos.malbazar.core.preferences.userId
import uz.texnopos.malbazar.data.model.Animal
import uz.texnopos.malbazar.data.retrofit.ApiInterface

class GetSelectedViewModel(private val api: ApiInterface) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private var _getSelectedAnimals: MutableLiveData<Resource<List<Animal>>> = MutableLiveData()
    val getSelectedAnimals: MutableLiveData<Resource<List<Animal>>> get() = _getSelectedAnimals

    fun getSelectedAnimals() {
        _getSelectedAnimals.value = Resource.loading()
        compositeDisposable.add(
            api.getSelectedAnimals(userId!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _getSelectedAnimals.value = Resource.success(it.data)
                    },
                    {
                        _getSelectedAnimals.value = Resource.error(it.message)
                    }
                )
        )
    }
}