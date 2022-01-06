package uz.texnopos.malbazar.ui.main.info

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
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
import textToString
import toast
import uz.texnopos.malbazar.R
import uz.texnopos.malbazar.core.Constants.ASK_PHONE_PERMISSION_REQUEST_CODE
import uz.texnopos.malbazar.core.ResourceState
import uz.texnopos.malbazar.core.SelectCategory
import uz.texnopos.malbazar.core.SelectCity
import uz.texnopos.malbazar.data.model.Animal
import uz.texnopos.malbazar.databinding.FragmentInfoBinding

class InfoFragment : Fragment(R.layout.fragment_info) {

    private lateinit var binding: FragmentInfoBinding
    private val adapter = InfoAdapter()
    private val args: InfoFragmentArgs by navArgs()
    private val viewModel: InfoViewModel by viewModel()
    private val addSelectedAnimalViewModel: SelectedViewModel by viewModel()
    private val maxImageCount = 3
    private var imageCount = 1
    private lateinit var animal: Animal
    private var select = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData(args.id)
        binding = FragmentInfoBinding.bind(view)

        binding.apply {
            toolbar.title = args.categoryId
            recyclerView.adapter = adapter
            ivLeft.isVisible = false

            tvPhoneNumber.setOnClickListener {
                callToUser()
            }
            ivCallIcon.setOnClickListener {
                callToUser()
            }
            toolbar.setNavigationOnClickListener {
                findNavController().navigate(R.id.action_infoFragment_to_mainFragment)
            }
            ivRight.setOnClickListener {
                if (maxImageCount - imageCount == 2) {
                    imageCount = 2
                    binding.tvImageCount.text = "2/3"
                    binding.ivLeft.isVisible = true
                    Glide
                        .with(requireContext())
                        .load(animal.img2)
                        .into(binding.ivAnimal)
                } else {
                    imageCount = 3
                    binding.tvImageCount.text = "3/3"
                    binding.ivRight.isVisible = false
                    binding.ivLeft.isVisible = true
                    Glide
                        .with(requireContext())
                        .load(animal.img3)
                        .into(binding.ivAnimal)
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
                        .into(binding.ivAnimal)
                } else {
                    imageCount = 2
                    binding.tvImageCount.text = "2/3"
                    Glide
                        .with(requireContext())
                        .load(animal.img2)
                        .into(binding.ivAnimal)
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
                    else -> false
                }
            }
        }

        adapter.onItemClick = { id, categoryId ->
            updateUI(id, categoryId)
        }

    }

    private fun callToUser() {
        var phone = binding.tvPhoneNumber.textToString()
        if (isHasPermission(Manifest.permission.CALL_PHONE)) {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$phone")
            startActivity(callIntent)
        } else askPermission(
            arrayOf(Manifest.permission.CALL_PHONE),
            ASK_PHONE_PERMISSION_REQUEST_CODE
        )
    }

    private fun getData(id: Int) {
        viewModel.getAnimalInfo(id)
        setUpObserver()
    }

    private fun setUpObserver() {
        viewModel.getInfoAboutAnimal.observe(viewLifecycleOwner) {
            when (it.status) {
                ResourceState.LOADING -> showProgress()
                ResourceState.SUCCESS -> {
                    hideProgress()
                    setDataToRecyclerView(it.data!!.likes)
                    animal = it.data.animal
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
                    toast("Successful selected")
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
                    toast("Successful unSelected")
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
        Glide
            .with(requireContext())
            .load(animal.img1)
            .into(binding.ivAnimal)

        var city: String = SelectCity().selectCity(animal.city_id)
        binding.tvDescription.text = animal.description
        binding.tvPhoneNumber.text = ": ${animal.phone}"
        binding.tvPrice.text = ": ${animal.price}"
        binding.tvTitle.text = animal.title
        binding.tvCity.text = ": $city"
    }

    private fun setDataToRecyclerView(likes: List<Animal>) {
        adapter.models = likes
    }

    private fun updateUI(id: Int, categoryId: Int) {
        val category = SelectCategory().selectCategory(categoryId)
        val action = InfoFragmentDirections.actionInfoFragmentSelf(id, category)
        findNavController().navigate(action)
    }
}