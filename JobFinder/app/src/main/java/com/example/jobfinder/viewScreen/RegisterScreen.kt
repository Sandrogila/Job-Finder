package com.example.jobfinder.viewScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.jobfinder.dataClass.CompanyDTO
import com.example.jobfinder.dataClass.UserDto
import com.example.jobfinder.viewModel.AuthViewModel
import com.example.jobfinder.viewModel.AuthViewModelFactory
import com.example.jobfinder.Services.AuthRepository
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(AuthRepository(LocalContext.current)))
) {
    var isUserSelected by remember { mutableStateOf(true) }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var companyName by remember { mutableStateOf("") }
    var companyAddress by remember { mutableStateOf("") }
    var companyEmail by remember { mutableStateOf("") }
    var companyPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var registrationType by remember { mutableStateOf("") }

    val authResult by authViewModel.authResult.collectAsState()

    LaunchedEffect(authResult) {
        authResult?.let { result ->
            result.onSuccess { _ ->
                val destination = if (registrationType == "user") "user_dashboard" else "company_dashboard"
                navController.navigate(destination)
            }
            result.onFailure { error ->
                errorMessage = error.message ?: "Erro desconhecido"
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0077B5)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Job Finder", fontSize = 26.sp, color = Color(0xFF005582))

            Button(
                onClick = { isUserSelected = !isUserSelected },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF449647)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(if (isUserSelected) "Registrar como Empresa" else "Registrar como Usuário")
            }

            if (isUserSelected) {
                RegisterUserForm(
                    username = username,
                    email = email,
                    password = password,
                    onUsernameChange = { username = it },
                    onEmailChange = { email = it },
                    onPasswordChange = { password = it }
                )
            } else {
                RegisterCompanyForm(
                    companyName = companyName,
                    companyEmail = companyEmail,
                    companyAddress = companyAddress,
                    companyPassword = companyPassword,
                    onCompanyNameChange = { companyName = it },
                    onCompanyEmailChange = { companyEmail = it },
                    onCompanyAddressChange = { companyAddress = it },
                    onCompanyPasswordChange = { companyPassword = it }
                )
            }

            if (errorMessage.isNotEmpty()) {
                Text(errorMessage, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
            }

            Button(
                onClick = {
                    registrationType = if (isUserSelected) "user" else "company"
                    if (isUserSelected) {
                        authViewModel.registerUser(UserDto(username, email, password))
                    } else {
                        authViewModel.registerCompany(CompanyDTO(companyName, companyEmail, companyAddress, companyPassword))
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF449647)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text("Cadastrar")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterUserForm(
    username: String,
    email: String,
    password: String,
    onUsernameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        CustomOutlinedTextField("Nome de Usuário", username, Icons.Default.Person, onUsernameChange)
        CustomOutlinedTextField("E-mail", email, Icons.Default.Email, onEmailChange)
        CustomOutlinedTextField("Senha", password, Icons.Default.Lock, onPasswordChange, isPassword = true)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterCompanyForm(
    companyName: String,
    companyEmail: String,
    companyAddress: String,
    companyPassword: String,
    onCompanyNameChange: (String) -> Unit,
    onCompanyEmailChange: (String) -> Unit,
    onCompanyAddressChange: (String) -> Unit,
    onCompanyPasswordChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        CustomOutlinedTextField("Nome da Empresa", companyName, Icons.Default.AccountBox, onCompanyNameChange)
        CustomOutlinedTextField("E-mail da Empresa", companyEmail, Icons.Default.Email, onCompanyEmailChange)
        CustomOutlinedTextField("Endereço da Empresa", companyAddress, Icons.Default.LocationOn, onCompanyAddressChange)
        CustomOutlinedTextField("Senha da Empresa", companyPassword, Icons.Default.Lock, onCompanyPasswordChange, isPassword = true)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomOutlinedTextField(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(icon, contentDescription = null) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color(0xFF0077B5),
            unfocusedBorderColor = Color.Gray
        )
    )
}
