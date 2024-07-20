package com.example.professorallocation

import com.example.professorallocation.model.Course
import com.example.professorallocation.model.Department
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RetrofitAPI {

    @POST("courses")
    fun postCourse(@Body courseModel: Course?): Call<Course?>?

    @GET("courses")
    fun getCourses():  Call<List<Course>?>?

    @POST("departments")
    fun postdeparment(@Body department: Department?): Call<Department?>?

    @GET("departments")
    fun getDeparments(): Call<List<Department>?>?
}