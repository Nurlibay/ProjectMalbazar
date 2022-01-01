package uz.texnopos.malbazar.ui.profile.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import uz.texnopos.malbazar.core.Resource
import uz.texnopos.malbazar.data.models.RegisterUser
import uz.texnopos.malbazar.data.models.UserToken
import uz.texnopos.malbazar.data.retrofit.ApiInterface

class RegisterViewModel(private val apiInterface: ApiInterface) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private var _registerUser: MutableLiveData<Resource<UserToken>> = MutableLiveData()
    val registerUser: MutableLiveData<Resource<UserToken>>
        get() = _registerUser

    fun registerUser(phone: String, name: String, password: String) {
        _registerUser.value = Resource.loading()
        compositeDisposable.add(
            apiInterface.registerUser(user = RegisterUser(phone, name, password))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _registerUser.value = Resource.success(it.data)
                    },
                    { _registerUser.value = Resource.error(it.message) }
                )
        )
    }
}