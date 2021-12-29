package uz.texnopos.malbazar.ui.myAds

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import uz.texnopos.malbazar.data.helper.Resource
import uz.texnopos.malbazar.data.models.Animal
import uz.texnopos.malbazar.data.models.AnimalsCategory
import uz.texnopos.malbazar.data.models.LastAnimals
import uz.texnopos.malbazar.data.retrofit.ApiInterface

class MyAdsViewModel(private val api: ApiInterface) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private var _myAds: MutableLiveData<Resource<List<Animal>>> = MutableLiveData()
    val myAds: MutableLiveData<Resource<List<Animal>>>
        get() = _myAds

    fun userAds(userId:Int) {
        _myAds.value = Resource.loading()
        compositeDisposable.add(
            api.getUserAds(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _myAds.value = Resource.success(it.data)
                    }, {
                        _myAds.value = Resource.error(it.message)
                    }
                )
        )
    }

}