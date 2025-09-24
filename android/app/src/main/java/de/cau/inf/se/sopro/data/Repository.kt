package de.cau.inf.se.sopro.data

import de.cau.inf.se.sopro.model.applicant.Applicant
import de.cau.inf.se.sopro.model.applicant.Usertype
import de.cau.inf.se.sopro.model.application.Application
import de.cau.inf.se.sopro.model.application.Form
import de.cau.inf.se.sopro.model.application.Status
import de.cau.inf.se.sopro.network.api.ApiService
import kotlinx.coroutines.runBlocking

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Response
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Query

interface Repository{
    suspend fun checkHealth() : Call<List<String>>
    suspend fun authenticateLogin()
    suspend fun createApplicant(username: String, password: String)
    suspend fun getForms() : Call<List<Form>>
    suspend fun getApplications( ApplicantId: Int, status : Status) : Call<List<Application>>
    suspend fun createApplication( id: Int, applicantName: String,
                                   submissionDate: String,
                                   form: Form,
                                   status: Status,
                                   public: Boolean,
                                   edited: Boolean) : Application
    suspend fun updateApplication(@Query("id") id: Int)
    fun validateUsercreation(response: Int)
}
class DefRepository( private val apiService : ApiService) : Repository{
    override suspend fun updateApplication(id: Int) {
        TODO("Not yet implemented")
    }

    override fun validateUsercreation(response: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun createApplication(
        id: Int,
        applicantName: String,
        submissionDate: String,
        form: Form,
        status: Status,
        public: Boolean,
        edited: Boolean
    ): Application {
        TODO("Not yet implemented")
    }

    override suspend fun authenticateLogin() {
        TODO("Not yet implemented")
    }

    override suspend fun checkHealth(): Call<List<String>> {
        TODO("Not yet implemented")
    }
    override suspend fun createApplicant(username:String,password: String) {

        val response = apiService.createApplicant(username, password, Usertype.APPLICANT)
        if (response.isSuccessful){
            print("do nothing")
        }else{
            print("failed")
        }
    }


    override suspend fun getApplications(
        ApplicantId: Int,
        status: Status
    ): Call<List<Application>> {
        TODO("Not yet implemented")
    }

    override suspend fun getForms(): Call<List<Form>> {
        TODO("Not yet implemented")
    }

    //private val databasedao : DatabaseDao


    fun main() {
        val server = MockWebServer()
        server.start()

        // Enqueue fake response
        server.enqueue(
            MockResponse()
                .setBody("""{"id": 1}""")
                .setResponseCode(201)
        )

        // Build Retrofit pointing to the mock server
        val retrofit = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)

        runBlocking {
            val post = api.createApplicant(Usertype.APPLICANT,"max ratjen","password")
            println("Mocked Response: $post")
        }

        server.shutdown()
    }
}