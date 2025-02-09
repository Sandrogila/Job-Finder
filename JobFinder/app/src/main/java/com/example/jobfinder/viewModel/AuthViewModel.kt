package com.example.jobfinder.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.jobfinder.Services.AuthRepository
import com.example.jobfinder.dataClass.AuthenticationResponse
import com.example.jobfinder.dataClass.CompanyDTO
import com.example.jobfinder.dataClass.UserDto
import com.example.jobfinder.database.entities.UserEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    // Estado para indicar se o usuário está logado
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    // Estado para armazenar o resultado da autenticação (se necessário)
    private val _authResult = MutableStateFlow<Result<AuthenticationResponse>?>(null)
    val authResult: StateFlow<Result<AuthenticationResponse>?> = _authResult

    // Estado para armazenar os dados do usuário (salvo no Room)
    private val _user = MutableStateFlow<UserEntity?>(null)
    val user: StateFlow<UserEntity?> = _user

    init {
        checkAuthState()
    }

    // Verifica se há um usuário salvo no banco de dados Room
    private fun checkAuthState() = viewModelScope.launch {
        _user.value = authRepository.getUser()
        _isLoggedIn.value = _user.value != null
    }

    // Login: chama a API e salva os dados no Room
    fun login(email: String, password: String) = viewModelScope.launch {
        val result = authRepository.authenticate(email, password)
        _authResult.value = result
        result.onSuccess {
            _user.value = authRepository.getUser()
            _isLoggedIn.value = _user.value != null
        }
    }

    // Logout: limpa os dados salvos no Room
    fun logout() = viewModelScope.launch {
        authRepository.clearUser()
        _user.value = null
        _isLoggedIn.value = false
    }

    // Registrar usuário: chama a API e salva os dados no Room
    fun registerUser(userDto: UserDto) = viewModelScope.launch {
        val result = authRepository.registerUser(userDto)
        _authResult.value = result
        result.onSuccess {
            _user.value = authRepository.getUser()
            _isLoggedIn.value = _user.value != null
        }
    }

    // Registrar empresa: chama a API e salva os dados no Room
    fun registerCompany(companyDto: CompanyDTO) = viewModelScope.launch {
        val result = authRepository.registerCompany(companyDto)
        _authResult.value = result
        result.onSuccess {
            _user.value = authRepository.getUser()
            _isLoggedIn.value = _user.value != null
        }
    }
}

class AuthViewModelFactory(private val repository: AuthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
