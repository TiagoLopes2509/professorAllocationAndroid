package com.example.professorallocation.service

import com.example.professorallocation.model.Course
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CourseService {

    @GET("courses")
    fun getAll(): Call<List<Course>>

    @POST("courses")
    fun save(@Body course: Course): Call<Any>

    @GET("courses/{id}")
    fun getById(@Path("id") id: Long): Call<Course>

    @PUT("courses/{id}")
    fun update(@Path("id") id: Long?, @Body course: Course): Call<Course>

    @DELETE("courses/{id}")
    fun delete(@Path("id") id: Long): Call<Any>
}