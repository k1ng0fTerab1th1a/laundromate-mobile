package com.example.laundromate.data.repository

import com.example.laundromate.data.api.ApiService
import com.example.laundromate.data.models.LoginRequest
import com.example.laundromate.data.models.RegisterRequest
import com.example.laundromate.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) : AuthRepository {
    override suspend fun login(username: String, password: String): Result<Unit> {
        return try {
            val response = apiService.login(LoginRequest(username, password))
            tokenManager.saveAccessToken(response.access)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(username: String, password: String, email: String?): Result<Unit> {
        return try {
            apiService.register(RegisterRequest(username, password, email, null, null))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        tokenManager.clearTokens()
    }
}