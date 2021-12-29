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
import uz.texnopos.malbazar.data.helper.ResourceState
import uz.texnopos.malbazar.data.models.Animal
import uz.texnopos.malbazar.databinding.FragmentMainBinding
import uz.texnopos.malbazar.ui.main.search.SearchViewModel

class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var b: FragmentMainBinding
    private val adapter = MainAdapter()
    private val adapter2 = MainAdapter2()
    private val mainViewModel: MainViewModel by viewModel()
    private val searchViewModel: SearchViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        b = FragmentMainBinding.bind(view)
        getData()
        b.recyclerView.adapter = adapter
        b.recyclerView2.adapter = adapter2

        b.recyclerView.addItemDecoration(
            DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))

        b.recyclerView2.addItemDecoration(
            DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))

        b.swipe.setOnRefreshListener {
            b.swipe.isRefreshing = false
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
        b.btnSearch.setOnClickListener {
            var quer: String = b.searchView.text.toString()
            searchAnimal(quer)
            b.tvEnKopKoringenler.isVisible = false
            b.tvLastLook.isVisible = false
            b.recyclerView2.isVisible = false
        }
    }

    private fun searchAnimal(quer: String) {

        searchViewModel.searchAnimal(quer)

        searchViewModel.search.observe(viewLifecycleOwner) {
            when (it.status) {
                ResourceState.LOADING -> {
                    b.lottie.isVisible = true
                }
                ResourceState.SUCCESS -> {
                    adapter.models = listOf()
                    adapter.models = it!!.data!!.results
                    b.lottie.isVisible = false
                }
                ResourceState.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    b.lottie.isVisible = false
                }
            }
        }
    }

    private fun getData() {
        mainViewModel.lastAnimals()
        mainViewModel.lastAnimals.observe(viewLifecycleOwner) {
            when (it.status) {
                ResourceState.LOADING -> {
                    b.swipe.isRefreshing = false
                    b.lottie.isVisible = true
                }
                ResourceState.SUCCESS -> {
                    b.lottie.isVisible = false
                    setData(it.data!!.lastes, it.data.views)
                }
                ResourceState.ERROR -> {
                    b.lottie.isVisible = false
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