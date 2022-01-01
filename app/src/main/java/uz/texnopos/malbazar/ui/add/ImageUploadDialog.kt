package uz.texnopos.malbazar.ui.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import onClick
import uz.texnopos.malbazar.R
import uz.texnopos.malbazar.databinding.DialogImageUploadBinding

class ImageUploadDialog(private val mFragment: AddAnimalFragment) : BottomSheetDialogFragment() {

    private lateinit var binding: DialogImageUploadBinding
    private var savedViewInstance: View? = null

    init {
        show(mFragment.requireActivity().supportFragmentManager, "tag")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return if (savedInstanceState != null) {
            savedViewInstance
        } else {
            savedViewInstance =
                inflater.inflate(R.layout.dialog_image_upload, container, true)
            savedViewInstance
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DialogImageUploadBinding.bind(view)

        binding.apply {
            tvFromCamera.onClick {
                selectCamera()
                dismiss()
            }

            tvFromGallery.onClick {
                selectGallery()
                dismiss()
            }
        }
    }

    private var selectCamera: () -> Unit = {}
    private var selectGallery: () -> Unit = {}

    fun onCameraSelected(selectCamera: () -> Unit) {
        this.selectCamera = selectCamera
    }

    fun onGallerySelected(selectGallery: () -> Unit) {
        this.selectGallery = selectGallery
    }
}

