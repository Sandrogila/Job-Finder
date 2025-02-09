package com.example.jobfinder.viewScreen.dashboard

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.jobfinder.R
import com.example.jobfinder.Services.AuthRepository
import com.example.jobfinder.database.entities.UserEntity
import com.example.jobfinder.utils.SessionManager
import com.example.jobfinder.viewScreen.cards.JobCard
import com.example.jobfinder.viewModel.AuthViewModel
import com.example.jobfinder.viewModel.AuthViewModelFactory
import com.example.jobfinder.viewModel.JobViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyDashboardScreen(
    navController: NavHostController,
    jobViewModel: JobViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(AuthRepository(LocalContext.current))
    )
) {
    val activity = LocalContext.current as Activity
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    // Configura o BackHandler para minimizar o app quando pressionar "voltar"
    BackHandler {
        activity.moveTaskToBack(true)
    }

    // Coleta os estados do JobViewModel
    val jobs by jobViewModel.jobs.collectAsState()
    val message by jobViewModel.message.collectAsState()

    // Carrega as vagas quando a tela é iniciada
    LaunchedEffect(Unit) {
        jobViewModel.loadJobs()
    }

    // Recupera o usuário logado (UserEntity) usando o SessionManager
    val user: UserEntity? = sessionManager.getUser()
    // Filtra as vagas: exibe apenas as vagas cuja empresa (job.company.email) seja igual ao email do usuário logado
    val filteredJobs = if (user != null && user.email.isNotBlank()) {
        jobs.filter { it.company.email == user.email }
    } else {
        jobs
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("JobFinder", color = Color.White) },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF003366)),
                actions = {
                    IconButton(onClick = {
                        sessionManager.logout()
                        authViewModel.logout()
                        navController.navigate("login") {
                            popUpTo("company_dashboard") { inclusive = true }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ExitToApp,
                            contentDescription = "Logout",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("EditeJob") },
                containerColor = Color(0xFF003366)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Adicionar Vaga",
                    tint = Color.White
                )
            }
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                // Cabeçalho e informações de boas-vindas
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                                contentDescription = "Ícone da Empresa",
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    "Bem-vindo ao JobFinder!",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Color(0xFF003366)
                                )
                                Text(
                                    "Gerencie suas vagas com facilidade.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.DarkGray
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "📌 Funcionalidades:",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF003366)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("🔹 Crie e publique novas vagas.", fontSize = 14.sp, color = Color.DarkGray)
                    Text("🔹 Edite e exclua suas vagas ativas.", fontSize = 14.sp, color = Color.DarkGray)
                    Text("🔹 Visualize candidatos interessados.", fontSize = 14.sp, color = Color.DarkGray)
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Lista de vagas filtradas pela empresa logada
                if (filteredJobs.isEmpty()) {
                    item {
                        Text(
                            "⚠ Nenhuma vaga publicada. Clique no botão '+' para adicionar.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                } else {
                    items(filteredJobs) { job ->
                        JobCard(
                            job = job,
                            isCompany = true,
                            onEdit = { navController.navigate("EditeJob/${job.id}") },
                            onDelete = { jobViewModel.deleteJob(job.id) },
                            onViewCandidates = { navController.navigate("candidates/${job.id}") },
                            onClick = { navController.navigate("jobDetails/${job.id}") }
                        )
                    }
                }

                // Exibe mensagem de erro/sucesso (se houver)
                item {
                    message?.let { msg ->
                        Text(text = msg, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
                    }
                }
            }
        }
    )
}
