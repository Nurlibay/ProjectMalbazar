package uz.texnopos.malbazar.ui.main

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import uz.texnopos.malbazar.R
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import toast
import uz.texnopos.malbazar.core.ResourceState
import uz.texnopos.malbazar.data.models.Animal
import uz.texnopos.malbazar.databinding.FragmentMainBinding
import uz.texnopos.malbazar.ui.main.search.SearchViewModel

class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var binding: FragmentMainBinding
    private val adapterLastAdded = AdapterLastAdded()
    private val adapterMoreViewed = AdapterMoreViewed()
    private val mainViewModel: MainViewModel by viewModel()
    private val searchViewModel: SearchViewModel by viewModel()
    private var lastAdded: List<Animal> = listOf()
    private var views: List<Animal> = listOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)
        getData()
        binding.apply {
            rvLastAnimals.adapter = adapterLastAdded
            rvMoreViewed.adapter = adapterMoreViewed
        }


        adapterLastAdded.onItemClick = {
            goToInfoFragment(it)
        }
        adapterMoreViewed.onItemClick = {
            goToInfoFragment(it)
        }

        binding.etSearch.addTextChangedListener {
            if (it!!.isEmpty()) {
                adapterLastAdded.models = lastAdded
                binding.tvMoreViewed.isVisible = true
                binding.tvLastLook.isVisible = true
                binding.rvMoreViewed.isVisible = true
            } else {
                val query: String = binding.etSearch.text.toString()
                searchAnimal(query)
                binding.tvMoreViewed.isVisible = false
                binding.tvLastLook.isVisible = false
                binding.rvMoreViewed.isVisible = false
            }
        }
    }


    private fun goToInfoFragment(id: Int) {
        val action = MainFragmentDirections.actionMainFragmentToInfoFragment(id)
        findNavController().navigate(action)
    }

    private fun searchAnimal(query: String) {

        searchViewModel.searchAnimal(query)

        searchViewModel.search.observe(viewLifecycleOwner) {
            when (it.status) {
                ResourceState.LOADING -> {
                    //binding.shimmerLayout.startShimmer()
                    binding.rvLastAnimals.isVisible = false
                    binding.rvMoreViewed.isVisible = false
                    binding.tvMoreViewed.isVisible = false
                    binding.tvLastLook.isVisible = false
                }
                ResourceState.SUCCESS -> {
                    adapterLastAdded.models = listOf()
                    adapterLastAdded.models = it!!.data!!.results
                    //binding.shimmerLayout.isVisible = false
                    binding.rvLastAnimals.isVisible = true
                }
                ResourceState.ERROR -> {
                    it.message?.let { it1 -> toast(it1) }
                    //binding.shimmerLayout.isVisible = false
                    binding.rvLastAnimals.isVisible = true
                }
            }
        }
    }

    private fun getData() {
        mainViewModel.lastAnimals()
        mainViewModel.lastAnimals.observe(viewLifecycleOwner) {
            when (it.status) {
                ResourceState.LOADING -> {
                    //binding.shimmerLayout.startShimmer()
                }
                ResourceState.SUCCESS -> {
                    lastAdded = it.data!!.lastes
                    views = it.data.views
                    setData()
                    //binding.shimmerLayout.isVisible = false
                    binding.rvLastAnimals.isVisible = true
                    binding.rvMoreViewed.isVisible = true
                    binding.tvMoreViewed.isVisible = true
                }
                ResourceState.ERROR -> {
                    it.message?.let { it1 -> toast(it1) }
                    //binding.shimmerLayout.isVisible = false
                    binding.tvLastLook.isVisible = false
                }
            }
        }
    }

    private fun setData() {
        adapterLastAdded.models = lastAdded
        adapterMoreViewed.models = views
    }
}