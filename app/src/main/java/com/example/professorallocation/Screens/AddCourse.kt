package com.example.professorallocation.Screens

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.professorallocation.model.Course
import com.example.professorallocation.repository.CourseRepository
import com.example.professorallocation.repository.RetrofitConfig
import com.example.professorallocation.ui.theme.greenJC

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddCourse(onCourseAdded: () -> Unit) {

    postCourse(onCourseAdded)

}

@Composable
fun postCourse(onCourseAdded: () -> Unit){
    val ctx = LocalContext.current
    val courseName = remember{
        mutableStateOf(TextFieldValue())
    }
    val response = remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight()
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Cadastrar Curso",
            color = greenJC,
            fontSize = 20.sp,
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(5.dp))

        TextField(
            value = courseName.value,
            onValueChange = {courseName.value = it},
            placeholder = { Text(text = "Digite o nome do Curso")},
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
            singleLine = true,
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                val course = Course(name = courseName.value.text)
                postDataCourse(ctx, course, response) {
                    onCourseAdded()
                }

            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)

        ) {
            Text(text = "Cadastrar Novo Curso", modifier = Modifier.padding(8.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = response.value,
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold, modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

private fun postDataCourse(
    ctx: Context,
    course: Course,
    result: MutableState<String>,
    onCourseAdded: () -> Unit
){
    val courseRepository =  CourseRepository(RetrofitConfig.courseService)

    courseRepository.saveCourse(
        course = course,
        onSuccess = {
            result.value = "Curso Cadastrado com Sucesso!"
            Toast.makeText(ctx, "Curso Cadastrado", Toast.LENGTH_SHORT).show()
            onCourseAdded()
        },
        onError = { throwable ->
            result.value =  "Error loading courses: ${throwable}"
        }
    )

}