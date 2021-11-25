package uz.texnopos.malbazar.ui.profile.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.malbazar.R
import uz.texnopos.malbazar.data.helper.ResourceState
import uz.texnopos.malbazar.databinding.FragmentLoginBinding

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var b: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        b = FragmentLoginBinding.bind(view)
        b.btnSignIn.setOnClickListener {
            when {
                b.etPassword.text.isEmpty() -> {
                    b.etPassword.error = ""
                }
                b.etPhone.text.isEmpty() -> {
                    b.etPassword.error = ""
                }
                else -> {
                    viewModel.loginUser(
                        b.etPhone.text.toString().toInt(),
                        b.etPassword.text.toString()
                    )
                }
            }
        }
        viewModel.login.observe(viewLifecycleOwner) {
            when (it.status) {
                ResourceState.LOADING -> {
                    b.progressBar.isVisible = true
                }
                ResourceState.SUCCESS -> {
                    Toast.makeText(requireContext(), "SUCCESS", Toast.LENGTH_SHORT).show()
                    b.progressBar.isVisible = false
                }
                ResourceState.ERROR -> {
                    b.progressBar.isVisible = false
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        b.btnSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }
}