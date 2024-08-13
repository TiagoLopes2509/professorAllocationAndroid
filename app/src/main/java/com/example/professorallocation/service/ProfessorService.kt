package com.example.professorallocation.service

import com.example.professorallocation.model.Professor
import com.example.professorallocation.model.ProfessorWithDepartment
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProfessorService {

    @GET("professors")
    fun getAll() : Call<List<ProfessorWithDepartment>>

    @GET("professors/{id}")
    fun getById(@Path("id") id: Long) : Call<Professor>

    @POST("professors")
    fun save(@Body professor: Professor) : Call<Any>

    @PUT("professors/{id}")
    fun update(@Path("id") id: Long?, @Body professor: Professor) : Call<Professor>

    @DELETE("professors/{id}")
    fun delete(@Path("id") id: Long): Call<Any>
}