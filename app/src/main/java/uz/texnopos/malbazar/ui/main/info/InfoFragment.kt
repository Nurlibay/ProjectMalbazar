package uz.texnopos.malbazar.ui.main.info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import hideProgress
import org.koin.androidx.viewmodel.ext.android.viewModel
import showProgress
import toast
import uz.texnopos.malbazar.R
import uz.texnopos.malbazar.SelectCity
import uz.texnopos.malbazar.core.ResourceState
import uz.texnopos.malbazar.data.models.Animal
import uz.texnopos.malbazar.data.models.AnimalInfo
import uz.texnopos.malbazar.databinding.FragmentInfoBinding

class InfoFragment : Fragment(R.layout.fragment_info) {

    private lateinit var binding: FragmentInfoBinding
    private val args: InfoFragmentArgs by navArgs()
    private val adapter = InfoAdapter()
    private val viewModel: InfoViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getData(args.id)

        binding = FragmentInfoBinding.bind(view)
        binding.recyclerView.adapter = adapter
        Glide
            .with(binding.root.context)
            .load(R.drawable.malbazar_logo)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(18)))
            .into(binding.ivAnimal)
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
                    setInfo(it.data.animal)
                }
                ResourceState.ERROR -> {
                    hideProgress()
                    it.message?.let { it1 -> toast(it1) }
                }
            }
        }
    }

    private fun setInfo(animal: Animal) {
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