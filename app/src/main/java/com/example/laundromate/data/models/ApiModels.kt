package com.example.laundromate.data.models

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val access: String,
    val refresh: String
)

data class RegisterRequest(
    val username: String,
    val password: String,
    val email: String?,
    @SerializedName("first_name") val firstName: String?,
    @SerializedName("last_name") val lastName: String?
)

data class Laundry(
    val id: Int,
    val name: String,
    val address: String,
    @SerializedName("price_per_kg") val pricePerKg: String,
    @SerializedName("dynamic_pricing") val dynamicPricing: Boolean
)

data class WashingMachine(
    val id: Int,
    val status: String,
    @SerializedName("max_load") val maxLoad: String,
    @SerializedName("laundry") val laundry: Int
)

data class WashingCycle(
    val id: Int? = null,
    val mode: String,
    val temperature: Int,
    val status: String? = null,
    @SerializedName("booked_for") val bookedFor: String?,
    @SerializedName("washing_machine") val washingMachine: Int,
    val user: Int? = null
)

data class CreateWashingCycleRequest(
    val mode: String,
    val temperature: Int,
    @SerializedName("booked_for") val bookedFor: String,
    @SerializedName("washing_machine") val washingMachine: Int
)