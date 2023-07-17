package io.madcamp.treasurehunterar.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class UserViewModel constructor(
    savedStateHandle: SavedStateHandle,
    userRepository: UserRepository
) : ViewModel() {
    private val userId: String = savedStateHandle["uid"] ?:
    throw IllegalArgumentException("Missing user ID")

    private val _user = MutableLiveData<User>()
    private val user = _user as LiveData<User>

    init {
        getUser()
    }

    private fun getUser(): LiveData<User> {
        viewModelScope.launch {
            try {
                // Calling the repository is safe as it moves execution off
                // the main thread
                val user = UserApi.retrofitService.getUser(userId)
                _user.value = user
            } catch (error: Exception) {
                // Show error message to user
                Log.d("UserViewModel", error.toString())
            }

        }
        return user
    }
}