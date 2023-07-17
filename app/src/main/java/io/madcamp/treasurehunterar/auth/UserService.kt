package io.madcamp.treasurehunterar.auth

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface UserService {
    @GET("/users/{id}")
    suspend fun getUser(@Path("id") id: String): User
}

private const val BASE_URL = ""
private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

object UserApi {
    val retrofitService : UserService by lazy {
        retrofit.create(UserService::class.java)
    }
}