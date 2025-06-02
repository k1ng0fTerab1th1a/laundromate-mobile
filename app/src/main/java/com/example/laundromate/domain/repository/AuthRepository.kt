package com.example.laundromate.domain.repository

interface AuthRepository {
    suspend fun login(username: String, password: String): Result<Unit>
    suspend fun register(username: String, password: String, email: String?): Result<Unit>
    suspend fun logout()
}