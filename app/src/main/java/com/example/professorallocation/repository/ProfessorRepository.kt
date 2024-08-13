package com.example.professorallocation.repository

import android.util.Log
import com.example.professorallocation.model.Professor
import com.example.professorallocation.model.ProfessorWithDepartment
import com.example.professorallocation.service.ProfessorService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfessorRepository(private val professorService: ProfessorService) {

    fun saveProfessor(
        professor: Professor,
        onSuccess: ()-> Unit,
        onError:(Throwable) -> Unit
    ){
        if(professor.name.isBlank() || professor.cpf.isBlank() || professor.departmentId == null){
            onError(Throwable("Não pode conter campos em branco."))
            return
        }

        professorService.save(professor).enqueue(object : Callback<Any> {
            override fun onResponse(p0: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful){
                    onSuccess()
                }
            }

            override fun onFailure(p0: Call<Any>, p1: Throwable) {
                onError(p1)
            }
        })
    }

    fun getProfessors(
        onSuccess: (List<ProfessorWithDepartment>) -> Unit,
        onError: (String) -> Unit
    ) {
        professorService.getAll().enqueue(object : Callback<List<ProfessorWithDepartment>> {
            override fun onResponse(p0: Call<List<ProfessorWithDepartment>>, response: Response<List<ProfessorWithDepartment>>) {
                if (response.isSuccessful) {
                    val professorsResponse = response.body()
                    if (professorsResponse != null) {
                        onSuccess(professorsResponse)
                    } else {
                        onError("Resposta vazia ou inválida ao carregar professores")
                    }
                } else {
                    onError("Erro ao carregar professores: ${response.message()}")
                }

            }

            override fun onFailure(p0: Call<List<ProfessorWithDepartment>>, p1: Throwable) {
                onError("Falha na requisição ao carregar professores: ${p1.message}")
            }

        })
    }

    fun getProfessorById(
        id: Long,
        onSuccess: (professors: Professor?) -> Unit,
        onError: (message: String) -> Unit
    ){
        professorService.getById(id).enqueue(object : Callback<Professor> {
            override fun onResponse(p0: Call<Professor>, response: Response<Professor>) {
                response.isSuccessful.let {
                    if (it)
                        onSuccess(response.body())
                    else
                        onError(response.message())
                }
            }

            override fun onFailure(p0: Call<Professor>, p1: Throwable) {
                p1.message?.let { onError(it) }
            }
        })
    }

    fun updateProfessor(
        professor: Professor,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ){
        professorService.update(professor.id, professor).enqueue(object : Callback<Professor> {
            override fun onResponse(p0: Call<Professor>, response: Response<Professor>) {
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError(Throwable("Erro: ${response.code()} - ${response.message()}"))
                }
            }

            override fun onFailure(p0: Call<Professor>, p1: Throwable) {
                onError(p1)
            }

        })
    }

    fun deleteProfessor(
        id: Long,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    ) {
        professorService.delete(id).enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if(response.isSuccessful){
                    onSuccess()
                } else {
                    onError("Falha ao deletar Professor: ${response.message()}")
                }
            }

            override fun onFailure(p0: Call<Any>, p1: Throwable) {
                onError("Error: ${p1.message}")
            }

        })
    }
}