import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.example.professorallocation.Screens.Screens
import com.example.professorallocation.ViewModel.DepartmentViewModel
import com.example.professorallocation.model.Course
import com.example.professorallocation.model.Department
import com.example.professorallocation.ui.theme.greenJC

@Composable
fun Department(viewModel: DepartmentViewModel, navController: NavController) {
    val departments by viewModel.departments.collectAsStateWithLifecycle()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screens.AddDepartment.screen)
                },
                containerColor = greenJC,
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Department")
            }
        },
        content = { paddingValues ->
            DepartmentList(
                departments = departments,
                Modifier.padding(paddingValues),
                onDepartmentClick = { department ->
                    department.id?.let { id ->
                        navController.navigate(Screens.EditDepartment.withArgs(id))
                    }
                },
                onDeleteClick = { department ->
                    viewModel.deleteDepartment(department)
                }
            )
        }
    )
}

@Composable
fun DepartmentList(
    departments: List<Department>,
    modifier: Modifier = Modifier,
    onDepartmentClick: (Department) -> Unit,
    onDeleteClick: (Department) -> Unit,
){
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
    ) {
        items(departments) { department ->
            DepartmentItem(
                department = department,
                onClick = {onDepartmentClick(department)},
                onDelete = {onDeleteClick(department) }
            )
        }
    }
}

@Composable
fun DepartmentItem(
    department: Department,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
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
                text = department.name,
                fontSize = 20.sp,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f) // Ensures text takes up available space
            )
            IconButton(onClick = { onDelete() }) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete")
            }
        }
    }
}