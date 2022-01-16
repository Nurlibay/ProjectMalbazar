package uz.texnopos.malbazar.ui.profile.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import uz.texnopos.malbazar.core.Resource
import uz.texnopos.malbazar.data.model.RegisterUser
import uz.texnopos.malbazar.data.model.UserToken
import uz.texnopos.malbazar.data.retrofit.ApiInterface

class RegisterViewModel(private val api: ApiInterface) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private var _registerUser: MutableLiveData<Resource<UserToken>> = MutableLiveData()
    val registerUser: MutableLiveData<Resource<UserToken>>
        get() = _registerUser

    fun registerUser(phone: String, name: String, password: String, from: String) {
        _registerUser.value = Resource.loading()
        compositeDisposable.add(
            api.registerUser(user = RegisterUser(phone, name, password, from = from))
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