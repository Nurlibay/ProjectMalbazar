package uz.texnopos.malbazar.ui.main

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import uz.texnopos.malbazar.R
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import hideProgress
import org.koin.androidx.viewmodel.ext.android.viewModel
import showProgress
import textToString
import toast
import uz.texnopos.malbazar.core.Constants
import uz.texnopos.malbazar.core.ResourceState
import uz.texnopos.malbazar.data.model.Animal
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
        lastAnimals()
        binding.apply {
            rvLastAnimals.adapter = adapterLastAdded
            rvMoreViewed.adapter = adapterMoreViewed
        }

        adapterLastAdded.setOnItemClickListener {
            val action = MainFragmentDirections.actionMainFragmentToInfoFragment(it)
            findNavController().navigate(action)
        }

        adapterMoreViewed.onItemClick = {
            val action = MainFragmentDirections.actionMainFragmentToInfoFragment(it)
            findNavController().navigate(action)
        }

        binding.etSearch.addTextChangedListener {
            if (it!!.isEmpty()) {
                adapterLastAdded.models = lastAdded
                binding.tvMoreViewed.isVisible = true
                binding.tvLastLook.isVisible = true
                binding.rvMoreViewed.isVisible = true
            } else {
                val query: String = binding.etSearch.textToString()
                searchAnimal(query)
                binding.tvMoreViewed.isVisible = false
                binding.tvLastLook.isVisible = false
                binding.rvMoreViewed.isVisible = false
            }
        }
    }

    private fun searchAnimal(query: String) {
        searchViewModel.searchAnimal(query)
        searchViewModel.search.observe(viewLifecycleOwner) {
            when (it.status) {
                ResourceState.LOADING -> {
                    showProgress()
                }
                ResourceState.SUCCESS -> {
                    adapterLastAdded.models = it!!.data!!.results
                }
                ResourceState.ERROR -> {
                    hideProgress()
                    toast(it.message!!)
                }
                ResourceState.NETWORK_ERROR -> {
                    hideProgress()
                    toast(Constants.NO_INTERNET)
                }
            }
        }
    }

    private fun lastAnimals() {
        mainViewModel.lastAnimals()
        mainViewModel.lastAnimals.observe(viewLifecycleOwner) {
            when (it.status) {
                ResourceState.LOADING -> { showProgress() }
                ResourceState.SUCCESS -> {
                    lastAdded = it.data!!.latest
                    views = it.data.views
                    setData()
                }
                ResourceState.ERROR -> {
                    hideProgress()
                    toast(it.message!!)
                }
                ResourceState.NETWORK_ERROR -> {
                    hideProgress()
                    toast(Constants.NO_INTERNET)
                }
            }
        }
    }

    private fun setData() {
        adapterLastAdded.models = lastAdded
        adapterMoreViewed.models = views
    }
}