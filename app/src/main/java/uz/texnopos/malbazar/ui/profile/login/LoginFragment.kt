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
import uz.texnopos.malbazar.preferences.token
import uz.texnopos.malbazar.preferences.userId

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var b: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        b = FragmentLoginBinding.bind(view)
        animateViews()
        b.btnSignIn.setOnClickListener {
            when {
                b.etPassword.text!!.isEmpty() -> {
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
                    b.progressBar.isVisible = false
                    token = it.data!!.token
                    userId = it.data.userId
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
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

    private fun animateViews() {
        b.btnSignIn.translationX = -1000F
        b.btnSignIn.alpha = 0F
        b.btnSignIn.animate().translationX(0F).alpha(1F).setDuration(1500).setStartDelay(100)
            .start()

        b.iv1.translationY = 1000F
        b.iv1.alpha = 0F
        b.iv1.animate().translationY(0F).alpha(1F).setDuration(1000).setStartDelay(10)
            .start()
        b.ivPeron.translationY = 1000F
        b.ivPeron.alpha = 0F
        b.ivPeron.animate().translationY(0F).alpha(1F).setDuration(1000).setStartDelay(10)
            .start()

        b.appName.translationY = 1000F
        b.appName.alpha = 0F
        b.appName.animate().translationY(0F).alpha(1F).setDuration(1000).setStartDelay(30)
            .start()

        b.etPassword.translationX = 1100F
        b.etPassword.alpha = 0F
        b.etPassword.animate().translationX(0F).alpha(1F).setDuration(1200).setStartDelay(400)
            .start()

        b.etPhone.translationX = -1200F
        b.etPhone.alpha = 0F
        b.etPhone.animate().translationX(0F).alpha(1F).setDuration(1400).setStartDelay(400)
            .start()

        b.btnSignUp.translationY = 1000F
        b.btnSignUp.alpha = 0F
        b.btnSignUp.animate().translationY(0F).alpha(1F).setDuration(1500).setStartDelay(100)
            .start()
    }
}