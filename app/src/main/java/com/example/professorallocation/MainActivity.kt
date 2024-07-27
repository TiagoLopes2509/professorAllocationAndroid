package com.example.professorallocation

import Course
import Department
import EditCourse
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.professorallocation.Screens.AddCourse
import com.example.professorallocation.Screens.Allocation
import com.example.professorallocation.Screens.Home
import com.example.professorallocation.Screens.Professor
import com.example.professorallocation.Screens.Screens
import com.example.professorallocation.ViewModel.CourseViewModel
import com.example.professorallocation.ui.theme.ProfessorAllocationTheme
import com.example.professorallocation.ui.theme.greenJC
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProfessorAllocationTheme {
              Surface(
                  //modifier = Modifier.fillMaxSize(),
                  color = MaterialTheme.colorScheme.background
              ) {
                    NavDrawer(viewModel = CourseViewModel())
              }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavDrawer(viewModel: CourseViewModel){
    val navigationController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val context = LocalContext.current.applicationContext

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            ModalDrawerSheet {
                Box(modifier = Modifier
                    .background(greenJC)
                    .fillMaxWidth()
                    .height(80.dp)) {

                    Text(
                        text = "Unifafire",
                        fontSize = 50.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.run { padding(16.dp)}
                    )
                }
                HorizontalDivider()
                NavigationDrawerItem(label = { Text(text = "Home", color = greenJC) },
                    selected = false,
                    icon = { Icon(imageVector = Icons.Default.Home, contentDescription = "home", tint = greenJC)},
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        navigationController.navigate(Screens.Home.screen){
                            popUpTo(0)
                        }
                    })

                NavigationDrawerItem(label = { Text(text = "Professor", color = greenJC) },
                    selected = false,
                    icon = { Icon(imageVector = Icons.Default.Person, contentDescription = "professor", tint = greenJC)},
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        navigationController.navigate(Screens.Professor.screen){
                            popUpTo(0)
                        }
                    })

                NavigationDrawerItem(label = { Text(text = "Department", color = greenJC) },
                    selected = false,
                    icon = { Icon(imageVector = Icons.Default.AccountBox, contentDescription = "department", tint = greenJC)},
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        navigationController.navigate(Screens.Department.screen){
                            popUpTo(0)
                        }
                    })

                NavigationDrawerItem(label = { Text(text = "Course", color = greenJC) },
                    selected = false,
                    icon = { Icon(imageVector = Icons.Default.Create, contentDescription = "course", tint = greenJC)},
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        navigationController.navigate(Screens.Course.screen){
                            popUpTo(0)
                        }
                    })

                NavigationDrawerItem(label = { Text(text = "Allocation", color = greenJC) },
                    selected = false,
                    icon = { Icon(imageVector = Icons.Default.Face, contentDescription = "allocation", tint = greenJC)},
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        navigationController.navigate(Screens.Allocation.screen){
                            popUpTo(0)
                        }
                    })


            }
        },
        ) {
        Scaffold(
            topBar = {
                val coroutineScope = rememberCoroutineScope()
                TopAppBar(title = { Text(text = "Professor Allocation") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = greenJC,
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    ),
                    navigationIcon = {
                        IconButton(onClick = {
                            coroutineScope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(
                                Icons.Rounded.Menu, contentDescription = "MenuButton"
                            )
                        }
                    },
                )
            },
            content = {paddingValues ->
                NavHost(
                    navController = navigationController,
                    startDestination = Screens.Home.screen,
                    Modifier.padding(paddingValues)
                ){
                    composable(Screens.Home.screen){ Home() }
                    composable(Screens.Professor.screen){ Professor() }
                    composable(Screens.Department.screen){ Department() }
                    composable(Screens.Allocation.screen){ Allocation() }
                    composable(Screens.Course.screen){ Course(viewModel, navigationController)}
                    composable(Screens.EditCourse.screen){EditCourse(viewModel, navigationController)}
                    composable(Screens.AddCourse.screen){ AddCourse(onCourseAdded = {
                        navigationController.popBackStack()
                        viewModel.refreshCourses()
                    })}
                }
            }
        )
    }
}