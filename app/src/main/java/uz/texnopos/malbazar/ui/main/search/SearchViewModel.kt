package uz.texnopos.malbazar.ui.main.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import uz.texnopos.malbazar.core.Resource
import uz.texnopos.malbazar.data.models.SearchAnimal
import uz.texnopos.malbazar.data.models.SearchResult
import uz.texnopos.malbazar.data.retrofit.ApiInterface

class SearchViewModel(private val api: ApiInterface) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private var _search: MutableLiveData<Resource<SearchResult>> = MutableLiveData()
    val search: MutableLiveData<Resource<SearchResult>>
        get() = _search

    fun searchAnimal(query: String) {
        _search.value = Resource.loading()
        compositeDisposable.add(
            api.searchAnimal(query= SearchAnimal(query,"all","all"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _search.value = Resource.success(it.data)
                    }, {
                        _search.value = Resource.error(it.message)
                    }
                )
        )
    }

}