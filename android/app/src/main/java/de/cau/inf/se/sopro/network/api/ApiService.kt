package de.cau.inf.se.sopro.network.api


import com.google.gson.annotations.SerializedName
import de.cau.inf.se.sopro.model.applicant.Usertype
import de.cau.inf.se.sopro.model.application.Application
import de.cau.inf.se.sopro.model.application.Block
import de.cau.inf.se.sopro.model.application.Form
import de.cau.inf.se.sopro.model.application.Status
import de.cau.inf.se.sopro.ui.submitApplication.FieldPayload
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

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
    suspend fun createApplicant(@Body request: CreateApplicantRequest) : Response<CreateApplicantResponse>


    @GET("/api/v1/forms") //get all Forms
    suspend fun getForms() : Response<List<Form>>

    @GET("/api/v1/applications")
    suspend fun getApplications( //criteria for filtering applications
        @Query("user_id") userId: Int? = null,
        @Query("form_id") formId: Int? = null,
        @Query("status") status: Status? = null,
        @Query("public") isPublic: Boolean? = null

    ) : Response<List<Application>>

    @POST("/api/v1/applications")
    suspend fun createApplication(@Body request : createApplication) : Response<CreateResponse>
    //create Application with desired body


    @PUT("/api/v1/applications/{applicationId}")
    suspend fun updateApplication(application: Application) : Response<List<Int>>


}

//data classes
@Serializable
data class LoginResponse(
    @SerializedName("access_token")
    val accessToken: String?,
    @SerializedName("token_type")
    val tokenType: String,
    @SerializedName("user_id")
    val userId: Int,
    val roles: List<String>
)

@Serializable
data class CreateApplicantResponse( //our response if an applicant is created from api
    val user_id: Int
)

@Serializable
data class CreateApplicantRequest(
    val username: String,
    val email: String,
    val password: String,
    val role: Usertype
)

//data classes for getForms()
@Serializable
data class FormResponse(
    val id: Int,
    val formName: String, //das ist der responsename
    val blocks: List<Section> //this list contains our building blocks for the application
)

@Serializable
data class Section( //this is one form
    val sectionID: Int = 0,
    val title: String = "",
    val fields: List<Block> = emptyList() //this list contains our building blocks for the application
)


data class createApplication( //these are the exact variable names that our api expects regarding the underscores
    val form_id : Int,
    val payload : Map<Int, FieldPayload>
)
data class CreateResponse( //our response if an application is created
    val id: Int
)