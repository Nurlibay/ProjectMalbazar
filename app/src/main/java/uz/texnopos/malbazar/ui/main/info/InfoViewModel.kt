package uz.texnopos.malbazar.ui.main.info

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import uz.texnopos.malbazar.core.Resource
import uz.texnopos.malbazar.data.models.AnimalInfo
import uz.texnopos.malbazar.data.models.LastAnimals
import uz.texnopos.malbazar.data.retrofit.ApiInterface

class InfoViewModel(private val api: ApiInterface) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private var _getInfoAboutAnimal: MutableLiveData<Resource<AnimalInfo>> = MutableLiveData()
    val getInfoAboutAnimal: MutableLiveData<Resource<AnimalInfo>>
        get() = _getInfoAboutAnimal

    fun getAnimalInfo(id:Int) {
        _getInfoAboutAnimal.value = Resource.loading()
        compositeDisposable.add(
            api.getRecommendations(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _getInfoAboutAnimal.value = Resource.success(it.data)
                    }, {
                        _getInfoAboutAnimal.value = Resource.error(it.message)
                    }
                )
        )
    }

}