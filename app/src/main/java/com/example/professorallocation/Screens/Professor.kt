package com.example.professorallocation.Screens
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.professorallocation.ViewModel.DepartmentViewModel
import com.example.professorallocation.ViewModel.ProfessorViewModel
import com.example.professorallocation.model.Department
import com.example.professorallocation.model.ProfessorWithDepartment
import com.example.professorallocation.ui.theme.greenJC

@Composable
fun Professor(viewModel: ProfessorViewModel, departmentViewModel: DepartmentViewModel, navController: NavController) {
    val professors by viewModel.professors.collectAsStateWithLifecycle()
    val departments by departmentViewModel.departments.collectAsStateWithLifecycle()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screens.AddProfessor.screen)
                },
                containerColor = greenJC,
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Professor")
            }
        },
        content = { paddingValues ->
            ProfessorList(
                professors = professors,
                departments = departments,
                Modifier.padding(paddingValues),
                onProfessorClick = { professor ->
                    professor.id?.let { id ->
                        navController.navigate(Screens.EditProfessor.withArgs(professor.id, professor.department.id))
                    }
                },
                onDeleteClick = { professor ->
                    viewModel.deleteProfessor(professor)
                }
            )
        }
    )
}

@Composable
fun ProfessorList(
    professors: List<ProfessorWithDepartment>,
    departments: List<Department>,
    modifier: Modifier = Modifier,
    onProfessorClick: (ProfessorWithDepartment) -> Unit,
    onDeleteClick: (ProfessorWithDepartment) -> Unit,
){
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
    ) {
        items(professors) { professor ->
            ProfessorItem(
                professor = professor,
                departments = departments,
                onClick = {onProfessorClick(professor)},
                onDelete = {onDeleteClick(professor) }
            )
        }
    }
}

@Composable
fun ProfessorItem(
    professor: ProfessorWithDepartment,
    departments: List<Department>,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val departmentName = departments.find { it.id == professor.department.id }?.name ?: "Desconhecido"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp) // Space between items
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background,),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = professor.name,
                fontSize = 20.sp,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
                    .wrapContentWidth(Alignment.Start)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = departmentName,
                fontSize = 15.sp,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
                    .wrapContentWidth(Alignment.Start)
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = { onDelete() }) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete")
            }
        }
    }
}