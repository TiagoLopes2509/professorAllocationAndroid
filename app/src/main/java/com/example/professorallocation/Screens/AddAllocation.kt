package com.example.professorallocation.Screens

import android.app.TimePickerDialog
import android.content.Context
import android.widget.TimePicker
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.material3.Icon
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.professorallocation.Utils.DayOfWeek
import com.example.professorallocation.ViewModel.CourseViewModel
import com.example.professorallocation.ViewModel.ProfessorViewModel
import com.example.professorallocation.model.Allocation
import com.example.professorallocation.model.AllocationwhithIds
import com.example.professorallocation.model.Course
import com.example.professorallocation.model.ProfessorWithDepartment
import com.example.professorallocation.repository.AllocationRepository
import com.example.professorallocation.repository.RetrofitConfig
import com.example.professorallocation.ui.theme.greenJC
import java.util.Calendar

@Composable
fun AddAllocation(onAllocationAdded: () -> Unit, courseViewModel: CourseViewModel, professorViewModel: ProfessorViewModel){

    val courses by courseViewModel.courses.collectAsState()
    val professors by professorViewModel.professors.collectAsState()

    postAllocation(
        onAllocationAdded = onAllocationAdded,
        courses = courses,
        professors = professors
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun postAllocation(
    onAllocationAdded: () -> Unit,
    courses: List<Course>,
    professors: List<ProfessorWithDepartment>
) {
    val ctx = LocalContext.current
    val selectedDay = remember { mutableStateOf(DayOfWeek.SUNDAY) }
    val startHour = remember { mutableStateOf("Select Start Time") }
    val endHour = remember { mutableStateOf("Select End Time") }
    val selectedCourse = remember { mutableStateOf<Course?>(null) }
    val selectedProfessor = remember { mutableStateOf<ProfessorWithDepartment?>(null) }
    val response = remember { mutableStateOf("") }

    val dayDropdownExpanded = remember { mutableStateOf(false) }
    val courseDropdownExpanded = remember { mutableStateOf(false) }
    val professorDropdownExpanded = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Alocar Professor",
            color = greenJC,
            fontSize = 20.sp,
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = dayDropdownExpanded.value,
            onExpandedChange = { dayDropdownExpanded.value = !dayDropdownExpanded.value }
        ) {
            TextField(
                value = selectedDay.value.name,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { Icon(Icons.Filled.ArrowDropDown, contentDescription = null) },
                modifier = Modifier
                    .menuAnchor()
                    .clickable { dayDropdownExpanded.value = !dayDropdownExpanded.value }
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = dayDropdownExpanded.value,
                onDismissRequest = { dayDropdownExpanded.value = false }
            ) {
                DayOfWeek.values().forEach { day ->
                    DropdownMenuItem(
                        text = {Text(text = day.name)},
                        onClick = {
                        selectedDay.value = day
                        dayDropdownExpanded.value = false
                    })
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        TimePickerField(
            label = "Start Time",
            time = startHour.value,
            onTimeSelected = { time ->
                startHour.value = time
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // End Time
        TimePickerField(
            label = "End Time",
            time = endHour.value,
            onTimeSelected = { time ->
                endHour.value = time
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = courseDropdownExpanded.value,
            onExpandedChange = { courseDropdownExpanded.value = !courseDropdownExpanded.value }
        ) {
            TextField(
                value = selectedCourse.value?.name ?: "Selecione o Curso",
                onValueChange = {},
                readOnly = true,
                trailingIcon = { Icon(Icons.Filled.ArrowDropDown, contentDescription = null) },
                modifier = Modifier
                    .menuAnchor()
                    .clickable { courseDropdownExpanded.value = !courseDropdownExpanded.value }
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = courseDropdownExpanded.value,
                onDismissRequest = { courseDropdownExpanded.value = false }
            ) {
                courses.forEach { course ->
                    DropdownMenuItem(
                        text = {Text(text = course.name)},
                        onClick = {
                        selectedCourse.value = course
                        courseDropdownExpanded.value = false
                    })
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = professorDropdownExpanded.value,
            onExpandedChange = { professorDropdownExpanded.value = !professorDropdownExpanded.value }
        ) {
            TextField(
                value = selectedProfessor.value?.name ?: "Selecione o Professor",
                onValueChange = {},
                readOnly = true,
                trailingIcon = { Icon(Icons.Filled.ArrowDropDown, contentDescription = null) },
                modifier = Modifier
                    .menuAnchor()
                    .clickable {
                        professorDropdownExpanded.value = !professorDropdownExpanded.value
                    }
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = professorDropdownExpanded.value,
                onDismissRequest = { professorDropdownExpanded.value = false }
            ) {
                professors.forEach { professor ->
                    DropdownMenuItem(
                        text = {Text(text =professor.name)},
                        onClick = {
                        selectedProfessor.value = professor
                        professorDropdownExpanded.value = false
                    })
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                // Your logic to save the allocation
                val allocation = AllocationwhithIds(
                    day = selectedDay.value,
                    startHour = startHour.value,
                    endHour = endHour.value,
                    courseId = selectedCourse.value?.id ?: 0L,
                    professorId = selectedProfessor.value?.id ?: 0L
                )

                postDataAllocation(ctx, allocation, response) {
                    onAllocationAdded()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Alocar Professor", modifier = Modifier.padding(8.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = response.value,
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

    }
}

private fun postDataAllocation(
    ctx: Context,
    allocation: AllocationwhithIds,
    result: MutableState<String>,
    onAllocationAdded: () -> Unit
){
    val allocationRepository = AllocationRepository(RetrofitConfig.allocationService)

    allocationRepository.saveAllocation(
        allocation = allocation,
        onSuccess = {
            result.value = "Professor alocado com sucesso!"
            Toast.makeText(ctx, "Professor Alocado", Toast.LENGTH_SHORT).show()
            onAllocationAdded()
        },
        onError = {throwable ->
            result.value = "Erro ao alocar professor: ${throwable.message}"}
    )
}

@Composable
fun TimePickerField(
    label: String,
    time: String,
    onTimeSelected: (String) -> Unit
) {
    val context = LocalContext.current
    var showTimePicker by remember { mutableStateOf(false) }
    var selectedTime by remember { mutableStateOf(time) }

    if (showTimePicker) {
        val currentTime = Calendar.getInstance()
        TimePickerDialog(
            context,
            { _: TimePicker, hourOfDay: Int, minute: Int ->
                val formattedTime = String.format("%02d:%02d", hourOfDay, minute)
                onTimeSelected(formattedTime)
                selectedTime = formattedTime
            },
            currentTime.get(Calendar.HOUR_OF_DAY),
            currentTime.get(Calendar.MINUTE),
            true
        ).show()
        showTimePicker = false
    }

    TextField(
        value = selectedTime,
        onValueChange = {},
        readOnly = true,
        label = { Text(label) },
        trailingIcon = { Icon(Icons.Filled.ArrowDropDown, contentDescription = null) },
        modifier = Modifier
            .clickable { showTimePicker = true }
            .fillMaxWidth()
            .padding(16.dp)
    )
}