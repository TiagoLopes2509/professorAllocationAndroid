package com.example.professorallocation.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.professorallocation.model.Course
import com.example.professorallocation.repository.CourseRepository
import com.example.professorallocation.repository.RetrofitConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CourseViewModel : ViewModel(){

    private val courseRepository =  CourseRepository(RetrofitConfig.courseService)

    private val _courses = MutableStateFlow<List<Course>>(emptyList())
    val courses: StateFlow<List<Course>> get() = _courses

    private val _currentCourse = MutableStateFlow<Course?>(null)
    val currentCourse: StateFlow<Course?> = _currentCourse.asStateFlow()


    init {
       getCourses()
    }

    private fun getCourses(){
        viewModelScope.launch {
            courseRepository.getCourses(
                onSuccess = {
                    _courses.value = it
                },
                onError = { error ->
                    Log.e("CourseViewModel", "Error loading courses: $error")
                    }
            )
        }
    }

    fun refreshCourses(){
        getCourses()
    }

    fun deleteCourse(course: Course) {
        viewModelScope.launch {

            val courseId = course.id

            if(courseId != null){
                try{
                    courseRepository.deleteCourse(
                        id = courseId,
                        onSuccess = {
                            _courses.value = _courses.value.filter { it.id != course.id }
                                    },
                        onError = { message ->
                            Log.e("CourseViewModel", message)
                        }
                    )
                }catch (e: Exception) {
                    Log.e("CourseViewModel", "Failed to delete course ${e.message}", e)
                }
            } else{
                Log.e("CourseViewModel", "Course ID is null")
            }
        }
    }

    fun getCourseById(id: Long?) {

        if(id != null){
            courseRepository.getCourseById(
                id = id,
                onSuccess = { course ->
                    _currentCourse.value = course
                },
                onError = {throwable ->
                    Log.e("CourseViewModel", "Falha ao recuperar dados",)
                }
            )
        }
    }

    fun updateCourse(
        course: Course,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        viewModelScope.launch {
            courseRepository.updateCourse(
                course = course,
                onSuccess = onSuccess,
                onError = onError
            )
        }
    }
}