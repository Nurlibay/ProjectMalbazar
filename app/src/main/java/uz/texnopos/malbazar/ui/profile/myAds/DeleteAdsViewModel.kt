package uz.texnopos.malbazar.ui.profile.myAds

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import uz.texnopos.malbazar.core.Resource
import uz.texnopos.malbazar.core.preferences.userId
import uz.texnopos.malbazar.data.model.PostSelectedAnimal
import uz.texnopos.malbazar.data.retrofit.ApiInterface

class DeleteAdsViewModel(private val api: ApiInterface) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private var _deleteAds: MutableLiveData<Resource<Any>> = MutableLiveData()
    val deleteAds: MutableLiveData<Resource<Any>> get() = _deleteAds

    fun deleteAd(animalId: Int) {
        _deleteAds.value = Resource.loading()
        compositeDisposable.add(
            api.deleteAds(animalId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _deleteAds.value = Resource.success(it.data)
                    },
                    {
                        _deleteAds.value = Resource.error(it.message)
                    }
                )
        )
    }
}