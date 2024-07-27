package com.example.professorallocation.Screens

sealed class Screens(val screen: String){
    data object Home: Screens("home")
    data object Professor: Screens("professor")
    data object Department: Screens("department")
    data object Course: Screens("course")
    data object AddCourse: Screens("AddCourse")
    data object EditCourse: Screens("EditCourse/{courseId}"){
        fun withArgs(id: Long) ="EditCourse/$id"
    }
    data object Allocation: Screens("allocation")
    data object AddDepartment: Screens("AddDepartment")
}