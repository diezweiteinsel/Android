package de.cau.inf.se.sopro.network.api

import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import de.cau.inf.se.sopro.model.applicant.Applicant
import de.cau.inf.se.sopro.model.applicant.Usertype
import de.cau.inf.se.sopro.model.application.Application
import de.cau.inf.se.sopro.model.application.Form
import de.cau.inf.se.sopro.model.application.Status
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ApiService{
    @GET("api/v1/health") //check if the Database is running
    suspend fun checkHealth() : Call<List<String>>

    @PUT("/api/v1/auth/login") //authenticate the applicant
    suspend fun authenticateLogin()

    @POST("/api/v1/users") //create new applicant
    suspend fun createApplicant(username: String, password: String) : Int


    @GET("/api/v1/forms") //get all Forms
    suspend fun getForms() : Call<List<Form>>

    @GET("/api/v1/applications")
    suspend fun getApplications(
        @Query("ApplicantId") ApplicantId: Int,
        @Query("status") status : Status
    ) : Call<List<Application>>

    @POST("/api/v1/applications")
    suspend fun createApplication( id: Int, applicantName: String,
                                  submissionDate: String,
                                  form: Form,
                                  status: Status,
                                  public: Boolean,
                                  edited: Boolean) : Application
    @PUT("/api/v1/applications/{applicationId}")
    suspend fun updateApplication(@Query("id") id: Int)


}
