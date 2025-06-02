package com.example.laundromate.data.api

import com.example.laundromate.data.models.*
import retrofit2.http.*

interface ApiService {
    @POST("login/")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("register/customer/")
    suspend fun register(@Body request: RegisterRequest): RegisterRequest

    @GET("laundries/")
    suspend fun getLaundries(): List<Laundry>

    @GET("washing-machines/")
    suspend fun getWashingMachines(): List<WashingMachine>

    @POST("washing-cycles/")
    suspend fun createWashingCycle(@Body request: CreateWashingCycleRequest): WashingCycle

    @GET("washing-cycles/")
    suspend fun getWashingCycles(): List<WashingCycle>
}