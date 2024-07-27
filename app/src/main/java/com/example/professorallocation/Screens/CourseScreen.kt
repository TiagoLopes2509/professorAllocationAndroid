import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.professorallocation.model.Course
import com.example.professorallocation.Screens.CourseViewModel

@Composable
fun CourseScreen(viewModel: CourseViewModel) {
    val courses by viewModel.courses.collectAsState()

    CourseList(courses)
}

@Composable
fun CourseList(courses: List<Course>) {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(Color.Yellow)) {
        items(courses) { course ->
            CourseItem(course = course)
        }
    }
}

@Composable
fun CourseItem(course: Course) {
    Text(
        text = course.name,
        style = MaterialTheme.typography.bodyMedium
    )
}
