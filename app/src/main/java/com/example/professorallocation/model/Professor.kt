package com.example.professorallocation.model

data class Professor (
    var id: Long? = null,
    var name: String,
    var cpf: String,
    var departmentId: Long
)

data class ProfessorWithDepartment(
    var id: Long? = null,
    var name: String,
    var cpf: String,
    var department: Department
)