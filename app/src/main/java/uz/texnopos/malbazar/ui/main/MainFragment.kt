package uz.texnopos.malbazar.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import uz.texnopos.malbazar.R
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.malbazar.SelectCity
import uz.texnopos.malbazar.core.ResourceState
import uz.texnopos.malbazar.data.models.Animal
import uz.texnopos.malbazar.databinding.FragmentMainBinding
import uz.texnopos.malbazar.ui.main.search.SearchViewModel

class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var binding: FragmentMainBinding
    private val adapter = MainAdapter()
    private val adapter2 = MainAdapter2()
    private val mainViewModel: MainViewModel by viewModel()
    private val searchViewModel: SearchViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)
        getData()
        binding.recyclerView.adapter = adapter
        binding.recyclerView2.adapter = adapter2

        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))

        binding.recyclerView2.addItemDecoration(
            DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))

        binding.swipe.setOnRefreshListener {
            binding.swipe.isRefreshing = false
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
        binding.btnSearch.setOnClickListener {
            var quer: String = binding.searchView.text.toString()
            searchAnimal(quer)
            binding.tvEnKopKoringenler.isVisible = false
            binding.tvLastLook.isVisible = false
            binding.recyclerView2.isVisible = false
        }
    }

    private fun searchAnimal(quer: String) {

        searchViewModel.searchAnimal(quer)

        searchViewModel.search.observe(viewLifecycleOwner) {
            when (it.status) {
                ResourceState.LOADING -> {
                    binding.lottie.isVisible = true
                }
                ResourceState.SUCCESS -> {
                    adapter.models = listOf()
                    adapter.models = it!!.data!!.results
                    binding.lottie.isVisible = false
                }
                ResourceState.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    binding.lottie.isVisible = false
                }
            }
        }
    }

    private fun getData() {
        mainViewModel.lastAnimals()
        mainViewModel.lastAnimals.observe(viewLifecycleOwner) {
            when (it.status) {
                ResourceState.LOADING -> {
                    binding.swipe.isRefreshing = false
                    binding.lottie.isVisible = true
                }
                ResourceState.SUCCESS -> {
                    binding.lottie.isVisible = false
                    setData(it.data!!.lastes, it.data.views)
                }
                ResourceState.ERROR -> {
                    binding.lottie.isVisible = false
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setData(lastes: List<Animal>, views: List<Animal>) {
        adapter.models = lastes
        adapter2.models = views
    }
}