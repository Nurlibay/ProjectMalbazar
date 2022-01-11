package uz.texnopos.malbazar.ui.selected

import android.view.View
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import hideProgress
import org.koin.androidx.viewmodel.ext.android.viewModel
import showProgress
import toast
import uz.texnopos.malbazar.R
import uz.texnopos.malbazar.core.Constants
import uz.texnopos.malbazar.core.ResourceState
import uz.texnopos.malbazar.core.SelectCategory
import uz.texnopos.malbazar.core.preferences.isSignedIn
import uz.texnopos.malbazar.databinding.FragmentSelectedBinding

class SelectedAnimalsFragment : Fragment(R.layout.fragment_selected) {

    private lateinit var binding: FragmentSelectedBinding
    private val viewModel: GetSelectedViewModel by viewModel()
    private val adapter = SelectedAnimalsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI()
        setUpObserver()
        binding = FragmentSelectedBinding.bind(view)
        viewModel.getSelectedAnimals()
        binding.apply {
            rvSelectedAnimals.adapter = adapter
            adapter.onItemClick = { id, categoryId ->
                var category = SelectCategory().selectCategory(categoryId)
                val action =
                    SelectedAnimalsFragmentDirections.actionSelectedFragmentToSelectedInfoFragment(
                        id,
                        category
                    )
                findNavController().navigate(action)
            }
        }
    }

    private fun updateUI() {
        if (!isSignedIn()){
            findNavController().navigate(R.id.action_selectedFragment_to_loginFragment)
        }
    }

    private fun setUpObserver() {
        viewModel.getSelectedAnimals.observe(viewLifecycleOwner) {
            when (it.status) {
                ResourceState.LOADING -> showProgress()
                ResourceState.SUCCESS -> {
                    hideProgress()
                    if (it.data?.isEmpty() == true) {
                        binding.apply {
                            tvNullData.isVisible = true
                            rvSelectedAnimals.isVisible = false
                        }
                    } else {
                        adapter.models = it.data!!
                    }
                }
                ResourceState.ERROR -> {
                    hideProgress()
                    it.message?.let { it1 -> toast(it1) }
                }
                ResourceState.NETWORK_ERROR -> {
                    hideProgress()
                    toast(Constants.NO_INTERNET)
                }
            }
        }
    }
}