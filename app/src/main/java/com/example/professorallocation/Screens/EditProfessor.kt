package com.example.professorallocation.Screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.professorallocation.ViewModel.DepartmentViewModel
import com.example.professorallocation.ViewModel.ProfessorViewModel
import com.example.professorallocation.model.Professor

@Composable
fun EditProfessor(
    onProfessorAdded: () -> Unit,
    viewModel: ProfessorViewModel,
    departmentViewModel: DepartmentViewModel,
    navController: NavController
) {
    val navBackStackEntry = navController.currentBackStackEntry
    val professorId = navBackStackEntry?.arguments?.getLong("professorId")
    val departmentId = navBackStackEntry?.arguments?.getLong("departmentId")

    LaunchedEffect(professorId) {
        professorId?.let {
            viewModel.getProfessorById(professorId)
        }
    }

    val professor by viewModel.currentProfessor.collectAsStateWithLifecycle()

    val departments by departmentViewModel.departments.collectAsStateWithLifecycle()
    val selectedDepartment = departments.find { it.id == departmentId }

    professor?.let { p ->
        if (selectedDepartment != null) {
            EditProfessorForm(
                onProfessorAdded = onProfessorAdded,
                professor = p,
                departmentViewModel = DepartmentViewModel(),
                onSave = { updatedProfessor ->
                    viewModel.updateProfessor(
                        professor = updatedProfessor,
                        onSuccess = {
                            navController.popBackStack()
                            viewModel.refreshProfessors()
                        },
                        onError = { throwable ->
                            Log.e("EditProfessorScreen", "Erro ao atualizar professor: $throwable")
                        }
                    )
                }
            )
        }
    } ?: run {
        Text("Carregando professor...")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfessorForm(
    onProfessorAdded: () -> Unit,
    professor: Professor,
    departmentViewModel: DepartmentViewModel,
    onSave: (Professor) -> Unit
) {
    val nameState = remember { mutableStateOf(professor.name) }
    val cpfState = remember { mutableStateOf(professor.cpf) }
    val selectedDepartmentIdState = remember { mutableStateOf(professor.departmentId) }
    val expanded = remember { mutableStateOf(false) }

    val departments by departmentViewModel.departments.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = nameState.value,
            onValueChange = { nameState.value = it },
            label = { Text("Nome") },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = cpfState.value,
            onValueChange = { cpfState.value = it },
            label = { Text("CPF") },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = expanded.value,
            onExpandedChange = { expanded.value = !expanded.value },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            TextField(
                value = departments.find { it.id == selectedDepartmentIdState.value }?.name ?: "Selecionar Departamento",
                onValueChange = {},
                label = { Text("Departamento") },
                readOnly = true,
                trailingIcon = {
                    Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
                },
                modifier = Modifier
                    .menuAnchor()
                    .clickable { expanded.value = !expanded.value }
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                departments.forEach { department ->
                    DropdownMenuItem(
                        text = { Text(text = department.name) },
                        onClick = {
                            selectedDepartmentIdState.value = department.id!!
                            expanded.value = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                onSave(professor.copy(
                    name = nameState.value,
                    cpf = cpfState.value,
                    departmentId = selectedDepartmentIdState.value
                ))
                onProfessorAdded()
            },
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
        ) {
            Text("Salvar")
        }
    }
}
