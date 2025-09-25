package de.cau.inf.se.sopro.network.api

import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import de.cau.inf.se.sopro.model.applicant.Applicant
import de.cau.inf.se.sopro.model.applicant.Usertype
import de.cau.inf.se.sopro.model.application.Application
import de.cau.inf.se.sopro.model.application.Form
import de.cau.inf.se.sopro.model.application.Status
import kotlinx.serialization.json.Json
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
import java.util.Date

interface ApiService{
    @GET("api/v1/health") //check if the Database is running
    suspend fun checkHealth() : Call<List<String>>

    @PUT("/api/v1/auth/login") //authenticate the applicant
    suspend fun authenticateLogin(@Query("username") username: String, @Query("password") password: String): Response<String>

    @POST("/api/v1/users") //create new applicant
    suspend fun createApplicant(@Field("username") username: String,
                                @Field("password") password: String,
                                @Field("role") role: Usertype) : Response<Int>


    @GET("/api/v1/forms") //get all Forms
    suspend fun getForms() : Response<List<Form>>

    @GET("/api/v1/applications")
    suspend fun getApplications(
        @Query("createdAt") createdAt: Date,
        @Query("formID") formId: Int,
        @Query("status") status : Status,
        @Query("applicantId") applicantId: Int
    ) : Response<List<Application>>

    @POST("/api/v1/applications")
    suspend fun createApplication( application: Application) : Response<Int>


    @PUT("/api/v1/applications/{applicationId}")
    suspend fun updateApplication(application: Application) : Response<List<Int>>


}
