package com.example.professorallocation.Screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import com.example.professorallocation.ViewModel.AllocationViewModel
import com.example.professorallocation.ViewModel.ProfessorViewModel
import com.example.professorallocation.model.Allocation
import com.example.professorallocation.model.Department
import com.example.professorallocation.model.ProfessorWithDepartment
import com.example.professorallocation.ui.theme.greenJC

@Composable
fun Allocation(viewModel: AllocationViewModel, professorViewModel: ProfessorViewModel, navController: NavController) {

    val allocations by viewModel.allocation.collectAsStateWithLifecycle()
    val professors by professorViewModel.professors.collectAsStateWithLifecycle()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screens.AddAllocation.screen)
                },
                containerColor = greenJC,
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Allocation")
            }
        },
        content = { paddingValues ->
            AllocationList(
                allocations = allocations,
                professors = professors,
                Modifier.padding(paddingValues),
                onAllocationClick = { allocation ->
                    allocation.id?.let { id ->
                        navController.navigate(Screens.EditAllocation.withArgs(allocation.id!!))
                    }
                },
                onDeleteClick = { allocation ->
                    viewModel.deleteAllocation(allocation)
                }
            )
        }
    )
}

@Composable
fun AllocationList(
    allocations: List<Allocation>,
    professors: List<ProfessorWithDepartment>,
    modifier: Modifier = Modifier,
    onAllocationClick: (Allocation) -> Unit,
    onDeleteClick: (Allocation) -> Unit,
){
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
    ) {
        items(allocations) { allocation ->
            AllocationItem(
                allocation = allocation,
                professors = professors,
                onClick = {onAllocationClick(allocation)},
                onDelete = {onDeleteClick(allocation) }
            )
        }
    }
}

@Composable
fun AllocationItem(
    allocation: Allocation,
    professors: List<ProfessorWithDepartment>,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val professorName = professors.find { it.id == allocation.professor.id }?.name ?: "Desconhecido"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
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
            Column {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = professorName,
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentWidth(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = allocation.day.toString(),
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentWidth(Alignment.Start)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = "Hora inicial: " + allocation.startHour,
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentWidth(Alignment.Start)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ){

                    Text(
                        text = "Hora final: " + allocation.endHour,
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentWidth(Alignment.Start)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = "Curso: " + allocation.course.name,
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentWidth(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(onClick = { onDelete() },) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete")
                    }
                }
            }
        }
    }
}