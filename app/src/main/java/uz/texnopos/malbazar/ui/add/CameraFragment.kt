//package uz.texnopos.oneup.ui.camera
//
//import android.Manifest
//import android.app.Activity.RESULT_OK
//import android.content.ContentValues
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.net.Uri
//import android.os.Build
//import android.provider.MediaStore
//import android.provider.Settings
//import android.widget.Toast
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.appcompat.app.AlertDialog
//import androidx.core.content.ContextCompat
//import androidx.core.content.FileProvider
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.lifecycleScope
//import id.zelory.compressor.Compressor
//import kotlinx.coroutines.launch
//import toast
//import uz.texnopos.malbazar.R
//import uz.texnopos.oneup.R
//import uz.texnopos.oneup.core.SingleLiveEvent
//import uz.texnopos.oneup.data.local.model.checking.ResultFileDbModel
//import java.io.*
//import java.util.*
//
//abstract class CameraFragment(viewResId: Int) : Fragment(viewResId) {
//    private lateinit var imageUri: Uri
//    private var compressedImage: File? = null
//    private var actualImageFile: File? = null
//    private var mutableImageModel: SingleLiveEvent<ResultFileDbModel> = SingleLiveEvent()
//    val imageModel: LiveData<ResultFileDbModel>
//        get() = mutableImageModel
//    private var checkingResultUuid: String = ""
//
//    private val requestMultiplePermissions =
//        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
//            var isGranted = true
//            permissions.entries.forEach {
//                if (!it.value) isGranted = false
//            }
//            if (isGranted) {
//                takePicture()
//            } else {
//                showDialog()
//            }
//        }
//
//    private val cameraResultLauncher = registerForActivityResult(
//        ActivityResultContracts.StartActivityForResult()
//    ) {
//        if (it.resultCode == RESULT_OK) {
//            compressedImage()
//        } else {
//            toast("Camera closed")
//        }
//    }
//
//    private fun compressedImage() {
//        actualImageFile?.let { actImg ->
//            lifecycleScope.launch {
//                compressedImage = Compressor.compress(requireContext(), actImg)
//                setCompressedImage()
//                actImg.delete()
//            }
//        }
//    }
//
//    private fun setCompressedImage() {
//        compressedImage?.let {
//            val result = ResultFileDbModel(
//                uuid = UUID.randomUUID().toString(),
//                checkingResultUuid = checkingResultUuid,
//                orginalImage = it.absolutePath,
//                smallImage = ""
//            )
//            mutableImageModel.value = result
//        }
//    }
//
//    fun tryToTakePicture(checkingResultUuid: String) {
//        this.checkingResultUuid = checkingResultUuid
//        checkForPermissions()
//    }
//
//    private fun takePicture() {
//        val values = ContentValues()
//        imageUri = requireContext().contentResolver.insert(
//            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
//        )!!
//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
//            takePictureIntent.resolveActivity(requireContext().packageManager).also {
//                actualImageFile = try {
//                    File(
//                        requireContext().getExternalFilesDir("images"),
//                        "image_" + "${System.currentTimeMillis()}.jpg"
//                    )
//                } catch (ex: IOException) {
//                    ex.printStackTrace()
//                    return
//                }
//                actualImageFile.also {
//                    val photoUri: Uri = FileProvider.getUriForFile(
//                        requireContext(), "${requireActivity().packageName}.fileprovider",
//                        it!!
//                    )
//                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
//                    cameraResultLauncher.launch(takePictureIntent)
//                }
//            }
//        }
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
//    }
//
//    private fun showDialog() {
//        val builder = AlertDialog.Builder(requireContext())
//        builder.apply {
//            setMessage(getString(R.string.permission_is_required))
//            setTitle(getString(R.string.permission_required_title))
//            setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
//                dialog.dismiss()
//            }
//            setPositiveButton(getString(R.string.go_to_settings)) { _, _ ->
//                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                val uri: Uri = Uri.fromParts("package", activity?.packageName, null)
//                intent.data = uri
//                startActivity(intent)
//            }
//        }.create().show()
//    }
//
//    private fun checkForPermissions() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(
//                    requireContext(),
//                    Manifest.permission.CAMERA
//                ) != PackageManager.PERMISSION_GRANTED
//                || ContextCompat.checkSelfPermission(
//                    requireContext(),
//                    Manifest.permission.READ_EXTERNAL_STORAGE
//                ) != PackageManager.PERMISSION_GRANTED
//                || ContextCompat.checkSelfPermission(
//                    requireContext(),
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                requestMultiplePermissions.launch(
//                    arrayOf(
//                        Manifest.permission.CAMERA,
//                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE
//                    )
//                )
//            } else {
//                takePicture()
//            }
//        }
//    }
//}