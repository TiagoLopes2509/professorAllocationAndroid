package com.example.professorallocation.Screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.professorallocation.ViewModel.DepartmentViewModel
import com.example.professorallocation.ViewModel.ProfessorViewModel
import com.example.professorallocation.model.Department
import com.example.professorallocation.model.Professor
import com.example.professorallocation.repository.ProfessorRepository
import com.example.professorallocation.repository.RetrofitConfig
import com.example.professorallocation.ui.theme.greenJC

@Composable
fun AddProfessor(onProfessorAdded: () -> Unit, departmentViewModel: DepartmentViewModel)
{

    val departments by departmentViewModel.departments.collectAsState()

    postProfessor(onProfessorAdded, departments)

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun postProfessor(
    onProfessorAdded: () -> Unit,
    departments: List<Department>,
) {
    val ctx = LocalContext.current
    val name = remember { mutableStateOf(TextFieldValue()) }
    val cpf = remember { mutableStateOf(TextFieldValue()) }
    val selectedDepartment = remember { mutableStateOf<Department?>(null) }
    val response = remember { mutableStateOf("") }
    val expanded = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Cadastrar Professor",
            color = greenJC,
            fontSize = 20.sp,
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = name.value,
            onValueChange = { name.value = it },
            placeholder = { Text(text = "Digite o nome do Professor") },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = cpf.value,
            onValueChange = { cpf.value = it },
            placeholder = { Text(text = "Digite o CPF do Professor") },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        if(departments.isNotEmpty()) {
            ExposedDropdownMenuBox(
                expanded = expanded.value,
                onExpandedChange = { expanded.value = !expanded.value },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                TextField(
                    value = selectedDepartment.value?.name ?: "Selecione o Departamento",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { Icon(Icons.Filled.ArrowDropDown, contentDescription = null) },
                    modifier = Modifier
                        .menuAnchor()
                        .clickable { expanded.value = !expanded.value }
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded.value,
                    onDismissRequest = { expanded.value = false }
                ) {
                    departments.forEach { department ->
                        DropdownMenuItem(
                            text = { Text(text = department.name) },
                            onClick = {
                                selectedDepartment.value = department
                                expanded.value = false
                            }
                        )
                    }
                }
            }
        } else {
            Text(text = "Nenhum departamento disponível", color = Color.Red)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val professor = Professor(
                    name = name.value.text,
                    cpf = cpf.value.text,
                    departmentId = selectedDepartment.value?.id ?: 0L
                )
                postDataProfessor(ctx, professor, response) {
                    onProfessorAdded()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Cadastrar Novo Professor", modifier = Modifier.padding(8.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = response.value,
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

private fun postDataProfessor(
    ctx: Context,
    professor: Professor,
    result: MutableState<String>,
    onProfessorAdded: () -> Unit
){
    val professorRepository = ProfessorRepository(RetrofitConfig.ProfessorService)

    professorRepository.saveProfessor(
        professor = professor,
        onSuccess = {
            result.value = "Professor Cadastrado com Sucesso!"
            Toast.makeText(ctx, "Professor Cadastrado", Toast.LENGTH_SHORT).show()
            onProfessorAdded()
        },
        onError = { throwable ->
            result.value = "Erro ao cadastrar professor: ${throwable.message}"
        }
    )
}