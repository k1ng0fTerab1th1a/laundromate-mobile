package com.example.laundromate.domain.models

data class Laundry(
    val id: Int,
    val name: String,
    val address: String,
    val pricePerKg: String,
    val dynamicPricing: Boolean
)

data class WashingMachine(
    val id: Int,
    val status: String,
    val maxLoad: String,
    val laundry: Int
)

data class WashingCycle(
    val id: Int? = null,
    val mode: String,
    val temperature: Int,
    val status: String? = null,
    val bookedFor: String?,
    val washingMachine: Int,
    val user: Int? = null
)

data class CreateWashingCycleRequest(
    val mode: String,
    val temperature: Int,
    val bookedFor: String,
    val washingMachine: Int
)

