package de.cau.inf.se.sopro.data

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.auth0.android.jwt.JWT
import de.cau.inf.se.sopro.model.applicant.Applicant
import de.cau.inf.se.sopro.model.applicant.Usertype
import de.cau.inf.se.sopro.model.application.Application
import de.cau.inf.se.sopro.model.application.Form
import de.cau.inf.se.sopro.network.api.ApiService
import de.cau.inf.se.sopro.network.api.CreateApplicantRequest
import de.cau.inf.se.sopro.network.api.createApplication
import de.cau.inf.se.sopro.persistence.dao.ApplicantDao
import de.cau.inf.se.sopro.persistence.dao.ApplicationDao
import de.cau.inf.se.sopro.persistence.dao.FormDao
import kotlinx.coroutines.flow.Flow
import java.io.IOException
import java.time.LocalDateTime

interface Repository{
    suspend fun checkHealth() : Boolean

    suspend fun authenticateLogin(username: String,
                                  password: String) : LoginResult
    suspend fun createApplicant(username: String,
                                email: String,
                                password: String,
                                role: Usertype): Boolean
    suspend fun getForms(): List<Form>?

    suspend fun getApplications(userId: Int?): List<Application>

    suspend fun getPublicApplicationsAsFlow(): Flow<List<Application>>

    fun getApplicationsAsFlow(userId: Int): Flow<List<Application>>

    suspend fun refreshApplications()

    suspend fun createApplication(application: Application)

    suspend fun updateApplication(application: Application)

    suspend fun getFormByTitle(title: String): Form?

    fun logout()

    suspend fun loginAndSync(username: String, password: String): LoginResult

    suspend fun refreshPublicApplications()
}
class DefRepository(private val apiService : ApiService,
                    private val applicantDao: ApplicantDao,
                    private val applicationDao: ApplicationDao,
                    private val formDao: FormDao,
                    private val tokenManager: TokenManager
) : Repository{

    override suspend fun getFormByTitle(title: String): Form?{
        val response = formDao.getFormByName(title)
        return response
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun loginAndSync(username: String, password: String): LoginResult {
        val loginResult = authenticateLogin(username, password)

        if (loginResult is LoginResult.Success) {
            try {
                refreshPublicApplications()
                refreshApplications()
            } catch (e: Exception) {
                Log.e("Repository", "Sync failed after login", e)
                return LoginResult.GenericError
            }
        }

        return loginResult
    }

    override suspend fun refreshApplications() {

        val formsResponse = apiService.getForms()
        if (formsResponse.isSuccessful) {
            val formsFromServer = formsResponse.body()
            if (!formsFromServer.isNullOrEmpty()) {

                formDao.insertAll(formsFromServer)
            }
        } else {
            throw IOException("Failed to fetch forms during refresh")
        }

        val userId = tokenManager.getUserId()

        if (userId == null) {
            Log.w("Repository", "Cannot refresh applications, no user ID found.")
            return
        }

        try {
            val response = apiService.getApplications()

            val networkApplications = response.body()

            val correctedApplications = networkApplications!!.map { application ->
                application.copy(userId = userId)
            }

            if (!networkApplications.isNullOrEmpty()) {
                applicationDao.deleteAll()
                applicationDao.upsertAll(correctedApplications)
            }
        } catch (e: Exception) {
            Log.e("Repository", "Failed to refresh applications for user $userId", e)
        }

    }

    override suspend fun checkHealth(): Boolean{
        val response = apiService.checkHealth()
        return response.isSuccessful
    }


    override suspend fun updateApplication(application: Application) {

        val response = apiService.updateApplication(application)
        try {
            response.isSuccessful
        }catch (e : IllegalArgumentException){ //is not an IllegalArgumentException
            print(e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun createApplication(application: createApplication){

        val response = apiService.createApplication(application)
        try {
            response.isSuccessful
        }catch (e : IllegalArgumentException){ //is not an IllegalArgumentException
            print(e)
        }
        val applicationId : Int? = response.body()
        val now = LocalDateTime.now()
        //applicationDao.saveApplication()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun authenticateLogin(username: String, password: String): LoginResult {
        return try {
            val response = apiService.authenticateLogin(username = username, password = password)

            if (response.isSuccessful && response.body() != null) {
                val jwt = response.body()!!.accessToken

                if (jwt != null) {
                    val decodedJWT = JWT(jwt)
                    val userId = decodedJWT.getClaim("userid").asInt()

                    if (userId != null) {
                        tokenManager.saveJwt(jwt)
                        tokenManager.saveUserId(userId)

                        val loggedInApplicant = Applicant(
                            userId = userId,
                            username = username,
                            role = Usertype.APPLICANT
                        )
                        applicantDao.upsertApplicant(loggedInApplicant)

                        return LoginResult.Success
                    }else {
                        Log.e("Repository", "Token did not contain a valid userId")
                        LoginResult.GenericError
                    }
                } else {

                    Log.e("Repository", "Authentication successful but token was null.")
                    return LoginResult.GenericError
                }

            } else {
                when (response.code()) {
                    404 -> LoginResult.UserNotFound
                    401, 403 -> LoginResult.WrongPassword
                    else -> LoginResult.GenericError
                }
            }
        } catch (e: Exception) {
            Log.e("Repository", "Authentication failed with an exception", e)
            LoginResult.GenericError
        }
    }



    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun createApplicant(
        username: String,
        email: String,
        password: String,
        role: Usertype
    ): Boolean {
        try {
            val requestBody = CreateApplicantRequest(
                username = username,
                email = email,
                password = password,
                role = role
            )

            Log.d("Repository", "Request body: $requestBody")

            val response = apiService.createApplicant(requestBody)

            if (response.isSuccessful) {
                val newUserId = response.body()?.user_id

                if (newUserId != null) {
                    Log.d("Repository", "User created successfully with ID: $newUserId")

                    applicantDao.saveApplicant(
                        Applicant(
                            newUserId,
                            username,
                            LocalDateTime.now().toString(),
                            role
                        )
                    )

                    return true
                } else {
                    Log.e("Repository", "Server response contained no user ID")
                    return false
                }
            } else {

                val errorCode = response.code()
                val errorMessage = response.errorBody()?.string()
                Log.e("Repository", "Server registration failed! Code: $errorCode, Message: $errorMessage")

                return false
            }
        } catch (e: Exception) {
            Log.e("Repository", "An exception occurred during registration: ${e.message}", e)
            return false
        }
    }

    override suspend fun getForms(): List<Form>? {  //convert forms into objects method will call this

        val response = apiService.getForms()
        val forms = response.body()
        Log.d("Repository", "Forms: $forms")
        if(response.isSuccessful){
            forms?.forEach { form -> formDao.saveForm(form) }
        }
        return response.body()
    }

    override suspend fun getApplications(userId: Int?): List<Application> {
        try {
            val response = apiService.getApplications()

            if (response.isSuccessful) {
                return response.body() ?: emptyList()
            } else {
                Log.e("Repository", "API Error: ${response.code()} - ${response.message()}")
                return emptyList()
            }
        } catch (e: Exception) {
            Log.e("Repository", "Network Error: ${e.message}")
            return emptyList()

        }
    }

    override fun getApplicationsAsFlow(userId: Int): Flow<List<Application>> {
        return applicationDao.getApplicationsAsFlow(userId)
    }

    override fun logout() {
        tokenManager.clearAll()
    }

    override suspend fun getPublicApplicationsAsFlow(): Flow<List<Application>> {
        return applicationDao.getPublicApplicationsAsFlow()
    }

    override suspend fun refreshPublicApplications() {
        try {
            val response = apiService.getApplications(isPublic = true)
            if (response.isSuccessful && response.body() != null) {
                val publicApplications = response.body()!!

                applicationDao.upsertAll(publicApplications)
            }
        } catch (e: Exception) {
            Log.e("Repository", "Failed to refresh public applications", e)
            throw e
        }
    }
}

sealed class LoginResult {
    data object Success : LoginResult()
    data object UserNotFound : LoginResult()
    data object WrongPassword : LoginResult()
    data object GenericError : LoginResult()
}