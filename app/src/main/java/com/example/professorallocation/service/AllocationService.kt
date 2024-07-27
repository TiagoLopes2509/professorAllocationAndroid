package com.example.professorallocation.service

import com.example.professorallocation.model.Allocation
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AllocationService {

    @GET("allocations")
    fun getAll() : Call<List<Allocation>>

    @GET("allocations/{id}")
    fun getById(@Path("id") id: Int) : Call<Allocation>

    @POST("allocations/{id}")
    fun save(@Body allocation: Allocation) : Call<Allocation>

    @PUT("allocations/{id}")
    fun update(@Path("id") id: Int, @Body allocation: Allocation) : Call<Allocation>

    @DELETE("allocations/{id}")
    fun delete(@Path("id") id: Int): Call<Any>
}