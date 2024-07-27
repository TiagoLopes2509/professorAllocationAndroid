import android.annotation.SuppressLint
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.professorallocation.Screens.Screens
import com.example.professorallocation.ViewModel.CourseViewModel
import com.example.professorallocation.model.Course


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Course(viewModel: CourseViewModel, navController: NavController) {
    val courses by viewModel.courses.collectAsStateWithLifecycle()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screens.AddCourse.screen)
                }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Course")
            }
        },
        content = { paddingValues ->
            CourseList(
                courses = courses,
                Modifier.padding(paddingValues),
                onCourseClick = { course ->
                    course.id?.let { id ->
                        navController.navigate(Screens.EditCourse.withArgs(id))
                    }
                },
                onDeleteClick = { course ->
                    viewModel.deleteCourse(course)
                }
            )
        }
    )
}

@Composable
fun CourseList(
    courses: List<Course>,
    modifier: Modifier = Modifier,
    onCourseClick: (Course) -> Unit,
    onDeleteClick: (Course) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp) // Add padding around the list
    ) {
        items(courses) { course ->
            CourseItem(
                course = course,
                onClick = {onCourseClick(course)},
                onDelete = {onDeleteClick(course) }
            )
        }
    }
}

@Composable
fun CourseItem(
    course: Course,
    onClick: () -> Unit,
    onDelete: () -> Unit
    ) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp) // Space between items
            .clickable { onClick() },
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
                text = course.name,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f) // Ensures text takes up available space
            )
            IconButton(onClick = { onDelete() }) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete")
            }
        }
    }
}

