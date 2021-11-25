package uz.texnopos.malbazar.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import uz.texnopos.malbazar.R
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.malbazar.data.helper.ResourceState
import uz.texnopos.malbazar.data.models.Animal
import uz.texnopos.malbazar.data.models.AnimalsCategory
import uz.texnopos.malbazar.databinding.FragmentMainBinding

class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var b: FragmentMainBinding
    private val adapter = MainAdapter()
    private val animalsCategoryViewModel: AnimalsCategortViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        b = FragmentMainBinding.bind(view)
        var list: MutableList<Animal> = mutableListOf()
        for (i in 1..30) {
            list.add(Animal("$i animal", i))
        }
        adapter.models = list
        b.recyclerView.adapter = adapter

        b.ivSort.setOnClickListener {
            showPopupMenu(it)
        }
    }

    private fun showPopupMenu(view: View?) {

        animalsCategoryViewModel.animalsCategory.observe(viewLifecycleOwner) {
            when (it.status) {
                ResourceState.LOADING -> {
                    Toast.makeText(requireContext(), "loading", Toast.LENGTH_SHORT).show()
                }
                ResourceState.SUCCESS -> {

                }
                ResourceState.ERROR -> {

                }
            }
        }

        val popup = PopupMenu(requireContext(), view)
        with(popup) {
            for (i in 0..10) {
            }
            inflate(R.menu.menu_sort)
            setOnMenuItemClickListener {

                true
            }
            show()
        }
    }
}