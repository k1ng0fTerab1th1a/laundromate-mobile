package com.example.laundromate.data.repository

import com.example.laundromate.data.api.ApiService
import com.example.laundromate.data.models.*
import com.example.laundromate.domain.repository.LaundryRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LaundryRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : LaundryRepository {
    override suspend fun getLaundries() = apiService.getLaundries()
    override suspend fun getWashingMachines() = apiService.getWashingMachines()
    override suspend fun createWashingCycle(request: CreateWashingCycleRequest) = apiService.createWashingCycle(request)
    override suspend fun getWashingCycles() = apiService.getWashingCycles()
}