package uz.texnopos.malbazar.ui.profile.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import uz.texnopos.malbazar.core.Resource
import uz.texnopos.malbazar.data.model.LoginUser
import uz.texnopos.malbazar.data.model.UserToken
import uz.texnopos.malbazar.data.retrofit.ApiInterface

class LoginViewModel(private val api: ApiInterface) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private var _login: MutableLiveData<Resource<UserToken>> = MutableLiveData()
    val login: MutableLiveData<Resource<UserToken>> get() = _login

    fun loginUser(phone: String, password: String) {
        _login.value = Resource.loading()
        compositeDisposable.add(
            api.loginUser(user = LoginUser(phone = phone, password))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _login.value = Resource.success(it.data)
                    },
                    {
                        _login.value = Resource.error(it.message)
                    }
                )
        )
    }
}