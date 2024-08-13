package com.example.professorallocation.repository

import android.util.Log
import com.example.professorallocation.model.Course
import com.example.professorallocation.model.Department
import com.example.professorallocation.service.DepartmentService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DepartmentRepository( private val departmentService: DepartmentService) {

    fun saveDepartment(
        department: Department,
        onSuccess: () -> Unit,
        onError:(Throwable) -> Unit
    ) {
        if (department.name.isBlank()){
            onError(Throwable("O nome do Departamento não pode ser vazio."))
            return
        }

        departmentService.save(department).enqueue(object : Callback<Any> {
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

    fun getDepartments(
        onSuccess: (List<Department>) -> Unit,
        onError: (String) -> Unit
    ) {
        departmentService.getAll().enqueue(object : Callback<List<Department>> {
            override fun onResponse(p0: Call<List<Department>>, response: Response<List<Department>>) {
                if (response.isSuccessful) {
                    val departmentsResponse = response.body()
                    if (departmentsResponse != null) {
                        onSuccess(departmentsResponse)
                    } else {
                        onError("Resposta vazia ou inválida ao carregar cursos")
                    }
                } else {
                    onError("Erro ao carregar departamentos: ${response.message()}")
                }
            }

            override fun onFailure(p0: Call<List<Department>>, p1: Throwable) {
                onError("Falha na requisição ao carregar departamentos: ${p1.message}")
            }

        })
    }

    fun getDepartmentById(
        id: Long,
        onSuccess: (departments: Department?) -> Unit,
        onError: (message: String) -> Unit
    ) {
        departmentService.getById(id).enqueue(object : Callback<Department> {
            override fun onResponse(p0: Call<Department>, response: Response<Department>) {
                response.isSuccessful.let {
                    if (it)
                        onSuccess(response.body())
                    else
                        onError(response.message())
                }
            }

            override fun onFailure(p0: Call<Department>, p1: Throwable) {
                p1.message?.let { onError(it) }
            }
        })
    }

    fun updateDepartment(
        department: Department,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        departmentService.update(department.id, department).enqueue(object : Callback<Department> {
            override fun onResponse(p0: Call<Department>, response: Response<Department>) {
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError(Throwable("Erro: ${response.code()} - ${response.message()}"))
                }
            }

            override fun onFailure(p0: Call<Department>, p1: Throwable) {
                onError(p1)
            }

        })
    }

    fun deleteDepartment(
        id: Long,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    ) {
        departmentService.delete(id).enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if(response.isSuccessful){
                    onSuccess()
                } else {
                    onError("Falha ao deletar departamento: ${response.message()}")
                }
            }

            override fun onFailure(p0: Call<Any>, p1: Throwable) {
                onError("Error: ${p1.message}")
            }

        })
    }
}



