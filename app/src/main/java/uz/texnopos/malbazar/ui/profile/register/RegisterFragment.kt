package uz.texnopos.malbazar.ui.profile.register

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import onClick
import org.koin.androidx.viewmodel.ext.android.viewModel
import textToString
import toast
import uz.texnopos.malbazar.R
import uz.texnopos.malbazar.core.ResourceState
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
        binding.tvSignIn.onClick {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
        binding.btnSignUp.setOnClickListener {
            when {
                binding.etPhone.text.toString().length < 9 -> {
                    binding.etPhone.error = "Durıs formattda kiritıń"
                }
                binding.etPassword.text!!.length < 6 -> {
                    binding.etPassword.error = "Parolińızdıń uzınlıǵı keminde 6 bolıw kerek"
                }
                binding.etName.text!!.isEmpty() -> {
                    binding.etName.error = "Atıńızdı kiritıń"
                }
                else -> {
                    val phone = binding.etPhone.text.toString()
                    viewModel.registerUser(
                        phone = phone,
                        name = binding.etName.textToString(),
                        password = binding.etPassword.textToString()
                    )
                }
            }
        }

        viewModel.registerUser.observe(viewLifecycleOwner) {
            when (it.status) {
                ResourceState.LOADING -> {
                    binding.progressBar.isVisible = true
                }
                ResourceState.SUCCESS -> {
                    binding.progressBar.isVisible = false
                    token = it.data?.token
                    userId = it.data!!.userId
                    findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
                }
                ResourceState.ERROR -> {
                    it.message?.let { it1 -> toast(it1) }
                    binding.progressBar.isVisible = false
                }
            }
        }
    }
}