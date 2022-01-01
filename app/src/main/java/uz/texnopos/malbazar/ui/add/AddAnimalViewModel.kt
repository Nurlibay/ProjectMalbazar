//package uz.texnopos.malbazar.ui.add
//
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
//import io.reactivex.rxjava3.disposables.CompositeDisposable
//import io.reactivex.rxjava3.schedulers.Schedulers
//import okhttp3.RequestBody
//import uz.texnopos.malbazar.data.helper.Resource
//import uz.texnopos.malbazar.data.models.AddAnimal
//import uz.texnopos.malbazar.data.retrofit.ApiInterface
//
//class AddAnimalViewModel(private val api: ApiInterface): ViewModel() {
//    private val compositeDisposable = CompositeDisposable()
//    private var _addAnimal: MutableLiveData<Resource<List<AddAnimal>>> = MutableLiveData()
//    val addAnimal: MutableLiveData<Resource<List<AddAnimal>>>
//        get() = _addAnimal
//
//    fun addAnimal(
//        title: String,
//        description: String,
//        cityId: Int,
//        userId: Int,
//        categoryId: Int,
//        phone: String,
//        price: String,
//        img1: String,
//        img2: String,
//        img3: String
//    ) {
//        _addAnimal.value = Resource.loading()
//
//        val partMap = HashMap<String, RequestBody>()
//            partMap["first_name"] = firstName.toRequestBody()
//            partMap["last_name"] = lastName.toRequestBody()
//            partMap["middle_name"] = middleName.toRequestBody()
//            partMap["phone1"] = phone1.toRequestBody()
//            partMap["phone2"] = phone2.toRequestBody()
//            partMap["pasport_serial"] = passportSerial.toRequestBody()
//            partMap["pasport_number"] = passportNumber.toRequestBody()
//
//        val file1 = postClient.passportPhoto.toMultiPart("pasport_photo")
//        val file2 = postClient.letter.toMultiPart("latter")
//        val response = api.clientRegister(partMap, file1, file2)
//        if (response.isSuccessful) {
//            if (response.body()!!.successful) {
//                _register.value = Resource.success(response.body()!!)
//            } else _register.value = Resource.error(response.body()!!.message)
//        } else {
//            _register.value = Resource.error(response.message())
//        }
//
//        compositeDisposable.add(
//            api.addAnimal(
//                animal = AddAnimal(
//                    title,
//                    description,
//                    cityId,
//                    categoryId,
//                    userId,
//                    phone,
//                    price,
//                    img1,
//                    img2,
//                    img3
//                )
//             )
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                    {
//                        _addAnimal.value = Resource.success(it.data)
//                    }, {
//                        _addAnimal.value = Resource.error(it.message)
//                    }
//                )
//
//        )
//    }
//
//}