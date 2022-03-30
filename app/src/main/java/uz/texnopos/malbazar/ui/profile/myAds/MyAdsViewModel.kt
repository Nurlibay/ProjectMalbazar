package uz.texnopos.malbazar.ui.profile.myAds

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import uz.texnopos.malbazar.core.Resource
import uz.texnopos.malbazar.data.model.MyAds
import uz.texnopos.malbazar.data.retrofit.ApiInterface

class MyAdsViewModel(private val api: ApiInterface) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private var _myAds: MutableLiveData<Resource<MyAds>> = MutableLiveData()
    val myAds: MutableLiveData<Resource<MyAds>> get() = _myAds

    fun userAds(userId: Int) {
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