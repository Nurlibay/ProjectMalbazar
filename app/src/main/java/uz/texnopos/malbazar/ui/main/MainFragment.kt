package uz.texnopos.malbazar.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import uz.texnopos.malbazar.R
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.malbazar.data.helper.ResourceState
import uz.texnopos.malbazar.data.models.Animal
import uz.texnopos.malbazar.databinding.FragmentMainBinding
import uz.texnopos.malbazar.ui.main.search.SearchViewModel

class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var binding: FragmentMainBinding
    private val adapter = MainAdapter()
    private val adapter2 = MainAdapter2()
    private val mainViewModel: MainViewModel by viewModel()
    private val searchViewModel: SearchViewModel by viewModel()
    private var lastest: List<Animal> = listOf()
    private var viewss: List<Animal> = listOf()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)
        getData()
        binding.recyclerView.adapter = adapter
        binding.recyclerView2.adapter = adapter2

        adapter.onPhoneClick = {
            val intent = Intent(Intent.ACTION_DIAL);
            intent.data = Uri.parse("tel:${it.phone}")
            startActivity(intent)
        }
        adapter2.onPhoneClick = {
            val intent = Intent(Intent.ACTION_DIAL);
            intent.data = Uri.parse("tel:${it.phone}")
            startActivity(intent)
        }
        binding.searchView.addTextChangedListener {
            if (it!!.isEmpty()) {
                adapter.models = lastest
                binding.tvEnKopKoringenler.isVisible = true
                binding.tvLastLook.isVisible = true
                binding.recyclerView2.isVisible = true
            } else {
                var query: String = binding.searchView.text.toString()
                searchAnimal(query)
                binding.tvEnKopKoringenler.isVisible = false
                binding.tvLastLook.isVisible = false
                binding.recyclerView2.isVisible = false
            }
        }
    }

    private fun searchAnimal(query: String) {

        searchViewModel.searchAnimal(query)

        searchViewModel.search.observe(viewLifecycleOwner) {
            when (it.status) {
                ResourceState.LOADING -> {
                    binding.shimmerLayout.startShimmer()
                    binding.recyclerView.isVisible = false
                    binding.recyclerView2.isVisible = false
                    binding.tvEnKopKoringenler.isVisible = false
                    binding.tvLastLook.isVisible = false
                }
                ResourceState.SUCCESS -> {
                    adapter.models = listOf()
                    adapter.models = it!!.data!!.results
                    binding.shimmerLayout.isVisible = false
                    binding.recyclerView.isVisible = true
                }
                ResourceState.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    binding.shimmerLayout.isVisible = false
                    binding.recyclerView.isVisible = true
                }
            }
        }
    }

    private fun getData() {
        mainViewModel.lastAnimals()
        mainViewModel.lastAnimals.observe(viewLifecycleOwner) {
            when (it.status) {
                ResourceState.LOADING -> {
                    binding.shimmerLayout.startShimmer()
                }
                ResourceState.SUCCESS -> {
                    lastest = it.data!!.lastes
                    viewss = it.data.views
                    setData()
                    binding.shimmerLayout.isVisible = false
                    binding.recyclerView.isVisible = true
                    binding.recyclerView2.isVisible = true
                    binding.tvEnKopKoringenler.isVisible = true
                }
                ResourceState.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    binding.shimmerLayout.isVisible = false
                    binding.tvLastLook.isVisible = false
                }
            }
        }
    }

    private fun setData() {
        adapter.models = lastest
        adapter2.models = viewss
    }
}