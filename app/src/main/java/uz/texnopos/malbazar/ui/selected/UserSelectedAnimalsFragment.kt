package uz.texnopos.malbazar.ui.selected

import android.view.View
import android.os.Bundle
import androidx.fragment.app.Fragment
import hideProgress
import org.koin.androidx.viewmodel.ext.android.viewModel
import showProgress
import toast
import uz.texnopos.malbazar.R
import uz.texnopos.malbazar.core.ResourceState
import uz.texnopos.malbazar.databinding.FragmentSelectedBinding

class UserSelectedAnimalsFragment : Fragment(R.layout.fragment_selected) {

    private lateinit var binding: FragmentSelectedBinding
    private val viewModel: GetSelectedViewModel by viewModel()
    private val adapter = SelectedAnimalsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSelectedBinding.bind(view)
        viewModel.getSelectedAnimals()
        setUpObserver()
        binding.apply {
            rvSelectedAnimals.adapter = adapter
        }
    }

    private fun setUpObserver() {
        viewModel.getSelectedAnimals.observe(viewLifecycleOwner) {
            when (it.status) {
                ResourceState.LOADING -> showProgress()
                ResourceState.SUCCESS -> {
                    hideProgress()
                   adapter.models = it.data!!
                }
                ResourceState.ERROR -> {
                    hideProgress()
                    it.message?.let { it1 -> toast(it1) }
                }
            }
        }
    }
}