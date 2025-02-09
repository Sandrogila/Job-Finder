package com.example.jobfinder.viewScreen.dashboard

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.jobfinder.R
import com.example.jobfinder.Services.AuthRepository
import com.example.jobfinder.utils.SessionManager
import com.example.jobfinder.viewModel.AuthViewModel
import com.example.jobfinder.viewModel.AuthViewModelFactory
import com.example.jobfinder.viewModel.JobViewModel
import com.example.jobfinder.viewScreen.cards.JobCard
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDashboardScreen(
    navController: NavHostController,
    jobViewModel: JobViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(AuthRepository(LocalContext.current)))
) {
    val context = LocalContext.current as Activity

    BackHandler {
        context.moveTaskToBack(true) // Minimiza o app
    }


    val jobs by jobViewModel.jobs.collectAsState()
    val message by jobViewModel.message.collectAsState()

    LaunchedEffect(Unit) {
        jobViewModel.loadJobs()
    }

    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Filtra as vagas com base no que foi digitado (pesquisa por título ou descrição)
    val filteredJobs = if (searchQuery.text.isBlank()) {
        jobs
    } else {
        jobs.filter { job ->
            job.title.contains(searchQuery.text, ignoreCase = true) ||
                    job.description.contains(searchQuery.text, ignoreCase = true)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                navController = navController,
                onClose = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Job Finder", color = Color.White) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF003366)),
                    actions = {
                        IconButton(onClick = { /* Ação do ícone de busca, se necessário */ }) {
                            Icon(imageVector = Icons.Filled.Search, contentDescription = "Search", tint = Color.White)
                        }
                    }
                )
            },
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = "Bem-vindo ao Painel do Usuário",
                        fontSize = 24.sp,
                        color = Color(0xFF003366),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Buscar Vagas") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(imageVector = Icons.Filled.Search, contentDescription = "Search Icon")
                        },
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(0xFF003366),
                            unfocusedBorderColor = Color.Gray
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(filteredJobs) { job ->
                            JobCard(
                                job = job,
                                onClick = { navController.navigate("jobDetails/${job.id}") }
                            )
                        }
                    }

                    if (message != null) {
                        Text(text = message!!, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
                    }
                }
            }
        )
    }
}
@Composable
fun DrawerContent(navController: NavHostController, onClose: () -> Unit,
                  authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(AuthRepository(LocalContext.current))))
{
    val contexte = LocalContext.current
    val sessionManager = remember { SessionManager(contexte) }
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(Color(0xFF003366))
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "User Profile",
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
        }

        NavigationDrawerItem(
            label = { Text("Meu Perfil") },
            selected = false,
            onClick = {
                navController.navigate("profile")
                onClose()
            },
            modifier = Modifier.padding(vertical = 8.dp)
        )
        NavigationDrawerItem(
            label = { Text("Minhas Candidaturas") },
            selected = false,
            onClick = {
                navController.navigate("applications")
                onClose()
            },
            modifier = Modifier.padding(vertical = 8.dp)
        )
        NavigationDrawerItem(
            label = { Text("Logout") },
            selected = false,
            onClick = {
                sessionManager.logout()
                authViewModel.logout() // Limpa o token
                navController.navigate("login") {
                    popUpTo("user_dashboard") { inclusive = true }
                }
                onClose()
            },
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}
