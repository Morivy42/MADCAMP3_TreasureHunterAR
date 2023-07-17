package io.madcamp.treasurehunterar.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("user_id")
    val id: Int,
    @SerialName("user_name")
    val username: String,
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String,
)
