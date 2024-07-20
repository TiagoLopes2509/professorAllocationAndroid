package com.example.professorallocation.model

import java.sql.Time
import java.time.DayOfWeek

data class Allocation (
    var id: Long,
    var day: DayOfWeek,
    var startHour: Time,
    var endHour: Time,
    var professor: Professor,
    var course: Course
)