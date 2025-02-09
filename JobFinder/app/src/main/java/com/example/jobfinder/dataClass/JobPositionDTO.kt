package com.example.jobfinder.dataClass

import java.math.BigDecimal

data class JobPositionDTO(
    val title: String,
    val description: String,
    val salary: BigDecimal,
    val location: String?,
    val requirements: String?,
    val benefits: String?,
)
