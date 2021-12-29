package uz.texnopos.malbazar.ui.profile.register

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.malbazar.R
import uz.texnopos.malbazar.data.helper.ResourceState
import uz.texnopos.malbazar.databinding.FragmentRegisterBinding
import uz.texnopos.malbazar.preferences.isSignedIn
import uz.texnopos.malbazar.preferences.token
import uz.texnopos.malbazar.preferences.userId

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var b: FragmentRegisterBinding
    private val viewModel: RegisterViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isSignedIn()) findNavController().navigate(R.id.action_registerFragment_to_myAdsFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        b = FragmentRegisterBinding.bind(view)
        animate()
        b.btnSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
        b.btnSignUp.setOnClickListener {
            when {
                b.etPhone.text.toString().length < 9 -> {
                    b.etPhone.error = "Durıs formattda kiritıń"
                }
                b.etPassword.text!!.length < 6 -> {
                    b.etPassword.error = "Parolińızdıń uzınlıǵı keminde 6 bolıw kerek"
                }
                b.etName.text.isEmpty() -> {
                    b.etName.error = "Atıńızdı kiritıń"
                }
                else -> {
                    val phone = b.etPhone.text.toString()
                    viewModel.registerUser(
                        phone = phone.toInt(),
                        name = b.etName.text.toString(),
                        password = b.etName.toString()
                    )
                }
            }
        }
        viewModel.registerUser.observe(viewLifecycleOwner) {
            when (it.status) {
                ResourceState.LOADING -> {
                    b.progressBar.isVisible = true
                }
                ResourceState.SUCCESS -> {
                    b.progressBar.isVisible = false
                    token = it.data?.token
                    userId = it.data!!.userId
                    findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
                }
                ResourceState.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    b.progressBar.isVisible = false
                }
            }
        }
    }

  private fun animate() {
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

        b.etName.translationX = -1200F
        b.etName.alpha = 0F
        b.etName.animate().translationX(0F).alpha(1F).setDuration(1400).setStartDelay(400)
            .start()

        b.etPhone.translationX = -1200F
        b.etPhone.alpha = 0F
        b.etPhone.animate().translationX(0F).alpha(1F).setDuration(1400).setStartDelay(400)
            .start()

        b.btnSignUp.translationX = 1000F
        b.btnSignUp.alpha = 0F
        b.btnSignUp.animate().translationX(0F).alpha(1F).setDuration(1500).setStartDelay(100)
            .start()
    }
}