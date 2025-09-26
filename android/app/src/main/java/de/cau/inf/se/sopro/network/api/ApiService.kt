package de.cau.inf.se.sopro.network.api


import de.cau.inf.se.sopro.model.applicant.Usertype
import de.cau.inf.se.sopro.model.application.Application
import de.cau.inf.se.sopro.model.application.Form
import de.cau.inf.se.sopro.model.application.Status
import kotlinx.serialization.Serializable
import okhttp3.Request
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
import java.util.Date

interface ApiService{

    @GET("api/v1/health") //check if the Database is running
    suspend fun checkHealth() : Response<String>
    //val grant_type: String = "password",
    @FormUrlEncoded
    @POST("/api/v1/auth/token")
    suspend fun authenticateLogin(
        @Field("grant_type") grantType: String = "password",
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<LoginResponse>
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

//data classes
@Serializable
data class LoginResponse(
    val access_token: String,
    val token_type: String,
    val roles: List<String>
)