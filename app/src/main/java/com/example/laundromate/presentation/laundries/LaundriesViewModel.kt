package com.example.laundromate.presentation.laundries

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laundromate.domain.models.Laundry
import com.example.laundromate.domain.models.WashingMachine
import com.example.laundromate.domain.repository.LaundryRepository
import com.example.laundromate.domain.usecase.BookWashingMachineUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class LaundriesViewModel @Inject constructor(
    private val laundryRepository: LaundryRepository,
    private val bookWashingMachineUseCase: BookWashingMachineUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LaundriesUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadLaundries()
    }

    fun loadLaundries() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val laundries = laundryRepository.getLaundries()
                val machines = laundryRepository.getWashingMachines()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    laundries = laundries,
                    washingMachines = machines
                )
            } catch (e: Exception) {
                Log.e("LaundriesViewModel", "Error in loadLaundries()", e)
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun bookMachine(machineId: Int, mode: String, temperature: Int, bookedFor: String) {
        viewModelScope.launch {
            try {
                bookWashingMachineUseCase(machineId, mode, temperature, bookedFor)
                _uiState.value = _uiState.value.copy(bookingSuccess = true)
                loadLaundries()
            } catch (e: Exception) {
                Log.e("LaundriesViewModel", "Error booking machine", e)
                var errorMsg = e.message
                if (e is HttpException) {
                    val errorBody = e.response()?.errorBody()?.string()
                    if (!errorBody.isNullOrEmpty()) {
                        try {
                            val json = JSONObject(errorBody)
                            errorMsg = when {
                                json.has("non_field_errors") -> json.getJSONArray("non_field_errors")[0].toString()
                                json.has("detail") -> json.getString("detail")
                                else -> errorBody
                            }
                        } catch (_: Exception) {

                        }
                    }
                }
                _uiState.value = _uiState.value.copy(error = errorMsg)
            }
        }
    }

    fun clearBookingSuccess() {
        _uiState.value = _uiState.value.copy(bookingSuccess = false)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class LaundriesUiState(
    val isLoading: Boolean = false,
    val laundries: List<Laundry> = emptyList(),
    val washingMachines: List<WashingMachine> = emptyList(),
    val error: String? = null,
    val bookingSuccess: Boolean = false
)
