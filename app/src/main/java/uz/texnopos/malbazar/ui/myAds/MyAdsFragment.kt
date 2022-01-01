package uz.texnopos.malbazar.ui.myAds

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.malbazar.R
import uz.texnopos.malbazar.core.ResourceState
import uz.texnopos.malbazar.core.preferences.userId

class MyAdsFragment : Fragment(R.layout.fragment_my_ads) {

    private val viewModel:MyAdsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData(userId!!)
    }

    private fun getData(userId: Int) {
        viewModel.userAds(userId)
        viewModel.myAds.observe(viewLifecycleOwner){
            when(it.status){
                ResourceState.LOADING -> {
                    Toast.makeText(requireContext(), "LOADING", Toast.LENGTH_SHORT).show()
                }
                ResourceState.SUCCESS -> {
                    Toast.makeText(requireContext(), "SUCCESS", Toast.LENGTH_SHORT).show()
                    if(it.data!!.isEmpty()){

                    }
                }
            }
        }
    }
}