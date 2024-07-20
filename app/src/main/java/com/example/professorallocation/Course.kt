import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.professorallocation.RetrofitAPI
import com.example.professorallocation.model.Course
import com.example.professorallocation.ui.theme.ProfessorAllocationTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Course() {
    ProfessorAllocationTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Scaffold{
                getCourse()
            }
        }
    }
}

@Composable
fun getCourse() {
    val url = "http://192.168.1.96:8080/"
    val retrofit = Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build()
    val retrofitAPI = retrofit.create(RetrofitAPI::class.java)
    val data = retrofitAPI.getCourses()

    data!!.enqueue(object : Callback<List<Course>?> {
        override fun onResponse(call: Call<List<Course>?>, response: Response<List<Course>?>) {

        }

        override fun onFailure(call: Call<List<Course>?>, t: Throwable) {

        }
    })
}

