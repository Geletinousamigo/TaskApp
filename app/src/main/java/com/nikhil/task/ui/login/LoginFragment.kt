package com.nikhil.task.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.nikhil.task.R
import com.nikhil.task.databinding.FragmentLoginBinding
import com.nikhil.task.utils.NetworkResult


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val authViewModel by activityViewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (authViewModel.isRegistered()) {
            binding.btnSignUp.text = resources.getString(R.string.forgot_password)
        }

        binding.btnSignUp.setOnClickListener {
            authViewModel.clearCredentials()
            it.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.btnLogin.setOnClickListener {
            loginUser()
        }
        bindObservers()
    }


    private fun loginUser() {
        val emailAddress = binding.txtUsername.text.toString()
        val password = binding.txtPassword.text.toString()
        val validation = authViewModel.validation(emailAddress, password)
        validation.onSuccess {

            authViewModel.loginUser(username = emailAddress, password = password)

        }.onFailure {
            showValidationErrors(it.message.orEmpty())
        }
    }


    private fun showValidationErrors(error: String) {
        binding.txtError.text = String.format(resources.getString(R.string.txt_error, error))
    }

    private fun bindObservers() {
        authViewModel.authLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                }

                is NetworkResult.Error -> {
                    showValidationErrors(it.message.toString())
                }

                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        })
    }
}