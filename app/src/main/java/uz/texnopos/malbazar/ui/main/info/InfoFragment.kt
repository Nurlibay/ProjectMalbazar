package uz.texnopos.malbazar.ui.main.info

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
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
import uz.texnopos.malbazar.SelectCity
import uz.texnopos.malbazar.core.Constants.ASK_PHONE_PERMISSION_REQUEST_CODE
import uz.texnopos.malbazar.core.ResourceState
import uz.texnopos.malbazar.data.models.Animal
import uz.texnopos.malbazar.databinding.FragmentInfoBinding

class InfoFragment : Fragment(R.layout.fragment_info) {

    private lateinit var binding: FragmentInfoBinding
    private val args: InfoFragmentArgs by navArgs()
    private val adapter = InfoAdapter()
    private val viewModel: InfoViewModel by viewModel()
    private val maxImageCount = 3
    private var imageCount = 1
    private lateinit var animal: Animal

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getData(args.id)

        binding = FragmentInfoBinding.bind(view)
        binding.recyclerView.adapter = adapter
        binding.ivLeft.isEnabled = false

        binding.tvPhoneNumber.setOnClickListener {
            var phone = binding.tvPhoneNumber.textToString()
            if (isHasPermission(Manifest.permission.CALL_PHONE)) {
                val callIntent = Intent(Intent.ACTION_CALL)
                callIntent.data = Uri.parse("tel:$phone")
                startActivity(callIntent)
            } else askPermission(arrayOf(Manifest.permission.CALL_PHONE), ASK_PHONE_PERMISSION_REQUEST_CODE)

        }

        binding.back.setOnClickListener {
            requireActivity().onBackPressed()
        }
        adapter.onItemClick = {
            var action = InfoFragmentDirections.actionInfoFragmentSelf(it)
            findNavController().navigate(action)
        }
        binding.ivRight.setOnClickListener {
            if (maxImageCount - imageCount == 2) {
                imageCount = 2
                Glide
                    .with(requireContext())
                    .load(animal.img2)
                    .into(binding.ivAnimal)
            } else {
                imageCount = 3
                binding.ivRight.isEnabled = false
                binding.ivLeft.isEnabled = true
                Glide
                    .with(requireContext())
                    .load(animal.img3)
                    .into(binding.ivAnimal)
            }
        }
        binding.ivLeft.setOnClickListener {
            binding.ivRight.isEnabled = true
            if (maxImageCount - imageCount == 1) {
                imageCount = 1
                binding.ivLeft.isEnabled = false
                Glide
                    .with(requireContext())
                    .load(animal.img1)
                    .into(binding.ivAnimal)
            } else {
                imageCount = 2
                Glide
                    .with(requireContext())
                    .load(animal.img2)
                    .into(binding.ivAnimal)
            }
        }
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
    }

    private fun setInfo() {
        Glide
            .with(requireContext())
            .load(animal.img1)
            .into(binding.ivAnimal)

        var city: String = SelectCity().selectCity(animal.city_id)
        binding.tvDescription.text = animal.description
        binding.tvPhoneNumber.text = animal.phone
        binding.tvPrice.text = animal.price
        binding.tvTitle.text = animal.title
        binding.tvCity.text = city
    }

    private fun setDataToRecyclerView(likes: List<Animal>) {
        adapter.models = likes
    }
}