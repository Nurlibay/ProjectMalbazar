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
import toast
import uz.texnopos.malbazar.core.Constants
import uz.texnopos.malbazar.core.ResourceState
import uz.texnopos.malbazar.core.SelectCategory
import uz.texnopos.malbazar.data.model.Animal
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
        mainViewModel.lastAnimals()
        categoryViewModel.getCategory()
        setUpObserverGetCategory()
        binding.apply {
            rvLastAnimals.adapter = adapterLastAdded
            rvMoreViewed.adapter = adapterMoreViewed
            rvCategory.adapter = adapterCategory

            ivCategory.setOnClickListener {
                tvLastAdded.isVisible = true
                mainViewModel.lastAnimals()
            }

            // search function
            etSearch.addTextChangedListener {
                val query: String = etSearch.text.toString()
                if (query.length >= 3) {
                    searchViewModel.searchAnimal(query, "all", "all")
                    binding.apply {
                        tvMoreViewed.isVisible = false
                        tvLastAdded.isVisible = false
                        rvLastAnimals.isVisible = false
                    }
                }
            }
        }

        adapterCategory.onItemClick = {
            searchViewModel.searchAnimal("", "all", "$it")
            binding.apply {
                tvMoreViewed.isVisible = false
                tvLastAdded.isVisible = false
                rvLastAnimals.isVisible = false
            }
        }
        adapterLastAdded.onItemClick = { id: Int, categoryId: Int ->
            var category = SelectCategory().selectCategory(categoryId)
            val action = MainFragmentDirections.actionMainFragmentToInfoFragment(id, category)
            findNavController().navigate(action)
        }
        adapterMoreViewed.onItemClick = { id: Int, categoryId: Int ->
            var category = SelectCategory().selectCategory(categoryId)
            val action = MainFragmentDirections.actionMainFragmentToInfoFragment(id, category)
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
                        rvLastAnimals.isVisible = true
                        tvMoreViewed.isVisible = true
                        views = it.data!!.views
                        lastAdded = it.data.lastes
                        setData()
                        hideProgress()
                    }
                    ResourceState.ERROR -> {
                        it.message?.let { it1 -> toast(it1) }
                        hideProgress()
                        rvLastAnimals.isVisible = false
                        tvMoreViewed.isVisible = false
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
                        it.message?.let { it1 -> toast(it1) }
                        hideProgress()
                        rvMoreViewed.isVisible = true
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
                        adapterLastAdded.models = listOf()
                        adapterLastAdded.models = it!!.data!!.results
                        hideProgress()
                        rvLastAnimals.isVisible = true
                    }
                    ResourceState.ERROR -> {
                        it.message?.let { it1 -> toast(it1) }
                        hideProgress()
                        rvLastAnimals.isVisible = true
                    }
                }
            }

            categoryViewModel.getCategory.observe(requireActivity(), {
                when (it.status) {
                    ResourceState.LOADING -> showProgress()
                    ResourceState.SUCCESS -> {
                        adapterCategory.models = it.data!!
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
            })
        }
    }
}