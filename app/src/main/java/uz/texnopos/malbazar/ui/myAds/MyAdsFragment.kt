package uz.texnopos.malbazar.ui.myAds

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import hideProgress
import org.koin.androidx.viewmodel.ext.android.viewModel
import showProgress
import toast
import uz.texnopos.malbazar.R
import uz.texnopos.malbazar.core.Constants
import uz.texnopos.malbazar.core.ResourceState
import uz.texnopos.malbazar.core.preferences.userId
import uz.texnopos.malbazar.databinding.FragmentMyAdsBinding

class MyAdsFragment : Fragment(R.layout.fragment_my_ads) {

    private lateinit var binding: FragmentMyAdsBinding
    private val viewModel:MyAdsViewModel by viewModel()
    private val adapter = MyAdsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMyAdsBinding.bind(view)
        viewModel.userAds(userId!!)
        setUpObserver()
        binding.apply {
            rvMyAds.adapter = adapter
        }
    }

    private fun setUpObserver() {
        viewModel.myAds.observe(requireActivity(), {
            when (it.status) {
                ResourceState.LOADING -> showProgress()
                ResourceState.SUCCESS -> {
                    hideProgress()
                    if(it.data!!.ads.isEmpty())toast(getString(R.string.empty_ad_list))
                    adapter.models = it.data.ads
                    binding.apply {
                        etName.setText(it.data.user_name)
                        etPhone.setText(it.data.phone)
                    }
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