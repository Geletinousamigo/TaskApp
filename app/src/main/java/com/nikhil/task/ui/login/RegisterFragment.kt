package com.nikhil.task.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.nikhil.task.R
import com.nikhil.task.databinding.FragmentRegisterBinding
import com.nikhil.task.utils.NetworkResult


class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val authViewModel by activityViewModels<AuthViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        if (authViewModel.isRegistered()) {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*binding.btnLoginUp.setOnClickListener {
            it.findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }*/
        binding.btnLoginUp.visibility = View.GONE

        binding.btnRegister.setOnClickListener {
            registerUser()
        }
        bindObservers()
    }

    private fun registerUser() {
        val emailAddress = binding.txtUsername.text.toString()
        val password = binding.txtPassword.text.toString()
        val validation = authViewModel.validation(emailAddress, password)

        validation.onSuccess {
            findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
            authViewModel.registerUser(username = emailAddress, password = password)

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}