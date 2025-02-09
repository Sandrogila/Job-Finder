package com.example.jobfinder.viewScreen.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.jobfinder.dataClass.JobPosition
import com.example.jobfinder.viewModel.JobViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobDetailsScreen(
    job: JobPosition,
    isCompany: Boolean = false,
    onDelete: () -> Unit = {},
    navController: NavController,
    jobViewModel: JobViewModel = viewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("JobFinder-Detalhes da Vaga", color = Color.White) },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF003366)),
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState()) // Habilita o scroll
        ) {
            // Título da vaga
            Text(
                text = job.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF003366)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Card com informações principais
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    InfoRow(label = "Descrição", value = job.description)
                    InfoRow(label = "Localização", value = job.location ?: "Remoto")
                    InfoRow(label = "Salário", value = job.salary?.toString() ?: "Não informado")
                    InfoRow(label = "Requisitos", value = job.requirements ?: "Nenhum requisito específico")
                    InfoRow(label = "Benefícios", value = job.benefits ?: "Nenhum benefício listado")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Card com informações da empresa
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = Color(0xFFF8F9FA))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Informações da Empresa",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF003366)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    InfoRow(label = "Nome", value = job.company.name)
                    InfoRow(label = "E-mail", value = job.company.email)
                    InfoRow(label = "Status", value = job.status?.toString() ?: "ABERTO")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botões de ação
            if (isCompany) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        onClick = { navController.navigate("EditeJob/${job.id}") },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF003366)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Editar")
                    }

                    OutlinedButton(
                        onClick = onDelete,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Excluir")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { navController.navigate("candidates/${job.id}") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF003366)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Ver Candidatos", color = Color.White)
                }

            } else {
                Button(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF28A745)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Salvar", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Text(text = label, fontSize = 14.sp, color = Color.Gray)
        Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.Black)
        Spacer(modifier = Modifier.height(4.dp))
    }
}
