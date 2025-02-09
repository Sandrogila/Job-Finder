package com.example.jobfinder.dataClass

import java.math.BigDecimal

data class User(
    val id:Long,
    val name:String,
    val email: String,
    val phone:String?,
    val resume: String?,
    val role: UserRole = UserRole.CANDIDATE,
    val password: String
)
data class Company(
    val id: Long,
    val name: String,
    val email: String,
    val description: String?,
    val website: String?,
    val password: String,
    val role: UserRole=UserRole.COMPANY
)

data class JobPosition(
    val id: Long,
    val title: String,
    val description: String,
    val salary: BigDecimal,
    val location: String?,
    val requirements: String?,
    val benefits: String?,
    val company: Company,
    val status: JobStatus=JobStatus.Open
)
enum class UserRole {
    CANDIDATE, COMPANY
}
enum class JobStatus{
    Open,
    Closed,
    Filled,

}