package com.example.laundromate.domain.repository

import com.example.laundromate.data.models.CreateWashingCycleRequest
import com.example.laundromate.data.models.Laundry
import com.example.laundromate.data.models.WashingCycle
import com.example.laundromate.data.models.WashingMachine

interface LaundryRepository {
    suspend fun getLaundries(): List<Laundry>
    suspend fun getWashingMachines(): List<WashingMachine>
    suspend fun createWashingCycle(
        request: CreateWashingCycleRequest
    ): WashingCycle
    suspend fun getWashingCycles(): List<WashingCycle>
}