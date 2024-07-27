package com.example.professorallocation.repository

import android.util.Log
import com.example.professorallocation.model.Course
import com.example.professorallocation.service.CourseService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CourseRepository(private val courseService: CourseService) {

    private val _courses = MutableStateFlow<List<Course>>(emptyList())
    val courses: StateFlow<List<Course>> get() = _courses

    fun saveCourse(
        course: Course,
        onCall: () -> Unit,
        onError: () -> Unit
    ) {
        courseService.save(course).enqueue(object : Callback<Any> {
            override fun onResponse(p0: Call<Any>, response: Response<Any>) {
                onCall()
            }

            override fun onFailure(p0: Call<Any>, p1: Throwable) {
                onError()
            }
        })
    }

    fun getCourses(
        onSuccess: (List<Course>) -> Unit,
        onError: (String) -> Unit
    ) {
        courseService.getAll().enqueue(object : Callback<List<Course>> {
            override fun onResponse(p0: Call<List<Course>>, response: Response<List<Course>>) {
                if (response.isSuccessful) {
                    val coursesResponse = response.body()
                    if (coursesResponse != null) {
                        onSuccess(coursesResponse)
                    } else {
                        onError("Resposta vazia ou inválida ao carregar cursos")
                    }
                } else {
                    onError("Erro ao carregar cursos: ${response.message()}")
                }

                Log.d("API_Response", "Response code: ${response.code()}")
                Log.d("API_Response", "Response body: ${response.body()}")
            }

            override fun onFailure(call: Call<List<Course>>, t: Throwable) {
                onError("Falha na requisição ao carregar cursos: ${t.message}")
            }
        })
    }

    fun getCourseById(
        id: Int,
        onCall: (courses: Course?) -> Unit,
        onError: (message: String) -> Unit
    ) {
        courseService.getById(id).enqueue(object : Callback<Course> {
            override fun onResponse(p0: Call<Course>, response: Response<Course>) {
                response.isSuccessful.let {
                    if (it)
                        onCall(response.body())
                    else
                        onError(response.message())
                }
            }

            override fun onFailure(p0: Call<Course>, p1: Throwable) {
                p1.message?.let { onError(it) }
            }
        })
    }

    fun updateCourse(
        id: Int,
        course: Course,
        onCall: (courses: Course?) -> Unit,
        onError: (message: String) -> Unit
    ) {
        courseService.update(id, course).enqueue(object : Callback<Course> {
            override fun onResponse(p0: Call<Course>, response: Response<Course>) {
                response.isSuccessful.let {
                    if (it)
                        onCall(response.body())
                    else
                        onError(response.message())
                }
            }

            override fun onFailure(p0: Call<Course>, p1: Throwable) {
                p1.message?.let { onError(it) }
            }
        })
    }

    fun deleteCourse(
        id: Int,
        onCall: () -> Unit,
        onError: (message: String) -> Unit
    ) {
        courseService.delete(id).enqueue(object : Callback<Any> {
            override fun onResponse(p0: Call<Any>, p1: Response<Any>) {
                TODO("Not yet implemented")
            }

            override fun onFailure(p0: Call<Any>, p1: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

}