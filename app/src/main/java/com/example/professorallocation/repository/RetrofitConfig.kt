package com.example.professorallocation.repository

import com.example.professorallocation.service.AllocationService
import com.example.professorallocation.service.CourseService
import com.example.professorallocation.service.DepartmentService
import com.example.professorallocation.service.ProfessorService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitConfig {

    val retrofit = Retrofit.Builder()
        .baseUrl("http:/192.168.1.16:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val allocationService = retrofit.create(AllocationService::class.java)
    val courseService = retrofit.create(CourseService::class.java)
    val departmentService = retrofit.create(DepartmentService::class.java)
    val ProfessorService = retrofit.create(ProfessorService::class.java)
}