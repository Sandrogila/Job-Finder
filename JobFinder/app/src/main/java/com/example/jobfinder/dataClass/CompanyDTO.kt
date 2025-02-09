package com.example.jobfinder.dataClass

data class CompanyDTO(
    val name: String,
    val email: String,
    val password: String,
    val description: String? = null,
    val website: String? = null
)
