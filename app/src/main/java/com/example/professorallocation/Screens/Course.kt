import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.professorallocation.ViewModel.CourseViewModel
import com.example.professorallocation.ui.theme.ProfessorAllocationTheme
import com.example.professorallocation.model.Course
import com.example.professorallocation.repository.CourseRepository
import com.example.professorallocation.repository.RetrofitConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Course(viewModel: CourseViewModel) {
    val courseService = RetrofitConfig.courseService
    val repository = remember { CourseRepository(courseService) }
    val courses by viewModel._courses.collectAsState()

//    LaunchedEffect(true) {
//        loadCourses(repository){}
//    }

    ProfessorAllocationTheme{
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Scaffold{

                CourseList(courses)
            }
        }
    }
}

private fun CoroutineScope.loadCourses(repository: CourseRepository, onSuccess: (List<Course>) -> Unit) {
    launch {
        try {
            repository.getCourses(
                onSuccess = { fetchedCourses ->
                    onSuccess(fetchedCourses)
                },
                onError = { error ->
                    println("Erro ao carregar cursos: $error")
                }
            )
        } catch (e: Exception) {
            println("Erro ao carregar cursos: ${e.message}")
        }
    }
}

@Composable
fun CourseList(courses: List<Course>) {

    LazyColumn {
        items(courses){ course ->
            CourseItem(course = course)
       }
    }

}

@Composable
fun CourseItem(course : Course){
    Text(
        text = course.name,
        style = MaterialTheme.typography.bodyMedium
    )
}
