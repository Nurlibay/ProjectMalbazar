package uz.texnopos.malbazar.ui.profile.myAds

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import hideProgress
import org.koin.androidx.viewmodel.ext.android.viewModel
import showProgress
import toast
import uz.texnopos.malbazar.R
import uz.texnopos.malbazar.core.Constants
import uz.texnopos.malbazar.core.Constants.TOKEN
import uz.texnopos.malbazar.core.ResourceState
import uz.texnopos.malbazar.core.SelectCategory
import uz.texnopos.malbazar.core.preferences.getSharedPreferences
import uz.texnopos.malbazar.core.preferences.userId
import uz.texnopos.malbazar.databinding.FragmentMyAdsBinding
import uz.texnopos.malbazar.ui.dialogs.ExitFromAccountDialog

class MyAdsFragment : Fragment(R.layout.fragment_my_ads) {

    private lateinit var binding: FragmentMyAdsBinding
    private val viewModel: MyAdsViewModel by viewModel()
    private val adapter = MyAdsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMyAdsBinding.bind(view)
        viewModel.userAds(userId!!)
        setUpObserver()
        binding.apply {
            rvMyAds.adapter = adapter
            toolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.logout -> {
                        val dialog = ExitFromAccountDialog(requireContext())
                        dialog.show()
                        dialog.exitClick = {
                            userId = 0
                            getSharedPreferences().removeKey(TOKEN)
                            findNavController().navigate(R.id.action_myAdsFragment_to_loginFragment)
                        }
                        true
                    }
                    R.id.info -> {
                        findNavController().navigate(R.id.action_myAdsFragment_to_aboutUsFragment)
                        true
                    }
                    else -> false
                }
            }
        }
        adapter.onItemClick = { id: Int, categoryId: Int ->
            goToInfoFragment(id, categoryId)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUpObserver() {
        viewModel.myAds.observe(requireActivity(), {
            when (it.status) {
                ResourceState.LOADING -> showProgress()
                ResourceState.SUCCESS -> {
                    hideProgress()
                    if (it.data!!.ads.isEmpty()) toast(getString(R.string.empty_ad_list))
                    adapter.models = it.data.ads
                    binding.apply {
                        tvName.text = (it.data.user_name)
                        tvPhoneNumber.text = (it.data.phone)
                        tvAdsCount.text = "Дағазалар саны: ${it.data.ads_count}"
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

    private fun goToInfoFragment(id: Int, categoryId: Int) {
        val category = SelectCategory().selectCategory(categoryId)
        val action = MyAdsFragmentDirections.actionMyAdsFragmentToInfoFragment(id, category)
        findNavController().navigate(action)
    }
}