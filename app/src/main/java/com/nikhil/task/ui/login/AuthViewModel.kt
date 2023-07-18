package com.nikhil.task.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikhil.task.repository.UserCredentials
import com.nikhil.task.repository.UserPreferenceRepository
import com.nikhil.task.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userPreferenceRepository: UserPreferenceRepository,
) : ViewModel() {

    private val _authLiveData = MutableLiveData<NetworkResult<String>>()
    val authLiveData get() = _authLiveData

    fun loginUser(username: String, password: String) {
        viewModelScope.launch {
            _authLiveData.postValue(NetworkResult.Loading())
            val userData = userPreferenceRepository.userPreferencesFlow.first()
            val result = if (username != userData.username ){
                NetworkResult.Error(message = "Enter Correct Username")

            }else if (password != userData.password) {
                NetworkResult.Error(message = "Enter Correct Password")
            } else {
                NetworkResult.Success(data = "Sucessfully Logged in...")
            }

            _authLiveData.postValue(result)

        }
    }

    fun registerUser(username: String, password: String) {
        viewModelScope.launch {
            _authLiveData.postValue(NetworkResult.Loading())

            userPreferenceRepository.updateUserCredentials(
                UserCredentials(
                    username = username,
                    password = password
                )
            )
            _authLiveData.postValue(NetworkResult.Success("Successfully Registered"))
        }
    }

    fun validation(username: String, password: String): Result<String> {
        if (username.isEmpty() || password.isEmpty()) {
            return Result.failure(
                exception = Throwable(message = "Username or Password Field Empty")
            )
        }
        if (password.length < 8) {
            return Result.failure(
                exception = Throwable(message = "Password Length is less than 8")
            )
        }
        return Result.success("Logging in ...")
    }

    fun isRegistered(): Boolean= runBlocking {
        val userData = userPreferenceRepository.userPreferencesFlow.first()
        !userData.username.isNullOrEmpty()
    }

    fun clearCredentials() {
        viewModelScope.launch {
            userPreferenceRepository.updateUserCredentials(
                userData = UserCredentials(
                    username = "",
                    password = "",
                )
            )
        }
    }
}

