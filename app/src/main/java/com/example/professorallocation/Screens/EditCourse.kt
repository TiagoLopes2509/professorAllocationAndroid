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
import androidx.compose.runtime.collectAsState
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
import com.example.professorallocation.model.Course

@Composable
fun EditCourse(
    onCourseAdded: () -> Unit,
    viewModel: CourseViewModel,
    navController: NavController
) {
    val navBackStackEntry = navController.currentBackStackEntry
    val courseId = navBackStackEntry?.arguments?.getLong("id")


    LaunchedEffect(courseId) {
        courseId?.let{
            viewModel.getCourseById(courseId)
        }
    }

    val course by viewModel.currentCourse.collectAsStateWithLifecycle()

    course?.let { c ->
        EditCourseForm(
            onCourseAdded,
            course = c,
            onSave = { updatedCourse ->
                viewModel.updateCourse(
                    course = updatedCourse,
                    onSuccess = {
                        navController.popBackStack()
                        viewModel.refreshCourses()
                    },
                    onError = { throwable ->
                       Log.e("EditCourseScreen", "Erro ao atualizar curso")
                    }
                )
            }
        )
    } ?: run {
        Text("Carregando curso...")
    }
}

@Composable
fun EditCourseForm(
    onCourseAdded: () -> Unit,
    course: Course,
    onSave: (Course) -> Unit
) {
    val nameState = remember { mutableStateOf(course.name) }

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
            label = { Text("Nome do Curso") },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
            singleLine = true,
        )

        Button(
            onClick = {
                onSave(course.copy(name = nameState.value))
                onCourseAdded()
            },
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
        ) {
            Text("Salvar")
        }
    }
}
