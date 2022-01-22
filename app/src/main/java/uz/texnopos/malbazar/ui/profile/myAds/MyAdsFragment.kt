package uz.texnopos.malbazar.ui.profile.myAds

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
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
    private val deleteAdsViewModel: DeleteAdsViewModel by viewModel()
    private val adapter = MyAdsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMyAdsBinding.bind(view)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
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
        adapter.onItemClick = {
            goToInfoFragment(it)
        }
        adapter.deleteItemClick = {
            deleteAdsViewModel.deleteAd(it)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUpObserver() {
        deleteAdsViewModel.deleteAds.observe(viewLifecycleOwner) {
            when (it.status) {
                ResourceState.LOADING -> showProgress()
                ResourceState.SUCCESS -> {
                    viewModel.userAds(userId!!)
                    toast("Дағаза ѳширилди")
                    updateUI()
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
        }
        viewModel.myAds.observe(requireActivity(), {
            when (it.status) {
                ResourceState.LOADING -> showProgress()
                ResourceState.SUCCESS -> {
                    hideProgress()
                    if (it.data!!.ads.isEmpty()) {
                        binding.apply {
                            tvNotAds.isVisible = true
                            ivLogo.isVisible = true
                            tvName.text = (it.data.user_name)
                            tvPhoneNumber.text = (it.data.phone)
                            tvAdsCount.text = "Дағазалар саны: ${it.data.ads_count}"
                        }
                    } else {
                        adapter.models = it.data.ads
                        binding.apply {
                            tvMyAds.isVisible = true
                            rvMyAds.isVisible = true
                            ivLogo.isVisible = true
                            tvName.text = (it.data.user_name)
                            tvPhoneNumber.text = (it.data.phone)
                            tvAdsCount.text = "Дағазалар саны: ${it.data.ads_count}"
                        }
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

    private fun updateUI() {
        findNavController().navigate(R.id.action_myAdsFragment_self)
    }

    private fun goToInfoFragment(id: Int) {
        val action = MyAdsFragmentDirections.actionMyAdsFragmentToInfoFragment(id)
        findNavController().navigate(action)
    }
}