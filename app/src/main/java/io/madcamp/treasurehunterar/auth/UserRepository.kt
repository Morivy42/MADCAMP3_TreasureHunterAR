package io.madcamp.treasurehunterar.auth

class UserRepository constructor(
    private val userService: UserService
) {
    suspend fun getUserById(id: String): User {
        return userService.getUser(id)
    }
}