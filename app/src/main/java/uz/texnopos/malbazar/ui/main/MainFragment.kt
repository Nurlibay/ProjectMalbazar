package uz.texnopos.malbazar.ui.main

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import uz.texnopos.malbazar.R
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import hideProgress
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import org.koin.androidx.viewmodel.ext.android.viewModel
import showProgress
import toast
import uz.texnopos.malbazar.core.Constants
import uz.texnopos.malbazar.core.Resource
import uz.texnopos.malbazar.core.ResourceState
import uz.texnopos.malbazar.data.models.Animal
import uz.texnopos.malbazar.databinding.FragmentMainBinding
import uz.texnopos.malbazar.ui.main.category.CategoryAdapter
import uz.texnopos.malbazar.ui.main.category.CategoryViewModel
import uz.texnopos.malbazar.ui.main.search.SearchViewModel

class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var binding: FragmentMainBinding
    private val adapterLastAdded = AdapterLastAdded()
    private val adapterCategory = CategoryAdapter()
    private val adapterMoreViewed = AdapterMoreViewed()
    private val mainViewModel: MainViewModel by viewModel()
    private val searchViewModel: SearchViewModel by viewModel()
    private val categoryViewModel: CategoryViewModel by viewModel()
    private var lastAdded: List<Animal> = listOf()
    private var views: List<Animal> = listOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)
        getData()
        categoryViewModel.getCategory()
        setUpObserverGetCategory()
        binding.apply {
            rvLastAnimals.adapter = adapterLastAdded
            rvMoreViewed.adapter = adapterMoreViewed
            rvCategory.adapter = adapterCategory

            // search func
            etSearch.addTextChangedListener {
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

        adapterCategory.onItemClick = {
            searchCategory(it)
            binding.tvMoreViewed.isVisible = false
            binding.tvLastLook.isVisible = false
            binding.rvMoreViewed.isVisible = false
        }
        adapterLastAdded.onItemClick = {
            goToInfoFragment(it)
        }
        adapterMoreViewed.onItemClick = {
            goToInfoFragment(it)
        }
    }

    private fun searchCategory(it: Int) {
        searchViewModel.searchAnimal("", "all", "$it")
        searchViewModel.search.observe(viewLifecycleOwner) {
            when (it.status) {
                ResourceState.LOADING -> {
                    showProgress()
                    binding.rvLastAnimals.isVisible = false
                    binding.rvMoreViewed.isVisible = false
                    binding.tvMoreViewed.isVisible = false
                    binding.tvLastLook.isVisible = false
                }
                ResourceState.SUCCESS -> {
                    adapterLastAdded.models = listOf()
                    adapterLastAdded.models = it!!.data!!.results
                    hideProgress()
                    binding.rvLastAnimals.isVisible = true
                }
                ResourceState.ERROR -> {
                    it.message?.let { it1 -> toast(it1) }
                    hideProgress()
                    binding.rvLastAnimals.isVisible = true
                }
            }
        }
    }


    private fun goToInfoFragment(id: Int) {
        val action = MainFragmentDirections.actionMainFragmentToInfoFragment(id)
        findNavController().navigate(action)
    }

    private fun searchAnimal(query: String) {
        searchViewModel.searchAnimal(query, "all", "all")
        searchViewModel.search.observe(viewLifecycleOwner) {
            when (it.status) {
                ResourceState.LOADING -> {
                    showProgress()
                    binding.rvLastAnimals.isVisible = false
                    binding.rvMoreViewed.isVisible = false
                    binding.tvMoreViewed.isVisible = false
                    binding.tvLastLook.isVisible = false
                }
                ResourceState.SUCCESS -> {
                    adapterLastAdded.models = listOf()
                    adapterLastAdded.models = it!!.data!!.results
                    hideProgress()
                    binding.rvLastAnimals.isVisible = true
                }
                ResourceState.ERROR -> {
                    it.message?.let { it1 -> toast(it1) }
                    hideProgress()
                    binding.rvLastAnimals.isVisible = true
                }
            }
        }
    }

    private fun getData() {
        mainViewModel.lastAnimals()
        mainViewModel.lastAnimals.observe(viewLifecycleOwner) {
            when (it.status) {
                ResourceState.LOADING -> showProgress()
                ResourceState.SUCCESS -> {
                    lastAdded = it.data!!.lastes
                    views = it.data.views
                    setData()
                    hideProgress()
                    binding.rvLastAnimals.isVisible = true
                    binding.rvMoreViewed.isVisible = true
                    binding.tvMoreViewed.isVisible = true
                }
                ResourceState.ERROR -> {
                    it.message?.let { it1 -> toast(it1) }
                    hideProgress()
                    binding.tvLastLook.isVisible = false
                }
            }
        }
    }

    private fun setData() {
        adapterLastAdded.models = lastAdded
        adapterMoreViewed.models = views
    }

    private fun setUpObserverGetCategory() {
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