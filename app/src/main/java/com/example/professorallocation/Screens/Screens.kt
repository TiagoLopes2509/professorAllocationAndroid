package com.example.professorallocation.Screens

sealed class Screens(val screen: String){
    data object Home: Screens("home")
    data object Professor: Screens("professor")
    data object AddProfessor: Screens("AddProfessor")
    data object EditProfessor: Screens("EditProfessor/{professorId}/{departmentId}"){
        fun withArgs(professorId: Long?, departmentId: Long?) ="EditProfessor/${professorId ?: "0"}/${departmentId ?: "0"}"
    }
    data object Course: Screens("course")
    data object AddCourse: Screens("AddCourse")
    data object EditCourse: Screens("EditCourse/{id}"){
        fun withArgs(id: Long) ="EditCourse/$id"
    }
    data object Allocation: Screens("allocation")
    data object AddAllocation: Screens("AddAllocation")
    data object EditAllocation: Screens("EditAllocation/{id}"){
        fun withArgs(id: Long) = "EditAllocation/$id"
    }
    data object Department: Screens("department")
    data object AddDepartment: Screens("AddDepartment")
    data object EditDepartment: Screens("EditDepartment/{id}"){
        fun withArgs(id: Long) = "EditDepartment/$id"
    }
}