package uz.texnopos.malbazar.ui.main.info

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import askPermission
import com.bumptech.glide.Glide
import hideProgress
import isHasPermission
import org.koin.androidx.viewmodel.ext.android.viewModel
import showProgress
import toast
import uz.texnopos.malbazar.core.Constants.ASK_PHONE_PERMISSION_REQUEST_CODE
import uz.texnopos.malbazar.core.ResourceState
import uz.texnopos.malbazar.core.SelectCity
import uz.texnopos.malbazar.data.model.Animal
import uz.texnopos.malbazar.databinding.FragmentInfoBinding
import android.os.Build
import android.os.Environment
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.fragment_info.*
import uz.texnopos.malbazar.BuildConfig
import uz.texnopos.malbazar.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class InfoFragment : Fragment(R.layout.fragment_info) {

    private lateinit var binding: FragmentInfoBinding
    private val adapter = InfoAdapter()
    private val args: InfoFragmentArgs by navArgs()
    private val viewModel: InfoViewModel by viewModel()
    private val addSelectedAnimalViewModel: SelectedViewModel by viewModel()
    private val maxImageCount = 3
    private var imageCount = 1
    private lateinit var animal: Animal
    private var select: Int = 1
    private var imageUrl: String = ""
    private var bmp: Bitmap? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentInfoBinding.bind(view)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        viewModel.getAnimalInfo(args.id)
        setUpObserver()
        select = args.isSelected
        setMenu()
        binding.apply {
            clComment.setOnClickListener {
                val action = InfoFragmentDirections.actionInfoFragmentToCommentsFragment(args.id)
                findNavController().navigate(action)
            }

                recyclerView.adapter = adapter

                tvPhoneNumber.setOnClickListener {
                    callToUser()
                }
                ivCallIcon.setOnClickListener {
                    callToUser()
                }
                toolbar.setNavigationOnClickListener {
                    requireActivity().onBackPressed()
                }
            ivRight.setOnClickListener {
                if (maxImageCount - imageCount == 2) {
                    imageCount = 2
                    binding.tvImageCount.text = "2/3"
                    binding.ivLeft.isVisible = true
                    Glide
                        .with(requireContext())
                        .load(animal.img2)
                        .into(binding.ivFirstAnimal)
                } else {
                    imageCount = 3
                    binding.tvImageCount.text = "3/3"
                    binding.ivRight.isVisible = false
                    binding.ivLeft.isVisible = true
                    Glide
                        .with(requireContext())
                        .load(animal.img3)
                        .into(binding.ivFirstAnimal)
                }
            }
            ivLeft.setOnClickListener {
                binding.ivRight.isVisible = true
                if (maxImageCount - imageCount == 1) {
                    imageCount = 1
                    binding.tvImageCount.text = "1/3"
                    binding.ivLeft.isVisible = false
                    Glide
                        .with(requireContext())
                        .load(animal.img1)
                        .into(binding.ivFirstAnimal)
                } else {
                    imageCount = 2
                    binding.tvImageCount.text = "2/3"
                    Glide
                        .with(requireContext())
                        .load(animal.img2)
                        .into(binding.ivFirstAnimal)
                }
            }
            }

            toolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.selected -> {
                        if (select == 0) {
                            select = 1
                            menuItem.setIcon(R.drawable.selected)
                            addSelectedAnimalViewModel.addSelectedAnimal(animal.id)
                        } else {
                            select = 0
                            menuItem.setIcon(R.drawable.select)
                            addSelectedAnimalViewModel.deleteSelectedAnimal(animal.id)
                        }
                        true
                    }
                    R.id.share -> {
                        shareImageFromBitmap(bmp)
                        true
                    }
                    else -> false
                }
            }

        adapter.onItemClick = {
            updateUI(it)
        }
    }

    private fun setMenu() {
        binding.apply {
            if (select == 0) {
                toolbar.inflateMenu(R.menu.menu_select_info)
            } else {
                toolbar.inflateMenu(R.menu.menu_unselect_info)
            }
        }
    }

    private fun shareImageFromBitmap(bmp: Bitmap?) {
        var city: String = SelectCity().selectCity(animal.city_id)
        val textToShare =
            "🕹Категория: ${animal.categoryName}\n💰Бахасы: ${animal.price}\n🏠Аймақ: $city\n📞Телефон: ${animal.phone}\n✍Мағлыумат: ${animal.description}"
        val uri = getUriImageFromBitmap(bmp, requireContext())
        val shareIntent = Intent(Intent.ACTION_SEND)
        this.imageUrl = animal.img1
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        shareIntent.putExtra(Intent.EXTRA_TEXT, "${textToShare}\n${this.imageUrl}")
        shareIntent.type = "image/png"
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(Intent.createChooser(shareIntent, animal.categoryName))
    }

    private fun getUriImageFromBitmap(bmp: Bitmap?, context: Context): Uri? {
        if (bmp == null) return null
        var bmpUri: Uri? = null
        try {
            val file = File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "IMG_" + System.currentTimeMillis() + ".png"
            )
            val out = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.flush()
            out.close()

            bmpUri =
                FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bmpUri
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun callToUser() {
        var phone = animal.phone
        if (isHasPermission(Manifest.permission.CALL_PHONE)) {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$phone")
            startActivity(callIntent)
        } else askPermission(
            arrayOf(Manifest.permission.CALL_PHONE),
            ASK_PHONE_PERMISSION_REQUEST_CODE
        )
    }

    private fun setUpObserver() {
        viewModel.getInfoAboutAnimal.observe(viewLifecycleOwner) {
            when (it.status) {
                ResourceState.LOADING -> showProgress()
                ResourceState.SUCCESS -> {
                    hideProgress()
                    adapter.models = it.data!!.likes
                    animal = it.data.animal
                    binding.toolbar.title = it.data.animal.categoryName
                    setInfo()
                }
                ResourceState.ERROR -> {
                    hideProgress()
                    it.message?.let { it1 -> toast(it1) }
                }
            }
        }

        addSelectedAnimalViewModel.selectedAnimal.observe(viewLifecycleOwner) {
            when (it.status) {
                ResourceState.LOADING -> showProgress()
                ResourceState.SUCCESS -> {
                    hideProgress()
                }
                ResourceState.ERROR -> {
                    hideProgress()
                    it.message?.let { it1 -> toast(it1) }
                }
            }
        }

        addSelectedAnimalViewModel.unSelectedAnimal.observe(viewLifecycleOwner) {
            when (it.status) {
                ResourceState.LOADING -> showProgress()
                ResourceState.SUCCESS -> {
                    hideProgress()
                }
                ResourceState.ERROR -> {
                    hideProgress()
                    it.message?.let { it1 -> toast(it1) }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setInfo() {
        binding.apply {
            Glide
                .with(requireContext())
                .load(animal.img1)
                .into(ivFirstAnimal)
            tvDescription.text = animal.description
            tvPhoneNumber.text = ": ${animal.phone}"
            tvPrice.text = ": ${animal.price}"
            tvTitle.text = animal.title
            tvCity.text = ": ${animal.city_name}"
            tvCommentCount.text = animal.countComments.toString()
            if (animal.img2.isNotEmpty()) {
                ivRight.isVisible = true
                tvImageCount.isVisible = true
                Glide
                    .with(requireContext())
                    .load(animal.img2)
                    .into(ivSecondAnimal)
                Glide
                    .with(requireContext())
                    .load(animal.img3)
                    .into(ivThirdAnimal)
            }
        }
    }

    private fun updateUI(id: Int) {
        val action = InfoFragmentDirections.actionInfoFragmentSelf(id)
        findNavController().navigate(action)
    }
}