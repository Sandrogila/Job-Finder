package com.example.jobfinder.API

import com.example.jobfinder.dataClass.AuthenticationRequest
import com.example.jobfinder.dataClass.AuthenticationResponse
import com.example.jobfinder.dataClass.CompanyDTO
import com.example.jobfinder.dataClass.JobPosition
import com.example.jobfinder.dataClass.UserDto
import com.example.jobfinder.dataClass.JobPositionDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST("api/v1/auth/register/user")
    suspend fun registerUser(@Body userDto: UserDto): Response<AuthenticationResponse>

    @POST("api/v1/auth/register/company")
    suspend fun registerCompany(@Body companyDto: CompanyDTO): Response<AuthenticationResponse>

    @POST("api/v1/auth/authenticate")
    suspend fun authenticate(@Body request: AuthenticationRequest): Response<AuthenticationResponse>

    @GET("api/v1/jobs")
    suspend fun getAllJobs(): Response<List<JobPosition>>

    @GET("api/v1/jobs/{id}")
    suspend fun getJobById(@Path("id") id: Long): Response<JobPosition>

    @POST("api/v1/jobs")
    suspend fun createJob(@Body jobDto: JobPositionDTO): Response<JobPosition>

    @PUT("api/v1/jobs/{id}")
    suspend fun updateJob(@Path("id") id: Long, @Body jobDto: JobPositionDTO): Response<JobPosition>

    @DELETE("api/v1/jobs/{id}")
    suspend fun deleteJob(@Path("id") id: Long): Response<Void>

    @POST("api/v1/jobs/{id}/apply")
    suspend fun applyForJob(@Path("id") id: Long): Response<Void>
}