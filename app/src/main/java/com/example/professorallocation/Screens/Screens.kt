package com.example.professorallocation.Screens

sealed class Screens(val screen: String){
    data object Home: Screens("home")
    data object Professor: Screens("professor")
    data object Department: Screens("department")
    data object Course: Screens("course")
    data object Allocation: Screens("allocation")
    data object AddCourse: Screens("AddCourse")
    data object AddDepartment: Screens("AddDepartment")
}