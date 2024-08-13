package com.example.professorallocation.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.professorallocation.model.Allocation
import com.example.professorallocation.model.AllocationwhithIds
import com.example.professorallocation.model.Professor
import com.example.professorallocation.model.ProfessorWithDepartment
import com.example.professorallocation.repository.AllocationRepository
import com.example.professorallocation.repository.RetrofitConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AllocationViewModel : ViewModel() {

    private val allocationRepository = AllocationRepository(RetrofitConfig.allocationService)

    private val _allocation = MutableStateFlow<List<Allocation>>(emptyList())
    val allocation: StateFlow<List<Allocation>> get() = _allocation

    private val _currentAllocation = MutableStateFlow<Allocation?>(null)
    val currentAllocation: StateFlow<Allocation?> = _currentAllocation.asStateFlow()

    init{
        getAllocations()
    }

    private fun getAllocations(){
        viewModelScope.launch {
            allocationRepository.getAllocations(
                onSuccess = {
                    _allocation.value = it
                },
                onError = {error ->
                    Log.e("allocationViewModel", "Error ao carregar alocações: $error")
                }
            )
        }
    }

    fun refreshAllocations(){
        getAllocations()
    }

    fun deleteAllocation(allocation: Allocation){
        viewModelScope.launch {

            val allocationId = allocation.id

            if(allocationId != null){
                try{
                    allocationRepository.deleteAllocation(
                        id = allocationId,
                        onSuccess = {
                            _allocation.value = _allocation.value.filter { it.id != allocation.id }
                        },
                        onError = {message ->
                            Log.e("allocationViewModel", message)

                        }
                    )
                }catch (e: Exception){
                    Log.e("allocationViewModel", "Failed to delete Allocation ${e.message}", e)
                }
            } else {
                Log.e("allocationViewModel", "Allocation ID is null")
            }
        }
    }

    fun getAllocationById(id: Long){

        if(id != null){
            allocationRepository.getAllocationById(
                id = id,
                onSuccess = { allocation ->
                    _currentAllocation.value = allocation
                },
                onError = {throwable ->
                    Log.e("allocationViewModel", "Falha ao recuperar alocações por ID",)
                }
            )
        }
    }

    fun updateAllocation(
        allocationwhithIds: AllocationwhithIds,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ){
        viewModelScope.launch {
            allocationRepository.updateAllocation(
                allocation = allocationwhithIds,
                onSuccess = onSuccess,
                onError = onError
            )
        }
    }
}