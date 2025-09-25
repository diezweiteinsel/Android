package de.cau.inf.se.sopro.data

import de.cau.inf.se.sopro.model.applicant.Usertype
import de.cau.inf.se.sopro.model.application.Application
import de.cau.inf.se.sopro.model.application.Form
import de.cau.inf.se.sopro.model.application.Status
import de.cau.inf.se.sopro.network.api.ApiService
import kotlinx.coroutines.runBlocking

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
import java.util.Date

interface Repository{
    suspend fun checkHealth() : Call<List<String>>

    suspend fun authenticateLogin(username: String,  password: String): Response<String>

    suspend fun createApplicant(username: String,
                                password: String, role: Usertype)

    suspend fun getForms(): Call<List<Form>>
    suspend fun getApplications(
      createdAt: Date,
      formId: Int,
      status : Status,
      applicantId: Int
    ): Call<List<Application>>

    suspend fun createApplication( application: Application)

    suspend fun updateApplication(application: Application)

}
class DefRepository( private val apiService : ApiService) : Repository{

    override suspend fun updateApplication(application: Application) {
        TODO("Not yet implemented")
    }


    override suspend fun createApplication(application: Application){
        TODO("Not yet implemented")
    }

    override suspend fun authenticateLogin(username: String, password: String): Response<String> {
        TODO("Not yet implemented")
    }

    override suspend fun checkHealth(): Call<List<String>> {
        TODO("Not yet implemented")
    }
    override suspend fun createApplicant(username:String,password: String,role: Usertype) {

        val response = apiService.createApplicant(username, password, Usertype.APPLICANT)
        if (response.isSuccessful){
            print("success")
        }else{
            print("failed")
        }
    }

    override suspend fun getApplications(
        createdAt: Date,
        formId: Int,
        status : Status,
        applicantId: Int
    ): Call<List<Application>> {
        TODO("Not yet implemented")
    }

    override suspend fun getForms(): Call<List<Form>> {
        TODO("Not yet implemented")
    }

    //private val databasedao : DatabaseDao

}