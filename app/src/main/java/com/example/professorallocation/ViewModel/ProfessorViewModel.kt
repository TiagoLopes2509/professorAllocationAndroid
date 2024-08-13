package com.example.professorallocation.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.professorallocation.model.Professor
import com.example.professorallocation.model.ProfessorWithDepartment
import com.example.professorallocation.repository.ProfessorRepository
import com.example.professorallocation.repository.RetrofitConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfessorViewModel : ViewModel() {

    private val professorRepository = ProfessorRepository(RetrofitConfig.ProfessorService)

    private val _professors = MutableStateFlow<List<ProfessorWithDepartment>>(emptyList())
    val professors: StateFlow<List<ProfessorWithDepartment>> get() = _professors

    private val _currentProfessor = MutableStateFlow<Professor?>(null)
    val currentProfessor: StateFlow<Professor?> = _currentProfessor.asStateFlow()

    init {
        getProfessors()
    }

    private fun getProfessors(){
        viewModelScope.launch {
            professorRepository.getProfessors(
                onSuccess = {
                    _professors.value = it
                },
                onError = {error ->
                    Log.e("professorViewModel", "Error loading professors: $error")
                }
            )
        }
    }

    fun refreshProfessors(){
        getProfessors()
    }

    fun deleteProfessor(professor: ProfessorWithDepartment){
        viewModelScope.launch {

            val professorId = professor.id

            if(professorId != null){
                try{
                    professorRepository.deleteProfessor(
                        id = professorId,
                        onSuccess = {
                            _professors.value = _professors.value.filter { it.id != professor.id }
                        },
                        onError = {message ->
                            Log.e("professorViewModel", message)

                        }
                    )
                }catch (e: Exception){
                    Log.e("professorViewModel", "Failed to delete professor ${e.message}", e)
                }
            } else {
                Log.e("professorViewModel", "Professor ID is null")
            }
        }
    }

    fun getProfessorById(id: Long){

        if(id != null){
            professorRepository.getProfessorById(
                id = id,
                onSuccess = { professor ->
                    _currentProfessor.value = professor
                },
                onError = {throwable ->
                    Log.e("professorViewModel", "Falha ao recuperar dados",)
                }
            )
        }
    }

    fun updateProfessor(
        professor: Professor,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ){
        viewModelScope.launch {
            professorRepository.updateProfessor(
                professor = professor,
                onSuccess = onSuccess,
                onError = onError
            )
        }
    }
}