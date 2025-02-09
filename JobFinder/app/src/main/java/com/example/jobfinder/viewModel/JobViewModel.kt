package com.example.jobfinder.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.jobfinder.Services.JobRepository
import com.example.jobfinder.dataClass.JobPosition
import com.example.jobfinder.dataClass.JobPositionDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class JobViewModel(private val jobRepository: JobRepository) : ViewModel() {

    // Estado para a lista de vagas
    private val _jobs = MutableStateFlow<List<JobPosition>>(emptyList())
    val jobs: StateFlow<List<JobPosition>> = _jobs

    // Estado para uma vaga específica
    private val _job = MutableStateFlow<JobPosition?>(null)
    val job: StateFlow<JobPosition?> = _job

    // Estado para mensagens de erro/sucesso
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    // Carregar todas as vagas
    fun loadJobs() {
        viewModelScope.launch {
            val response = jobRepository.getAllJobs()
            if (response.isSuccessful) {
                _jobs.value = response.body() ?: emptyList()
            } else {
                _message.value = "Erro ao carregar vagas: ${response.message()}"
            }
        }
    }

    // Carregar uma vaga por ID
    fun loadJobById(id: Long) {
        viewModelScope.launch {
            val response = jobRepository.getJobsId(id)
            if (response.isSuccessful) {
                _job.value = response.body()
            } else {
                _message.value = "Erro ao carregar vaga: ${response.message()}"
            }
        }
    }

    // Criar uma nova vaga
    fun createJob(jobDto: JobPositionDTO) {
        viewModelScope.launch {
            val response = jobRepository.creatJob(jobDto)
            if (response.isSuccessful) {
                _message.value = "Vaga criada com sucesso!"
                loadJobs() // Recarrega a lista de vagas
            } else {
                _message.value = "Erro ao criar vaga: ${response.message()}"
            }
        }
    }

    // Atualizar uma vaga existente
    fun updateJob(id: Long, jobDto: JobPositionDTO) {
        viewModelScope.launch {
            val response = jobRepository.updateJob(id, jobDto)
            if (response.isSuccessful) {
                _message.value = "Vaga atualizada com sucesso!"
                loadJobs() // Recarrega a lista de vagas
            } else {
                _message.value = "Erro ao atualizar vaga: ${response.message()}"
            }
        }
    }

    // Excluir uma vaga
    fun deleteJob(id: Long) {
        viewModelScope.launch {
            val response = jobRepository.deletJob(id)
            if (response.isSuccessful) {
                _message.value = "Vaga excluída com sucesso!"
                loadJobs() // Recarrega a lista de vagas
            } else {
                _message.value = "Erro ao excluir vaga: ${response.message()}"
            }
        }
    }

    // Candidatar-se a uma vaga
    fun applyForJob(id: Long) {
        viewModelScope.launch {
            val response = jobRepository.applyForJob(id)
            if (response.isSuccessful) {
                _message.value = "Candidatura enviada com sucesso!"
            } else {
                _message.value = "Erro ao enviar candidatura: ${response.message()}"
            }
        }
    }
}
class JobViewModelFactory(private val jobRepository: JobRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JobViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return JobViewModel(jobRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
