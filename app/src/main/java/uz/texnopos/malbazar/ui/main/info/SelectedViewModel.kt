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
    val addSelectedAnimal: MutableLiveData<Resource<Any>> get() = _selectedAnimal

    fun addSelectedAnimal(animalId:Int) {
        _selectedAnimal.value = Resource.loading()
        compositeDisposable.add(
            api.addSelectedAnimal(selectedAnimal = PostSelectedAnimal(userId!!,animalId))
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
    fun deleteSelectedAnimal(animalId:Int) {
        _selectedAnimal.value = Resource.loading()
        compositeDisposable.add(
            api.addSelectedAnimal(selectedAnimal = PostSelectedAnimal(userId!!,animalId))
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
}