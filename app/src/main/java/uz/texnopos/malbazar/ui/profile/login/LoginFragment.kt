package uz.texnopos.malbazar.ui.profile.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import checkIsEmpty
import com.google.android.material.textfield.TextInputEditText
import com.redmadrobot.inputmask.MaskedTextChangedListener
import getOnlyDigits
import hideProgress
import onClick
import org.koin.androidx.viewmodel.ext.android.viewModel
import showError
import showProgress
import textToString
import toast
import uz.texnopos.malbazar.R
import uz.texnopos.malbazar.core.ResourceState
import uz.texnopos.malbazar.core.mask.MaskWatcherPhone
import uz.texnopos.malbazar.core.preferences.isSignedIn
import uz.texnopos.malbazar.core.preferences.token
import uz.texnopos.malbazar.core.preferences.userId
import uz.texnopos.malbazar.databinding.FragmentLoginBinding

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        updateUI()
        setUpObserver()
        binding.apply {
            etPhone.addTextChangedListener(MaskWatcherPhone.phoneNumber())
            etPhone.addMaskAndHint("([00]) [000]-[00]-[00]")
            btnLogin.onClick {
                when {
                    etPhone.checkIsEmpty() -> {
                        tilPhone.showError(getString(R.string.required))
                    }
                    (etPhone.textToString().getOnlyDigits()).length < 9 -> {
                        tilPhone.showError(getString(R.string.number_format_exception))
                    }
                    etPassword.checkIsEmpty() -> {
                        tilPhone.isErrorEnabled = false
                        tilPassword.showError(getString(R.string.required))
                    }
                    etPassword.textToString().length < 6 -> {
                        tilPhone.isErrorEnabled = false
                        tilPassword.showError(getString(R.string.password_format_exception))
                    }
                    else -> {
                        etPassword.isCursorVisible = false
                        etPhone.isCursorVisible = false
                       viewModel.loginUser(
                            phone = ("+998${etPhone.textToString().getOnlyDigits()}"),
                            password = binding.etPassword.textToString()
                        )
                    }
                }
            }
            btnSignUp.onClick {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
        }
    }

    private fun setUpObserver(){
        viewModel.login.observe(viewLifecycleOwner) {
            when (it.status) {
                ResourceState.LOADING -> {
                    showProgress()
                }
                ResourceState.SUCCESS -> {
                    hideProgress()
                    token = it.data!!.token
                    userId = it.data.userId
                    updateUI()
                }
                ResourceState.ERROR -> {
                    hideProgress()
                    toast(it.message!!)
                }
            }
        }
    }

    private fun updateUI() {
        if (isSignedIn()) findNavController().navigate(R.id.action_loginFragment_to_myAdsFragment)
    }

    private fun TextInputEditText.addMaskAndHint(mask: String) {
        val listener = MaskedTextChangedListener.installOn(
            this,
            mask
        )
        this.hint = listener.placeholder()
    }

}