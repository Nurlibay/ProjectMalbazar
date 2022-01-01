package uz.texnopos.malbazar.ui.profile.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.malbazar.R
import uz.texnopos.malbazar.core.ResourceState
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
        updateUI()
        binding.btnLogin.setOnClickListener {
            when {
                binding.etPassword.text!!.isEmpty() -> {
                    binding.etPassword.error = ""
                }
                binding.etPhone.text!!.isEmpty() -> {
                    binding.etPassword.error = ""
                }
                else -> {
                    viewModel.loginUser(
                        binding.etPhone.text.toString(),
                        binding.etPassword.text.toString()
                    )
                }
            }
        }
        viewModel.login.observe(viewLifecycleOwner) {
            when (it.status) {
                ResourceState.LOADING -> {
                    binding.progressBar.isVisible = true
                }
                ResourceState.SUCCESS -> {
                    binding.progressBar.isVisible = false
                    token = it.data!!.token
                    userId = it.data.userId
                    updateUI()
                }
                ResourceState.ERROR -> {
                    binding.progressBar.isVisible = false
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.btnSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun updateUI() {
        if (isSignedIn()) findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
    }
}