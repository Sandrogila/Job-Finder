package com.example.jobfinder.viewScreen.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.jobfinder.dataClass.JobPosition
import com.example.jobfinder.viewModel.JobViewModel

@Composable
fun JobCard(
    job: JobPosition,
    isCompany: Boolean = false,

    onDelete: () -> Unit = {},
    onViewCandidates: () -> Unit = {},
    onEdit: () -> Unit = {},
    onClick: () -> Unit = {},
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)) // Fundo mais elegante
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = job.title,
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFF003366) // Azul profissional
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Empresa: ${job.company.name}",
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = job.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Localização: ${job.location ?: "Remoto"}",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(12.dp))

            if (isCompany) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = onEdit,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF003366))
                    ) {
                        Icon(imageVector = Icons.Filled.Edit, contentDescription = "Editar")
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Editar")
                    }

                    OutlinedButton(
                        onClick = onDelete,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                    ) {
                        Icon(imageVector = Icons.Filled.Delete, contentDescription = "Excluir")
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Excluir")
                    }
                }
/*
                Button(
                    onClick = onViewCandidates,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF003366))
                ) {
                    Icon(imageVector = Icons.Filled.Person, contentDescription = "Ver Candidatos")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Ver Candidatos")
                }

 */
            }
        }
    }
}
