package com.example.jobfinder.Services


import com.example.jobfinder.API.ApiService
import com.example.jobfinder.dataClass.JobPosition
import com.example.jobfinder.dataClass.JobPositionDTO
import retrofit2.Response

class JobRepository(private val apiService: ApiService){

    suspend fun getAllJobs(): Response<List<JobPosition>> {
        return apiService.getAllJobs()
    }

    suspend fun getJobsId(id: Long): Response<JobPosition> {
        return apiService.getJobById(id)
    }

    suspend fun creatJob(jobPositionDTO: JobPositionDTO): Response<JobPosition> {
        return apiService.createJob(jobPositionDTO)
    }

    suspend fun updateJob(id: Long, jobDto: JobPositionDTO): Response<JobPosition> {
        return apiService.updateJob(id, jobDto)
    }

    suspend fun deletJob(id: Long): Response<Void> {
        return apiService.deleteJob(id)
    }

    suspend fun applyForJob(id: Long): Response<Void> {
        return apiService.applyForJob(id)
    }
}