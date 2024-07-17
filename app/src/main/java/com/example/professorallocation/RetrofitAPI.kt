package com.example.professorallocation

import com.example.professorallocation.model.CourseModel
import com.example.professorallocation.model.DepartmentModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RetrofitAPI {

    @POST("courses")
    fun postCourse(@Body courseModel: CourseModel?): Call<CourseModel?>?

    @GET("courses")
    fun getCourses():  Call<List<CourseModel>?>?

    @POST("departments")
    fun postdeparment(@Body departmentModel: DepartmentModel?): Call<DepartmentModel?>?

    @GET("departments")
    fun getDeparments(): Call<List<DepartmentModel>?>?
}