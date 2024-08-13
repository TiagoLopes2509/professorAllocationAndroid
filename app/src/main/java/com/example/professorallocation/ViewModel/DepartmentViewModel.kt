package com.example.professorallocation.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.professorallocation.model.Course
import com.example.professorallocation.model.Department
import com.example.professorallocation.repository.DepartmentRepository
import com.example.professorallocation.repository.RetrofitConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DepartmentViewModel : ViewModel(){

    private val departmentRepository = DepartmentRepository(RetrofitConfig.departmentService)

    private val _departments = MutableStateFlow<List<Department>>(emptyList())
    val departments: StateFlow<List<Department>> get() = _departments

    private val _currentDepartment = MutableStateFlow<Department?>(null)
    val currentDepartment: StateFlow<Department?> = _currentDepartment.asStateFlow()

    init {
        getDeparments()
    }

    private fun getDeparments(){
        viewModelScope.launch {
            departmentRepository.getDepartments(
                onSuccess = {
                    _departments.value = it
                },
                onError = {error ->
                    Log.e("departmentViewModel", "Error loading departments: $error")
                }
            )
        }
    }

    fun refreshDepartments(){
        getDeparments()
    }

    fun deleteDepartment(department: Department){
        viewModelScope.launch {

            val departmentId = department.id

            if(departmentId != null){
                try{
                    departmentRepository.deleteDepartment(
                        id = departmentId,
                        onSuccess = {
                            _departments.value = _departments.value.filter { it.id != department.id }
                        },
                        onError = {message ->
                            Log.e("DepartmentViewModel", message)

                        }
                    )
                }catch (e: Exception){
                    Log.e("DepartmentViewModel", "Failed to delete department ${e.message}", e)
                }
            } else {
                Log.e("DepartmentViewModel", "Department ID is null")
            }
        }
    }

    fun getDepartmentById(id: Long){

        if(id != null){
            departmentRepository.getDepartmentById(
                id = id,
                onSuccess = { department ->
                    _currentDepartment.value = department
                },
                onError = {throwable ->
                    Log.e("DepartmentViewModel", "Falha ao recuperar dados",)
                }
            )
        }
    }

    fun updateDepartment(
        department: Department,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ){
        viewModelScope.launch {
            departmentRepository.updateDepartment(
                department = department,
                onSuccess = onSuccess,
                onError = onError
            )
        }
    }
}