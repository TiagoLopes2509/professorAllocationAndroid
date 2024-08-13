package com.example.professorallocation.model

import java.time.DayOfWeek

data class Allocation (
    var id: Long? = null,
    var day: DayOfWeek,
    var startHour: String,
    var endHour: String,
    var professor: ProfessorWithDepartment,
    var course: Course
)

data class AllocationwhithIds(
    var id: Long? = null,
    var day: com.example.professorallocation.Utils.DayOfWeek,
    var startHour: String,
    var endHour: String,
    var professorId: Long,
    var courseId: Long
)