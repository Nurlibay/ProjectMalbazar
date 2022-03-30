package uz.texnopos.malbazar.ui.main.info.comments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import uz.texnopos.malbazar.core.Resource
import uz.texnopos.malbazar.data.model.AddComment
import uz.texnopos.malbazar.data.model.GetComments
import uz.texnopos.malbazar.data.retrofit.ApiInterface

class CommentsViewModel(private val api: ApiInterface) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private var _postComments: MutableLiveData<Resource<Any>> = MutableLiveData()
    val postComments: MutableLiveData<Resource<Any>> get() = _postComments

    private var _getComments: MutableLiveData<Resource<List<GetComments>>> = MutableLiveData()
    val getComment: MutableLiveData<Resource<List<GetComments>>> get() = _getComments

    fun addComments(comment: AddComment) {
        _postComments.value = Resource.loading()
        compositeDisposable.add(
            api.addComment(comment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _postComments.value = Resource.success(it.data)
                    },
                    {
                        _postComments.value = Resource.error(it.message)
                    }
                )
        )
    }

    fun getComments(animalId: Int) {
        _getComments.value = Resource.loading()
        compositeDisposable.add(
            api.getComments(animalId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _getComments.value = Resource.success(it.data.comments)
                    },
                    {
                        _getComments.value = Resource.error(it.message)
                    }
                )
        )
    }
}