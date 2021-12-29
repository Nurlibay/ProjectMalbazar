package uz.texnopos.malbazar.ui.main.info

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import uz.texnopos.malbazar.data.helper.Resource
import uz.texnopos.malbazar.data.models.Recommendations
import uz.texnopos.malbazar.data.retrofit.ApiInterface

class RecommendationViewModel(private val api: ApiInterface) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private var _recomendAnimals: MutableLiveData<Resource<Recommendations>> = MutableLiveData()
    val recommendAnimals: MutableLiveData<Resource<Recommendations>>
        get() = _recomendAnimals

    fun recommendAnimals(animalId:Int) {
        _recomendAnimals.value = Resource.loading()
        compositeDisposable.add(
            api.getRecommendations(animalId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _recomendAnimals.value = Resource.success(it.data)
                    }, {
                        _recomendAnimals.value = Resource.error(it.message)
                    }
                )
        )
    }

}