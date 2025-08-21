package com.example.laundromate.data.api

import com.example.laundromate.data.models.*
import retrofit2.http.*

interface ApiService {
    @POST("login/")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("register/customer/")
    suspend fun register(@Body request: RegisterRequest): RegisterRequest

    @GET("laundries/")
    suspend fun getLaundries(): List<LaundryDto>

    @GET("washing-machines/")
    suspend fun getWashingMachines(): List<WashingMachineDto>

    @POST("washing-cycles/")
    suspend fun createWashingCycle(@Body request: CreateWashingCycleRequestDto): WashingCycleDto

    @GET("washing-cycles/")
    suspend fun getWashingCycles(): List<WashingCycleDto>
}

