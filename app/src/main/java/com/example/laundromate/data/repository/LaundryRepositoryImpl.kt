package com.example.laundromate.data.repository

import com.example.laundromate.data.api.ApiService
import com.example.laundromate.data.models.*
import com.example.laundromate.domain.models.CreateWashingCycleRequest
import com.example.laundromate.domain.repository.LaundryRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LaundryRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : LaundryRepository {
    override suspend fun getLaundries() =
        apiService.getLaundries().map { it.toDomain() }

    override suspend fun getWashingMachines() =
        apiService.getWashingMachines().map { it.toDomain() }

    override suspend fun createWashingCycle(request: CreateWashingCycleRequest) =
        apiService.createWashingCycle(request.toDto()).toDomain()

    override suspend fun getWashingCycles() =
        apiService.getWashingCycles().map { it.toDomain() }
}

