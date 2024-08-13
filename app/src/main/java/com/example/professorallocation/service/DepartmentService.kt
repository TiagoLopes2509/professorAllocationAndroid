package com.example.professorallocation.service

import com.example.professorallocation.model.Department
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface DepartmentService {

    @GET("departments")
    fun getAll() : Call<List<Department>>

    @POST("departments")
    fun save(@Body department: Department) : Call<Any>

    @GET("departments/{id}")
    fun getById(@Path("id") id: Long) : Call<Department>

    @PUT("departments/{id}")
    fun update(@Path("id") id: Long?, @Body department: Department) : Call<Department>

    @DELETE("departments/{id}")
    fun delete(@Path("id") id: Long): Call<Any>
}