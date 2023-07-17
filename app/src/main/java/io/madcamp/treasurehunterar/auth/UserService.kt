package io.madcamp.treasurehunterar.auth

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private const val BASE_URL = "http://172.10.5.178"
private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

interface UserService {
    @GET("/api/users/{collectionNum}")
    suspend fun getUser(@Path("id") id: String): User

    @GET("/api/users")
    suspend fun getUsers(): List<User>
}

object UserApi {
    val retrofitService : UserService by lazy {
        retrofit.create(UserService::class.java)
    }
}