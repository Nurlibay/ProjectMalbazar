package uz.texnopos.malbazar.ui.add

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import checkIsEmpty
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.ImagePicker.Companion.RESULT_ERROR
import com.github.dhaval2404.imagepicker.util.IntentUtils
import com.google.android.material.textfield.TextInputEditText
import com.redmadrobot.inputmask.MaskedTextChangedListener
import getOnlyDigits
import hideProgress
import onClick
import org.koin.androidx.viewmodel.ext.android.viewModel
import showError
import showProgress
import textToString
import toast
import uz.texnopos.malbazar.R
import uz.texnopos.malbazar.core.mask.MaskWatcherPrice
import uz.texnopos.malbazar.core.Constants.NO_INTERNET
import uz.texnopos.malbazar.core.ResourceState
import uz.texnopos.malbazar.core.imagehelper.pickCameraImage
import uz.texnopos.malbazar.core.imagehelper.pickGalleryImage
import uz.texnopos.malbazar.core.imagehelper.setLocalImage
import uz.texnopos.malbazar.core.mask.MaskWatcherPhone
import uz.texnopos.malbazar.core.preferences.userId
import uz.texnopos.malbazar.data.models.AddAnimal
import uz.texnopos.malbazar.data.models.Category
import uz.texnopos.malbazar.data.models.City
import uz.texnopos.malbazar.databinding.FragmentAddAnimalBinding
import java.io.File

class AddAnimalFragment: Fragment(R.layout.fragment_add_animal) {

    private lateinit var binding: FragmentAddAnimalBinding
    private val viewModel: AddAnimalViewModel by viewModel()
    private val allCategory = MutableLiveData<List<Category>>()
    private val allCity = MutableLiveData<List<City>>()
    private var img1ImageUri: Uri? = null
    private var img2ImageUri: Uri? = null
    private var img3ImageUri: Uri? = null
    private var categoryId: Int = 0
    private var cityId: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddAnimalBinding.bind(view)
        setUpObserver()
        setUpObserverGetCategory()
        setUpObserverGetCity()
        viewModel.getCategory()
        viewModel.getCity()
        binding.apply {

            etPrice.addTextChangedListener(MaskWatcherPrice(etPrice))
            etPhone.addTextChangedListener(MaskWatcherPhone.phoneNumber())
            etPhone.addMaskAndHint("([00]) [000]-[00]-[00]")

            allCategory.observe(requireActivity(), { categoryList ->
                val categoryNameList = categoryList.map { category ->  category.name }.toTypedArray()
                val categoryAdapter = ArrayAdapter(requireContext(), R.layout.item_drop_down, categoryNameList)
                etCategory.setAdapter(categoryAdapter)
                etCategory.setOnItemClickListener { _, _, position, _ ->
                    val categoryName = etCategory.textToString()
                    if(categoryList[position].name == categoryName) categoryId = categoryList[position].id
                }
            })

            allCity.observe(requireActivity(), { cityList ->
                val cityNameList = cityList.map { category ->  category.name }.toTypedArray()
                val categoryAdapter = ArrayAdapter(requireContext(), R.layout.item_drop_down, cityNameList)
                etCity.setAdapter(categoryAdapter)
                etCity.setOnItemClickListener { _, _, position, _ ->
                    val cityName = etCity.textToString()
                    if(cityList[position].name == cityName) cityId = cityList[position].id
                }
            })

            selectImg1.apply {
                fabAddGalleryPhoto.onClick {
                    imageUploadDialog(IMG1)
                }
                imgGallery.onClick {
                    showImage(this)
                }
            }

            selectImg2.apply {
                fabAddGalleryPhoto.onClick {
                    imageUploadDialog(IMG2)
                }
                imgGallery.onClick {
                    showImage(this)
                }
            }

            selectImg3.apply {
                fabAddGalleryPhoto.onClick {
                    imageUploadDialog(IMG3)
                }
                imgGallery.onClick {
                    showImage(this)
                }
            }

            btnApplyAd.onClick {
                if (validate()) {
                    val newAnimal = AddAnimal(
                        title = etTitle.textToString(),
                        description = etDescription.textToString(),
                        cityId = cityId,
                        categoryId = categoryId,
                        userId = userId!!,
                        phone = ("+998${etPhone.textToString().getOnlyDigits()}"),
                        price = etPrice.textToString().getOnlyDigits(),
                        img1 = if(img1ImageUri == null) null else File(img1ImageUri?.path!!),
                        img2 = if(img2ImageUri == null) null else File(img2ImageUri?.path!!),
                        img3 = if(img3ImageUri == null) null else File(img3ImageUri?.path!!),
                    )
                    viewModel.addAnimal(newAnimal)
                }
            }
        }
    }

    private fun TextInputEditText.addMaskAndHint(mask: String) {
        val listener = MaskedTextChangedListener.installOn(
            this,
            mask
        )
        this.hint = listener.placeholder()
    }

    private fun setUpObserver() {
        viewModel.addAnimal.observe(requireActivity(), {
            when (it.status) {
                ResourceState.LOADING -> showProgress()
                ResourceState.SUCCESS -> {
                    hideProgress()
                    toast(it.data!!.message)
                }
                ResourceState.ERROR -> {
                    hideProgress()
                    toast(it.message!!)
                }
                ResourceState.NETWORK_ERROR -> {
                    hideProgress()
                    toast(NO_INTERNET)
                }
            }
        })
    }

    private fun setUpObserverGetCategory() {
        viewModel.getCategory.observe(requireActivity(), {
            when (it.status) {
                ResourceState.LOADING -> showProgress()
                ResourceState.SUCCESS -> {
                    if(it.data != null) allCategory.postValue(it.data!!)
                    hideProgress()
                }
                ResourceState.ERROR -> {
                    hideProgress()
                    toast(it.message!!)
                }
                ResourceState.NETWORK_ERROR -> {
                    hideProgress()
                    toast(NO_INTERNET)
                }
            }
        })
    }

    private fun setUpObserverGetCity() {
        viewModel.getCity.observe(requireActivity(), {
            when (it.status) {
                ResourceState.LOADING -> showProgress()
                ResourceState.SUCCESS -> {
                    if(it.data != null) allCity.postValue(it.data!!)
                    hideProgress()
                }
                ResourceState.ERROR -> {
                    hideProgress()
                    toast(it.message!!)
                }
                ResourceState.NETWORK_ERROR -> {
                    hideProgress()
                    toast(NO_INTERNET)
                }
            }
        })
    }

    private fun FragmentAddAnimalBinding.validate(): Boolean {
        return when {
            etTitle.checkIsEmpty() -> tilTitle.showError(getString(R.string.required))
            etDescription.checkIsEmpty() -> tilDescription.showError(getString(R.string.required))
            etCity.checkIsEmpty() -> tilCity.showError(getString(R.string.required))
            etCategory.checkIsEmpty() -> tilCategory.showError(getString(R.string.required))
            etPhone.checkIsEmpty() -> tilPhone.showError(getString(R.string.required))
            etPrice.checkIsEmpty() -> tilPrice.showError(getString(R.string.required))
            else -> true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                val uri: Uri? = data?.data!!
                when (requestCode) {
                    IMG1_GALLERY_REQ_CODE, IMG1_CAMERA_REQ_CODE -> {
                        this.img1ImageUri = uri
                        binding.selectImg1.imgGallery.setLocalImage(uri)
                    }
                    IMG2_GALLERY_REQ_CODE, IMG2_CAMERA_REQ_CODE -> {
                        this.img2ImageUri = uri
                        binding.selectImg2.imgGallery.setLocalImage(uri)
                    }
                    IMG3_GALLERY_REQ_CODE, IMG3_CAMERA_REQ_CODE -> {
                        this.img3ImageUri = uri
                        binding.selectImg3.imgGallery.setLocalImage(uri)
                    }
                }
            }
            RESULT_ERROR -> {
                toast(ImagePicker.getError(data))
            }
        }
    }

    private fun showImage(view: View) {
        val uri = when (view) {
            binding.selectImg1.imgGallery -> this.img1ImageUri
            binding.selectImg2.imgGallery -> this.img2ImageUri
            binding.selectImg3.imgGallery -> this.img3ImageUri
            else -> null
        }
        uri?.let {
            startActivity(IntentUtils.getUriViewIntent(requireContext(), uri))
        }
    }

    private fun imageUploadDialog(type: Int) {
        val dialog = ImageUploadDialog(this)
        dialog.onGallerySelected {
            when (type) {
                IMG1 -> pickGalleryImage(IMG1_GALLERY_REQ_CODE)
                IMG2 -> pickGalleryImage(IMG2_GALLERY_REQ_CODE)
                IMG3 -> pickGalleryImage(IMG3_GALLERY_REQ_CODE)
            }
        }
        dialog.onCameraSelected {
            when (type) {
                IMG1 -> pickCameraImage(IMG1_CAMERA_REQ_CODE)
                IMG2 -> pickCameraImage(IMG2_CAMERA_REQ_CODE)
                IMG3 -> pickCameraImage(IMG3_CAMERA_REQ_CODE)
            }
        }
    }

    companion object {
        private const val IMG1_GALLERY_REQ_CODE = 102
        private const val IMG1_CAMERA_REQ_CODE = 103
        private const val IMG2_GALLERY_REQ_CODE = 104
        private const val IMG2_CAMERA_REQ_CODE = 105
        private const val IMG3_GALLERY_REQ_CODE = 106
        private const val IMG3_CAMERA_REQ_CODE = 107

        private const val IMG1 = 0
        private const val IMG2 = 1
        private const val IMG3 = 2
    }
}