package io.madcamp.treasurehunterar.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository constructor(
    private val userService: UserService
) {
    suspend fun getUserById(id: String): User {
        return userService.getUser(id)
    }

//    fun getUsers(): LiveData<List<User>> {
//        val users = MutableLiveData<List<User>>()
//
//        userService.getUsers().enqueue(object : Callback<List<User>> {
//            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
//                if (response.isSuccessful) {
//                    users.value = response.body()
//                }
//            }
//
//            override fun onFailure(call: Call<List<User>>, t: Throwable) {
//                Log.e("MyApp", "Failed to fetch users", t)
//            }
//        })
//
//        return users
//    }
}