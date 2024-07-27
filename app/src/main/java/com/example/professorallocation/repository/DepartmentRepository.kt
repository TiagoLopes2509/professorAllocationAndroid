package com.example.professorallocation.repository

import com.example.professorallocation.model.Department
import com.example.professorallocation.service.DepartmentService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DepartmentRepository( private val departmentService: DepartmentService) {

//    fun saveDepartment(
//        department: Department,
//        onCall: () -> Unit,
//        onError: () -> Unit) {
//        departmentService.save(department).enqueue(object : Callback<Any> {
//            override fun onResponse(p0: Call<Any>, p1: Response<Any>) {
//                onCall()
//            }
//
//            override fun onFailure(p0: Call<Any>, p1: Throwable) {
//                onError()
//            }
//
//        })
//    }

    fun getDepartments(
        onCall: (courses: List<Department>?) -> Unit,
        onError: (message: String) -> Unit
    ) {
        departmentService.getAll().enqueue(object : Callback<List<Department>> {
            override fun onResponse(p0: Call<List<Department>>, response: Response<List<Department>>) {
                response.isSuccessful.let {
                    if (it)
                        onCall(response.body())
                    else
                        onError(response.message())
                }
            }

            override fun onFailure(p0: Call<List<Department>>, p1: Throwable) {
                p1.message?.let { onError(it) }
            }

        })
    }

    fun getDepartmentById(
        id: Int,
        onCall: (departments: Department?) -> Unit,
        onError: (message: String) -> Unit
    ) {
        departmentService.getById(id).enqueue(object : Callback<Department> {
            override fun onResponse(p0: Call<Department>, response: Response<Department>) {
                response.isSuccessful.let {
                    if (it)
                        onCall(response.body())
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
        id: Int,
        department: Department,
        onCall: (departments: Department?) -> Unit,
        onError: (message: String) -> Unit
    ) {
        departmentService.update(id, department).enqueue(object : Callback<Department> {
            override fun onResponse(p0: Call<Department>, response: Response<Department>) {
                response.isSuccessful.let {
                    if (it)
                        onCall(response.body())
                    else
                        onError(response.message())
                }
            }

            override fun onFailure(p0: Call<Department>, p1: Throwable) {
                p1.message?.let { onError(it) }
            }

        })

        fun deleteDepartment(
            id: Int,
            onCall: () -> Unit,
            onError: (message: String) -> Unit
        ) {
            departmentService.delete(id).enqueue(object : Callback<Any> {
                override fun onResponse(p0: Call<Any>, p1: Response<Any>) {
                    TODO("Not yet implemented")
                }

                override fun onFailure(p0: Call<Any>, p1: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }
    }
}



