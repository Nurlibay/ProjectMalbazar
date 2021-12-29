package uz.texnopos.malbazar.ui.add

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.malbazar.R
import uz.texnopos.malbazar.data.helper.ResourceState
import uz.texnopos.malbazar.databinding.FragmentAddBinding
import java.io.File

class AddFragment : Fragment(R.layout.fragment_add) {

    private lateinit var b: FragmentAddBinding
    private val viewModel: AddAnimalViewModel by viewModel()
    private val args: AddFragmentArgs by navArgs()
    private var cityId: Int = 0
    private val getRootDirectoryPath =
        ContextCompat.getExternalFilesDirs(requireContext(), null)[0].absolutePath
    private var imageUri: Uri? = null
    private var mImg1: Uri? = null
    private var mImg2: Uri? = null
    private var mImg3: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        b = FragmentAddBinding.bind(view)

        imageUri = FileProvider.getUriForFile(
            requireContext(),
            requireActivity().packageName + ".provider", file
        )

        when (args.id) {
            0 -> b.menu.text = "Bólimler"
            1 -> b.menu.text = "Qaramal"
            2 -> b.menu.text = "Tuye"
            3 -> b.menu.text = "Jılqı"
            4 -> b.menu.text = "Eshki"
            5 -> b.menu.text = "Qoy"
            6 -> b.menu.text = "Mal azıǵı"
            7 -> b.menu.text = "Tawıq"
            8 -> b.menu.text = "Mal tasıw hizmeti"
            9 -> b.menu.text = "Teri(satıw hám satıp alıw)"
        }
        b.ivSecond.isEnabled = false
        b.ivThird.isEnabled = false

        b.menu.setOnClickListener {
            findNavController().navigate(R.id.action_addFragment_to_categoryFragment)
        }
        b.tvCity.setOnClickListener {
            showPopup(it)
        }
        b.btnSet.setOnClickListener {
            when {
                b.menu.text.isEmpty() -> {
                    Toast.makeText(requireContext(), "Bólimdi saylań", Toast.LENGTH_SHORT).show()
                }
                b.etShortInfo.text.isEmpty() -> {
                    Toast.makeText(
                        requireContext(),
                        "Qısqasha malıwmat kiritıń",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                b.etInfo.text.isEmpty() -> {
                    Toast.makeText(
                        requireContext(),
                        "Tolıǵraq malıwmat kiritıń",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                b.etPrice.text.isEmpty() -> {
                    b.etPrice.error = ""
                }
                b.etPhone.text.isEmpty() -> {
                    Toast.makeText(
                        requireContext(),
                        "Telefon nomerıńızdı kiritıń",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                b.tvCity.text.isEmpty() -> {
                    b.tvCity.error = ""
                }
                else -> {
                    setData(
                        title = b.etShortInfo.text.toString(),
                        description = b.etInfo.text.toString(),
                        cityId = cityId,
                        userId = 1,   // SAZLAW KEREK
                        categoryId = 5,   // SAZLAW KEREK
                        phone = b.etPhone.text.toString(),
                        price = b.etPrice.text.toString(),
                        img1, img2, img3
                    )
                }
            }
        }

        b.ivFirst.setOnClickListener {
            b.ivSecond.isEnabled = true
            b.ivFirst.isEnabled = false
        }
        b.ivSecond.setOnClickListener {
            b.ivThird.isEnabled = true
            b.ivSecond.isEnabled = false
        }
        b.ivThird.setOnClickListener {
            b.ivThird.isEnabled = false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val uri: Uri = data?.data!!
        when (requestCode) {
            IMG1_GALLERY_REQ_CODE, IMG1_CAMERA_REQ_CODE -> {
                this.mImg1 = uri
                b.ivFirst.imgGallery.setLocalImage(uri)
            }
            IMG2_GALLERY_REQ_CODE, IMG2_CAMERA_REQ_CODE -> {
                this.mImg2 = uri
                b.selectPassportImage.imgGallery.setLocalImage(uri)
            }
            IMG3_GALLERY_REQ_CODE, IMG3_CAMERA_REQ_CODE -> {
                this.mImg3 = uri
                b.selectPassportImage.imgGallery.setLocalImage(uri)
            }
        }
    }

    private fun setData(
        title: String,
        description: String,
        cityId: Int,
        userId: Int,
        categoryId: Int,
        phone: String,
        price: String,
        img1: Uri,
        img2: Uri,
        img3: Uri
    ) {
        viewModel.addAnimal.observe(viewLifecycleOwner) {
            when (it.status) {
                ResourceState.LOADING -> {
                    Toast.makeText(requireContext(), "LOADING", Toast.LENGTH_SHORT).show()
                }
                ResourceState.SUCCESS -> {
                    Toast.makeText(requireContext(), "SUCCESS", Toast.LENGTH_SHORT).show()
                }
                ResourceState.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModel.addAnimal(
            title,
            description,
            cityId,
            userId,
            categoryId,
            phone,
            price,
            img1,
            img2,
            img3
        )
    }
    }
    private fun showPopup(view: View) {
        val popup = PopupMenu(view.context, view)
        popup.inflate(R.menu.city_menu)
//        popup.setOnMenuItemClickListener { item: MenuItem? ->
//            when (item!!.itemId) {
//                R.id.nokis -> {
//                    cityId = 2
//                    b.tvCity.text = item.title
//                }
//                R.id.shimbay -> {
//                    cityId = 1
//                    b.tvCity.text = item.title
//                }
//                R.id.qonirat -> {
//                    cityId = 3
//                    b.tvCity.text = item.title
//                }
//                R.id.xojeli -> {
//                    cityId = 4
//                    b.tvCity.text = item.title
//                }
//                R.id.taxtakopir -> {
//                    cityId = 5
//                    b.tvCity.text = item.title
//                }
//                R.id.moynaq -> {
//                    cityId = 6
//                    b.tvCity.text = item.title
//                }
//                R.id.nokisRayon -> {
//                    cityId = 7
//                    b.tvCity.text = item.title
//                }
//                R.id.kegeyli -> {
//                    cityId = 8
//                    b.tvCity.text = item.title
//                }
//                R.id.qanlikol -> {
//                    cityId = 9
//                    b.tvCity.text = item.title
//                }
//                R.id.shomanay -> {
//                    cityId = 10
//                    b.tvCity.text = item.title
//                }
//                R.id.amudarya -> {
//                    cityId = 11
//                    b.tvCity.text = item.title
//                }
//                R.id.beruniy -> {
//                    cityId = 12
//                    b.tvCity.text = item.title
//                }
//                R.id.tortkol -> {
//                    cityId = 13
//                    b.tvCity.text = item.title
//                }
//                R.id.elliqqala -> {
//                    cityId = 14
//                    b.tvCity.text = item.title
//                }
//                R.id.bozataw -> {
//                    cityId = 15
//                    b.tvCity.text = item.title
//                }
//                R.id.qaraozek -> {
//                    cityId = 16
//                    b.tvCity.text = item.title
//                }
//                R.id.taqiatas -> {
//                    cityId = 17
//                    b.tvCity.text = item.title
//                }
//            }
//            true
//        }
        popup.show()
    }

private const val IMG1_GALLERY_REQ_CODE = 102
private const val IMG1_CAMERA_REQ_CODE = 103
private const val IMG2_GALLERY_REQ_CODE = 102
private const val IMG2_CAMERA_REQ_CODE = 103
private const val IMG3_GALLERY_REQ_CODE = 102
private const val IMG3_CAMERA_REQ_CODE = 103
}