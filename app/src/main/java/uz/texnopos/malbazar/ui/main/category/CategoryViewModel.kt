package uz.texnopos.malbazar.ui.main.category

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import uz.texnopos.malbazar.core.Resource
import uz.texnopos.malbazar.data.model.Category
import uz.texnopos.malbazar.data.retrofit.ApiInterface

class CategoryViewModel(private val api: ApiInterface): ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private var _getCategory = MutableLiveData<Resource<List<Category>>>()
    val getCategory get() = _getCategory

    fun getCategory() {
        _getCategory.value = Resource.loading()
        compositeDisposable.add(
            api.getAllCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _getCategory.value = Resource.success(it.data)
                    },
                    {
                        _getCategory.value = Resource.error(it.message)
                    }
                )
        )
    }
}
