package uz.texnopos.malbazar.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import uz.texnopos.malbazar.R
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import hideProgress
import org.koin.androidx.viewmodel.ext.android.viewModel
import showProgress
import toast
import uz.texnopos.malbazar.core.Constants
import uz.texnopos.malbazar.core.ResourceState
import uz.texnopos.malbazar.core.preferences.userId
import uz.texnopos.malbazar.data.model.Animal
import uz.texnopos.malbazar.data.model.Category
import uz.texnopos.malbazar.databinding.FragmentMainBinding
import uz.texnopos.malbazar.ui.main.category.CategoryAdapter
import uz.texnopos.malbazar.ui.main.category.CategoryViewModel
import uz.texnopos.malbazar.ui.main.search.SearchViewModel

class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var binding: FragmentMainBinding
    private val adapterLastAdded = AdapterLastAdded()
    private val adapterMoreViewed = AdapterMoreViewed()
    private val adapterCategory = CategoryAdapter()
    private val mainViewModel: MainViewModel by viewModel()
    private val searchViewModel: SearchViewModel by viewModel()
    private val categoryViewModel: CategoryViewModel by viewModel()
    private var lastAdded: List<Animal> = listOf()
    private var views: List<Animal> = listOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        mainViewModel.lastAnimals()
        categoryViewModel.getCategory()
        setUpObserverGetCategory()
        binding.apply {
            rvLastAnimals.adapter = adapterLastAdded
            rvMoreViewed.adapter = adapterMoreViewed
            rvCategory.adapter = adapterCategory

            // search function
            etSearch.addTextChangedListener {
                val query: String = etSearch.text.toString()
                if (query.isEmpty()) {
                    mainViewModel.lastAnimals()
                    binding.apply {
                        tvMoreViewed.isVisible = true
                        tvLastAdded.isVisible = true
                        rvLastAnimals.isVisible = true
                    }
                } else if (query.length >= 3) {
                    userId?.let { it1 -> searchViewModel.searchAnimal(query, "all", "all", it1) }
                    binding.apply {
                        tvMoreViewed.isVisible = false
                        tvLastAdded.isVisible = false
                        rvLastAnimals.isVisible = false
                    }
                }
            }
        }

        adapterCategory.onItemClick = {
            binding.apply {
                if (it == 0) {
                    userId?.let { it1 -> searchViewModel.searchAnimal("", "all", "all", it1) }
                    tvMoreViewed.isVisible = false
                    tvLastAdded.isVisible = false
                    rvLastAnimals.isVisible = false
                } else {
                    userId?.let { it1 -> searchViewModel.searchAnimal("", "all", "$it", it1) }
                    tvMoreViewed.isVisible = false
                    tvLastAdded.isVisible = false
                    rvLastAnimals.isVisible = false
                }
            }
        }

        adapterLastAdded.onItemClick = {
            val action = MainFragmentDirections.actionMainFragmentToInfoFragment(it)
            findNavController().navigate(action)
        }

        adapterMoreViewed.onItemClick = {
            val action = MainFragmentDirections.actionMainFragmentToInfoFragment(it)
            findNavController().navigate(action)
        }
    }

    private fun setData() {
        adapterMoreViewed.models = views
        adapterLastAdded.models = lastAdded
    }

    private fun setUpObserverGetCategory() {
        binding.apply {
            mainViewModel.lastAnimals.observe(viewLifecycleOwner) {
                when (it.status) {
                    ResourceState.LOADING -> showProgress()
                    ResourceState.SUCCESS -> {
                        views = it.data!!.views
                        lastAdded = it.data.lastes
                        setData()
                        hideProgress()
                        tvMoreViewed.isVisible = true
                        rvMoreViewed.isVisible = true
                        tvLastAdded.isVisible = true
                        rvLastAnimals.isVisible = true
                    }
                    ResourceState.ERROR -> {
                        it.message?.let { it1 -> toast(it1) }
                        hideProgress()
                    }
                }
            }

            searchViewModel.search.observe(viewLifecycleOwner) {
                when (it.status) {
                    ResourceState.LOADING -> {
                        showProgress()
                        rvLastAnimals.isVisible = false
                        rvLastAnimals.isVisible = false
                        tvMoreViewed.isVisible = false
                        tvLastAdded.isVisible = false
                    }
                    ResourceState.SUCCESS -> {
                        adapterMoreViewed.models = listOf()
                        adapterMoreViewed.models = it!!.data!!.results
                        rvMoreViewed.isVisible = true
                        hideProgress()
                    }
                    ResourceState.ERROR -> {
                        adapterMoreViewed.models = listOf()
                        it.message?.let { it1 -> toast(it1) }
                        hideProgress()
                        rvMoreViewed.isVisible = true
                    }
                }
            }

            categoryViewModel.getCategory.observe(requireActivity()) {
                when (it.status) {
                    ResourceState.LOADING -> showProgress()
                    ResourceState.SUCCESS -> {
                        var category = Category(
                            "",
                            "https://t4.ftcdn.net/jpg/01/85/42/93/240_F_185429335_uxBeyiRS6OUK9cfVGlCXqwbdERiZlXHW.jpg",
                            0,
                            getString(R.string.all),
                            ""
                        )
                        val list: MutableList<Category> = mutableListOf(category)
                        it.data!!.forEach { category ->
                            list.add(category)
                        }
                        adapterCategory.models = list
                        hideProgress()
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
    }
}