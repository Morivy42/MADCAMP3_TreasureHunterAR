package io.madcamp.treasurehunterar.collection

import io.madcamp.treasurehunterar.auth.UserService
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface CollectionService {
    @GET("collection/{id}")
    suspend fun getCollectionById(@Path("id") id: String): Collection
}

private const val BASE_URL = ""
private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

object CollectionApi {
    val retrofitService : CollectionService by lazy {
        retrofit.create(CollectionService::class.java)
    }
}