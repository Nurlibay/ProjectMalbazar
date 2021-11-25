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

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var b: FragmentRegisterBinding
    private val viewModel: RegisterViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        b = FragmentRegisterBinding.bind(view)
        b.btnSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
        b.btnSignUp.setOnClickListener {
            when {
                b.etPhone.text.toString().length < 9 -> {
                    b.etPhone.error = "Durıs formattda kiritıń"
                }
                b.etPassword.text.length < 6 -> {
                    b.etPhone.error = "Parolińızdıń uzınlıǵı keminde 6 bolıw kerek"
                }
                b.etName.text.isEmpty() -> {
                    b.etName.error = "Atıńızdı kiritıń"
                }
                else -> {
                    var phone = b.etPhone.text.toString()
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
                    Toast.makeText(requireContext(), "SUCCESS", Toast.LENGTH_SHORT).show()
                }
                ResourceState.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    b.progressBar.isVisible = false
                }
            }
        }
    }
}