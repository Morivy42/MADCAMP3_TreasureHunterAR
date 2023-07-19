package io.madcamp.treasurehunterar.auth

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

sealed interface UserUiState {
    data class Success(val Users: String) : UserUiState
    object Error : UserUiState
    object Loading : UserUiState
}

class UserViewModel : ViewModel() {
    var userUiState : UserUiState by mutableStateOf(UserUiState.Loading)
        private set

    init {
        getUsers()
    }
    private fun getUsers() {
        viewModelScope.launch {
            userUiState = UserUiState.Loading
            userUiState = try {
                val userList = UserApi.retrofitService.getUsers()
                UserUiState.Success(
                    "Success: ${userList.size} Mars photos retrieved"
                )
            } catch (e: IOException) {
                Log.d("GetUserException", e.toString())
                UserUiState.Error
            } catch (e: HttpException) {
                Log.d("GetUserException", e.toString())
                UserUiState.Error
            }
        }
    }
}

