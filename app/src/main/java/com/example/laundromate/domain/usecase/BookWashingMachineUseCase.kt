package com.example.laundromate.domain.usecase

import com.example.laundromate.domain.models.CreateWashingCycleRequest
import com.example.laundromate.domain.repository.LaundryRepository
import javax.inject.Inject

class BookWashingMachineUseCase @Inject constructor(
    private val repository: LaundryRepository
) {
    suspend operator fun invoke(
        machineId: Int,
        mode: String,
        temperature: Int,
        bookedFor: String
    ) = repository.createWashingCycle(
        CreateWashingCycleRequest(mode, temperature, bookedFor, machineId)
    )
}
