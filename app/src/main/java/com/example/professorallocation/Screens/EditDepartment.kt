package com.example.professorallocation.Screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.professorallocation.ViewModel.CourseViewModel
import com.example.professorallocation.ViewModel.DepartmentViewModel
import com.example.professorallocation.model.Course
import com.example.professorallocation.model.Department

@Composable
fun EditDepartment(
    onDepartmentAdded: () -> Unit,
    viewModel: DepartmentViewModel,
    navController: NavController
) {
    val navBackStackEntry = navController.currentBackStackEntry
    val departmentId = navBackStackEntry?.arguments?.getLong("id")

    LaunchedEffect(departmentId) {
        departmentId?.let{
            viewModel.getDepartmentById(departmentId)
        }
    }

    val department by viewModel.currentDepartment.collectAsStateWithLifecycle()

    department?.let { d ->
        EditDepartmentForm(
            onDepartmentAdded,
            department = d,
            onSave = { updatedDepartment ->
                viewModel.updateDepartment(
                    department = updatedDepartment,
                    onSuccess = {
                        navController.popBackStack()
                        viewModel.refreshDepartments()
                    },
                    onError = { throwable ->
                        Log.e("EditDepartmentScreen", "Erro ao atualizar departamento")
                    }
                )
            }
        )
    } ?: run {
        Text("Carregando departamento...")
    }
}

@Composable
fun EditDepartmentForm(
    onDepartmentAdded: () -> Unit,
    department: Department,
    onSave: (Department) -> Unit
) {
    val nameState = remember { mutableStateOf(department.name) }

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
            label = { Text("Nome do Departamento") },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
            singleLine = true,
        )

        Button(
            onClick = {
                onSave(department.copy(name = nameState.value))
                onDepartmentAdded()
            },
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
        ) {
            Text("Salvar")
        }
    }
}
