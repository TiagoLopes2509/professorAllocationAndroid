package com.example.professorallocation

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import com.example.professorallocation.model.CourseModel
import com.example.professorallocation.ui.theme.ProfessorAllocationTheme
import com.example.professorallocation.ui.theme.greenJC
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddCourse() {
    ProfessorAllocationTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Scaffold{
                postCourse()
            }
        }
    }
}

@Composable
fun postCourse(){
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
                postDataUsingRetrofit(ctx,courseName,response)
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

private fun postDataUsingRetrofit(
    ctx: Context,
    courseName: MutableState<TextFieldValue>,
    result: MutableState<String>
){
    val url = "http://192.168.1.96:8080/"
    val retrofit = Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build()
    val retrofitAPI = retrofit.create(RetrofitAPI::class.java)
    val courseModel = CourseModel(courseName.value.text)
    val call: Call<CourseModel?>? = retrofitAPI.postCourse(courseModel)

    call!!.enqueue(object : Callback<CourseModel?> {
        override fun onResponse(call: Call<CourseModel?>, response: Response<CourseModel?>) {
            Toast.makeText(ctx, "Curso Cadastrado", Toast.LENGTH_SHORT).show()

            val course: CourseModel? = response.body()

            val resp = "Status Code: " + response.code() + "\n" + "Course Name : " + course!!.name

            result.value = resp
        }

        override fun onFailure(call: Call<CourseModel?>, t: Throwable) {
            result.value = "Error found is: " + t.message
        }
    })

}