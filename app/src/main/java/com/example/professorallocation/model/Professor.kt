package com.example.professorallocation.model

data class Professor (
    var id: Long,
    var name: String,
    var cpf: String,
    var department: Department
)