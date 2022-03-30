package uz.texnopos.malbazar.ui.main.info

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import uz.texnopos.malbazar.core.Resource
import uz.texnopos.malbazar.core.preferences.userId
import uz.texnopos.malbazar.data.model.LoginUser
import uz.texnopos.malbazar.data.model.PostSelectedAnimal
import uz.texnopos.malbazar.data.model.UserToken
import uz.texnopos.malbazar.data.retrofit.ApiInterface

class SelectedViewModel(private val api: ApiInterface) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private var _selectedAnimal: MutableLiveData<Resource<Any>> = MutableLiveData()
    val selectedAnimal: MutableLiveData<Resource<Any>> get() = _selectedAnimal

    private var _unSelectedAnimal: MutableLiveData<Resource<Any>> = MutableLiveData()
    val unSelectedAnimal: MutableLiveData<Resource<Any>> get() = _unSelectedAnimal

    fun addSelectedAnimal(animalId: Int) {
        _selectedAnimal.value = Resource.loading()
        compositeDisposable.add(
            api.addSelectedAnimal(selectedAnimal = PostSelectedAnimal(userId!!, animalId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _selectedAnimal.value = Resource.success(it.data)
                    },
                    {
                        _selectedAnimal.value = Resource.error(it.message)
                    }
                )
        )
    }

    fun deleteSelectedAnimal(animalId: Int) {
        _unSelectedAnimal.value = Resource.loading()
        compositeDisposable.add(
            api.deleteSelectedAnimal(animalId = animalId, userId!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _unSelectedAnimal.value = Resource.success(it.data)
                    },
                    {
                        _unSelectedAnimal.value = Resource.error(it.message)
                    }
                )
        )
    }
}