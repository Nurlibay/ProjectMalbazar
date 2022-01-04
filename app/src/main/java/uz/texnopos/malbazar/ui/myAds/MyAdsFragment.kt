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

class MyAdsFragment : Fragment(R.layout.fragment_my_ads) {

    private val viewModel:MyAdsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.userAds(userId!!)
        setUpObserver()
    }

    private fun setUpObserver() {
        viewModel.myAds.observe(requireActivity(), {
            when (it.status) {
                ResourceState.LOADING -> showProgress()
                ResourceState.SUCCESS -> {
                    hideProgress()
                    if(it.data!!.ads.isEmpty())toast(getString(R.string.empty_ad_list))
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