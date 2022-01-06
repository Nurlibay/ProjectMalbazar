package uz.texnopos.malbazar.ui.selected

import android.view.View
import android.os.Bundle
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.malbazar.R
import uz.texnopos.malbazar.databinding.FragmentSelectedBinding

class UserSelectedAnimals : Fragment(R.layout.fragment_selected) {

    private lateinit var binding:FragmentSelectedBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSelectedBinding.bind(view)
    }
}