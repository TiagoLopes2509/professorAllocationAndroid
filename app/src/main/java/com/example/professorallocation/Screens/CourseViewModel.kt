package com.example.professorallocation.Screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.professorallocation.model.Course
import com.example.professorallocation.repository.CourseRepository
import com.example.professorallocation.repository.RetrofitConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CourseViewModel : ViewModel() {

    private val courseRepository = CourseRepository(RetrofitConfig.courseService)

    private val _courses = MutableStateFlow<List<Course>>(emptyList())
    val courses: StateFlow<List<Course>> get() = _courses

    init {
        getCourses()
    }

    private fun getCourses() {
        viewModelScope.launch {
            _courses.value = listOf(
                Course(id = 1, name = "Curso 1"),
                Course(id = 2, name = "Curso 2"),
                Course(id = 2, name = "Curso 2"),
                Course(id = 2, name = "Curso 2"),
                Course(id = 2, name = "Curso 2"),
                Course(id = 2, name = "Curso 2"),
                Course(id = 2, name = "Curso 2"),
                Course(id = 3, name = "Curso 3")
            )
//            courseRepository.getCourses(
//                onSuccess = {
//                    _courses.value = it
//                    Log.d("CourseViewModel", "Courses loaded successfully")
//                },
//                onError = { error ->
//                    Log.e("CourseViewModel", "Error loading courses: $error")
//                }
//            )
        }
    }
}