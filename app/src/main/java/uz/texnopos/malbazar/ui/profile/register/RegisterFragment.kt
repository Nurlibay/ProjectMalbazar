package uz.texnopos.malbazar.ui.profile.register

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import checkIsEmpty
import com.google.android.material.textfield.TextInputEditText
import com.redmadrobot.inputmask.MaskedTextChangedListener
import getOnlyDigits
import kotlinx.android.synthetic.main.fragment_login.*
import onClick
import org.koin.androidx.viewmodel.ext.android.viewModel
import showError
import textToString
import toast
import uz.texnopos.malbazar.R
import uz.texnopos.malbazar.core.ResourceState
import uz.texnopos.malbazar.core.mask.MaskWatcherPhone
import uz.texnopos.malbazar.core.preferences.isSignedIn
import uz.texnopos.malbazar.core.preferences.token
import uz.texnopos.malbazar.core.preferences.userId
import uz.texnopos.malbazar.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: RegisterViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(view)
        updateUI()
        setUpObserver()
        binding.apply {
            etPhone.addTextChangedListener(MaskWatcherPhone.phoneNumber())
            etPhone.addMaskAndHint("([00]) [000]-[00]-[00]")
            tvSignIn.onClick {
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }
            btnSignUp.onClick {
                if(validate()) {
                    viewModel.registerUser(
                        phone = ("+998${etPhone.textToString().getOnlyDigits()}"),
                        name = binding.etName.textToString(),
                        password = binding.etPassword.textToString()
                    )
                }
            }
        }
    }

    private fun setUpObserver() {
        viewModel.registerUser.observe(viewLifecycleOwner) {
            when (it.status) {
                ResourceState.LOADING -> {
                    binding.progressBar.isVisible = true
                }
                ResourceState.SUCCESS -> {
                    binding.progressBar.isVisible = false
                    token = it.data?.token
                    userId = it.data!!.userId
                    updateUI()
                }
                ResourceState.ERROR -> {
                    it.message?.let { it1 -> toast(it1) }
                    binding.progressBar.isVisible = false
                }
            }
        }
    }

    private fun updateUI() {
        if (isSignedIn()) findNavController().navigate(R.id.action_registerFragment_to_myAdsFragment)
    }

    private fun FragmentRegisterBinding.validate(): Boolean {
        return when {
            etPhone.checkIsEmpty() -> tilPhone.showError(getString(R.string.required))
            etPassword.checkIsEmpty() -> tilPassword.showError(getString(R.string.required))
            etPassword.textToString().length < 8 -> tilPassword.showError(getString(R.string.password_format_exception))
            etName.checkIsEmpty() -> tilName.showError(getString(R.string.required))
            else -> true
        }
    }

    private fun TextInputEditText.addMaskAndHint(mask: String) {
        val listener = MaskedTextChangedListener.installOn(
            this,
            mask
        )
        this.hint = listener.placeholder()
    }

}