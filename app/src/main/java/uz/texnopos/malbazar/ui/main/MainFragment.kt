package uz.texnopos.malbazar.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import uz.texnopos.malbazar.R
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.malbazar.SelectCity
import uz.texnopos.malbazar.data.helper.ResourceState
import uz.texnopos.malbazar.data.models.Animal
import uz.texnopos.malbazar.databinding.FragmentMainBinding

class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var b: FragmentMainBinding
    private val adapter = MainAdapter()
    private val adapter2 = MainAdapter2()
    private val mainViewModel: MainViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        b = FragmentMainBinding.bind(view)
        getData()
        b.recyclerView.adapter = adapter
        b.recyclerView2.adapter = adapter2

        b.lottie.setAnimation(R.raw.farm)
        b.lottie.speed = 0.6f

        b.ivSort.setOnClickListener {
            category()
        }
        b.swipe.setOnRefreshListener {
            getData()
        }
        adapter.onItemClick = {
            val city = SelectCity()
            val action = MainFragmentDirections.actionMainFragmentToInfoFragment(
                imgFirst = it.img1,
                imgSecond = it.img2,
                animalTitle = it.title,
                description = it.description,
                phone = it.phone,
                price = it.price,
                adress = city.selectCity(it.city_id),
                id = it.id
            )
            findNavController().navigate(action)
        }
        adapter2.onItemClick = {
            val city = SelectCity()
            val action = MainFragmentDirections.actionMainFragmentToInfoFragment(
                imgFirst = it.img1,
                imgSecond = it.img2,
                animalTitle = it.title,
                description = it.description,
                phone = it.phone,
                price = it.price,
                adress = city.selectCity(it.city_id),
                id = it.id
            )
            findNavController().navigate(action)
        }
    }

    private fun getData() {
        mainViewModel.lastAnimals()
        mainViewModel.lastAnimals.observe(viewLifecycleOwner) {
            when (it.status) {
                ResourceState.LOADING -> {
                    b.lottie.playAnimation()
                }
                ResourceState.SUCCESS -> {
                    b.apply {
                        lottie.isVisible = false
                        searchView.isVisible = true
                        ivSort.isVisible = true
                        swipe.isVisible = true
                    }
                    setData(it.data!!.lastes, it.data.views)
                }
                ResourceState.ERROR -> {
                    b.apply {
                        lottie.isVisible = false
                        searchView.isVisible = true
                        ivSort.isVisible = true
                        swipe.isVisible = true
                    }

                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setData(lastes: List<Animal>, views: List<Animal>) {
        adapter.models = lastes
        adapter2.models = views
    }

    private fun category() {
        b.tvLastLook.isVisible = false

    }
}