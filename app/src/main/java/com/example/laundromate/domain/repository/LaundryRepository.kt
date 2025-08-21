package com.example.laundromate.domain.repository

import com.example.laundromate.domain.models.CreateWashingCycleRequest
import com.example.laundromate.domain.models.Laundry
import com.example.laundromate.domain.models.WashingCycle
import com.example.laundromate.domain.models.WashingMachine

interface LaundryRepository {
    suspend fun getLaundries(): List<Laundry>
    suspend fun getWashingMachines(): List<WashingMachine>
    suspend fun createWashingCycle(
        request: CreateWashingCycleRequest
    ): WashingCycle
    suspend fun getWashingCycles(): List<WashingCycle>
}
