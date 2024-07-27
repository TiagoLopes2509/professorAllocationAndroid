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
        onSuccess: () -> Unit,
        onError:(Throwable) -> Unit
    ) {
        if (course.name.isBlank()) {
            onError(Throwable("O nome do curso não pode estar vazio."))
            return
        }

        courseService.save(course).enqueue(object : Callback<Any> {
            override fun onResponse(p0: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError(Throwable("Erro: ${response.code()} - ${response.message()}"))
                }
            }

            override fun onFailure(p0: Call<Any>, p1: Throwable) {
                onError(p1)
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
        id: Long,
        onSuccess: (courses: Course?) -> Unit,
        onError: (message: String) -> Unit
    ) {
        courseService.getById(id).enqueue(object : Callback<Course> {
            override fun onResponse(p0: Call<Course>, response: Response<Course>) {
                response.isSuccessful.let {
                    if (it)
                        onSuccess(response.body())
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
        course: Course,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        courseService.update(course.id, course).enqueue(object : Callback<Course> {
            override fun onResponse(call: Call<Course>, response: Response<Course>) {
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError(Throwable("Erro: ${response.code()} - ${response.message()}"))
                }
            }

            override fun onFailure(call: Call<Course>, t: Throwable) {
                onError(t)
            }
        })
    }

    fun deleteCourse(
        id: Long,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    ) {
        courseService.delete(id).enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError("Failed to delete course: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                onError("Error: ${t.message}")
            }
        })
    }

}