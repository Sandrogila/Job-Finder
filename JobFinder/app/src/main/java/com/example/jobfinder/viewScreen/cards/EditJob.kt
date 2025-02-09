package com.example.jobfinder.viewScreen.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jobfinder.dataClass.JobPosition
import com.example.jobfinder.dataClass.JobPositionDTO
import com.example.jobfinder.viewModel.JobViewModel
import kotlinx.coroutines.launch
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditJobScreen(
    job: JobPosition?, // Se for null, estamos criando uma nova vaga
    onSave: () -> Unit,
    onDelete: (() -> Unit)? = null,
    onCancel: () -> Unit,
    jobViewModel: JobViewModel = viewModel()
) {
    // Estados dos campos de entrada
    var title by remember { mutableStateOf(job?.title ?: "") }
    var description by remember { mutableStateOf(job?.description ?: "") }
    var salaryText by remember { mutableStateOf(job?.salary?.toString() ?: "") }
    var location by remember { mutableStateOf(job?.location ?: "") }
    var requirements by remember { mutableStateOf(job?.requirements ?: "") }
    var benefits by remember { mutableStateOf(job?.benefits ?: "") }

    // Estados para validação
    var titleError by remember { mutableStateOf(false) }
    var descriptionError by remember { mutableStateOf(false) }
    var salaryError by remember { mutableStateOf(false) }

    var isSaving by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("JobFinder", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF003366))
            )
        },
        content = { paddingValues ->
            // Fundo suave para a tela
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF2F2F2))
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState()) // Habilita o scroll
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Gerenciar Vaga",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color(0xFF1877F2)
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        titleError = it.isBlank()
                    },
                    label = { Text("Título") },
                    isError = titleError,
                    modifier = Modifier.fillMaxWidth()
                )
                if (titleError) {
                    Text("O título é obrigatório", color = Color.Red, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = {
                        description = it
                        descriptionError = it.isBlank()
                    },
                    label = { Text("Descrição") },
                    isError = descriptionError,
                    modifier = Modifier.fillMaxWidth()
                )
                if (descriptionError) {
                    Text("A descrição é obrigatória", color = Color.Red, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = salaryText,
                    onValueChange = {
                        salaryText = it
                        salaryError = it.toBigDecimalOrNull() == null
                    },
                    label = { Text("Salário") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = salaryError,
                    modifier = Modifier.fillMaxWidth()
                )
                if (salaryError) {
                    Text("Informe um valor numérico válido", color = Color.Red, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Localização") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = requirements,
                    onValueChange = { requirements = it },
                    label = { Text("Requisitos") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = benefits,
                    onValueChange = { benefits = it },
                    label = { Text("Benefícios") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            // Validações
                            titleError = title.isBlank()
                            descriptionError = description.isBlank()
                            salaryError = salaryText.toBigDecimalOrNull() == null

                            if (!titleError && !descriptionError && !salaryError) {
                                val salary = salaryText.toBigDecimalOrNull() ?: BigDecimal.ZERO
                                val jobDto = JobPositionDTO(title, description, salary, location, requirements, benefits)
                                isSaving = true
                                coroutineScope.launch {
                                    try {
                                        if (job == null) {
                                            jobViewModel.createJob(jobDto)
                                        } else {
                                            jobViewModel.updateJob(job.id, jobDto)
                                        }
                                        onSave()
                                    } finally {
                                        isSaving = false
                                    }
                                }
                            }
                        },
                        enabled = !isSaving,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1877F2))
                    ) {
                        Text(if (job == null) "Criar" else "Salvar", fontSize = 16.sp, color = Color.White)
                    }

                    if (job != null && onDelete != null) {
                        Button(
                            onClick = {
                                isSaving = true
                                coroutineScope.launch {
                                    try {
                                        jobViewModel.deleteJob(job.id)
                                        onDelete()
                                    } finally {
                                        isSaving = false
                                    }
                                }
                            },
                            enabled = !isSaving,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) {
                            Text("Excluir", fontSize = 16.sp, color = Color.White)
                        }
                    }

                    Button(onClick = onCancel, enabled = !isSaving) {
                        Text("Cancelar", fontSize = 16.sp)
                    }
                }
            }
        }
    )
}
