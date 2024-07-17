import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.professorallocation.RetrofitAPI
import com.example.professorallocation.model.CourseModel
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

    data!!.enqueue(object : Callback<List<CourseModel>?> {
        override fun onResponse(call: Call<List<CourseModel>?>, response: Response<List<CourseModel>?>) {

        }

        override fun onFailure(call: Call<List<CourseModel>?>, t: Throwable) {

        }
    })
}

