package de.cau.inf.se.sopro.data

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.auth0.android.jwt.JWT
import de.cau.inf.se.sopro.model.applicant.Applicant
import de.cau.inf.se.sopro.model.applicant.Usertype
import de.cau.inf.se.sopro.model.application.Application
import de.cau.inf.se.sopro.model.application.Form
import de.cau.inf.se.sopro.model.application.Status
import de.cau.inf.se.sopro.network.api.ApiService
import de.cau.inf.se.sopro.network.api.CreateApplicantRequest
import de.cau.inf.se.sopro.network.api.UpdateApplicationRequest
import de.cau.inf.se.sopro.network.api.createApplication
import de.cau.inf.se.sopro.persistence.dao.ApplicantDao
import de.cau.inf.se.sopro.persistence.dao.ApplicationDao
import de.cau.inf.se.sopro.persistence.dao.BlockDao
import de.cau.inf.se.sopro.persistence.dao.FormDao
import de.cau.inf.se.sopro.ui.submitApplication.FieldPayload
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.time.LocalDateTime

interface Repository {
    // --- Authentification & User ---
    suspend fun authenticateLogin(username: String, password: String): LoginResult
    suspend fun loginAndSync(username: String, password: String): LoginResult
    suspend fun createApplicant(username: String, email: String, password: String, role: Usertype): Boolean
    suspend fun logout()

    // --- User Applications ---
    fun getApplicationsAsFlow(userId: Int): Flow<List<Application>>
    suspend fun refreshApplicationsAndForms()
    suspend fun createApplication(application: createApplication)
    suspend fun getApplicationByCompositeKey(appId: Int, formId: Int): Application?
    suspend fun getFormById(id: Int): Form?
    suspend fun updateApplication(appId: Int, formId: Int, payload: Map<Int, FieldPayload>)

    // --- Public Applications ---
    fun getPublicApplicationsAsFlow(): Flow<List<Application>>
    suspend fun refreshPublicApplications()

    // --- Forms ---
    suspend fun getForms(): List<Form>?
    suspend fun getFormByTitle(title: String): Form?

    // --- Health Check / Other ---
    suspend fun checkHealth(url: String): Boolean
}

class DefRepository @Inject constructor(
    private val apiService : ApiService,
    private val applicantDao: ApplicantDao,
    private val applicationDao: ApplicationDao,
    private val formDao: FormDao,
    private val blockDao: BlockDao,
    private val tokenManager: TokenManager
) : Repository{

    // --- Authentification & User ---
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
        } catch (e: java.io.IOException) {
            Log.e("Repository", "Authentication failed with a network exception", e)
            return LoginResult.NetworkError
        }
        catch (e: Exception) {
            Log.e("Repository", "Authentication failed with an exception", e)
            return LoginResult.GenericError
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun loginAndSync(username: String, password: String): LoginResult {
        val loginResult = authenticateLogin(username, password)

        if (loginResult is LoginResult.Success) {
            try {
                refreshPublicApplications()
                refreshApplicationsAndForms()
            } catch (e: Exception) {
                Log.e("Repository", "Sync failed after login", e)
                return LoginResult.GenericError
            }
        }

        return loginResult
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

    // --- User Applications ---
    override suspend fun getApplicationByCompositeKey(appId: Int, formId: Int): Application? {
        return applicationDao.getApplicationByCompositeKey(appId, formId)
    }

    override suspend fun getFormById(id: Int): Form? {
        return formDao.getFormById(id)
    }

    override suspend fun updateApplication(appId: Int, formId: Int, payload: Map<Int, FieldPayload>) {
        try {
            val requestBody = UpdateApplicationRequest(
                formId = formId,
                applicationId = appId,
                payload = payload
            )

            val response = apiService.updateApplication(
                formId = formId,
                applicationId = appId,
                requestBody = requestBody
            )

            if (!response.isSuccessful) {
                Log.e("Repository", "Failed to update application $appId (form $formId). Code: ${response.code()}")
            } else {
                Log.d("Repository", "Application $appId (form $formId) updated successfully.")
                refreshApplicationsAndForms()
            }
        } catch (e: Exception) {
            Log.e("Repository", "Exception during updateApplication", e)
        }
    }

    override suspend fun logout() {
        tokenManager.clearAll()
        applicationDao.clearAll()
        blockDao.clearAll()
        applicantDao.clearAll()
        formDao.clearAll()
    }

    override suspend fun getFormByTitle(title: String): Form?{
        val response = formDao.getFormByName(title)
        return response
    }

    override suspend fun refreshApplicationsAndForms() {

        val formsResponse = apiService.getForms()
        if (formsResponse.isSuccessful) {           //getting successful response from api
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

            if (response.isSuccessful && response.body() != null) {
                val networkApplications = response.body()!!

                val correctedApplications = networkApplications.map { application ->
                    application.copy(userId = userId)
                }

                applicationDao.clearAndUpsertUserSpecific(correctedApplications, userId)
            } else {
                Log.w("Repository", "Fetching user applications was not successful. Code: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("Repository", "Failed to refresh applications for user $userId", e)
        }

    }

    override suspend fun checkHealth(url: String): Boolean {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            return false
        }

        return try {
            val tempBaseUrl = if (url.endsWith("/")) url else "$url/"

            val tempRetrofit = Retrofit.Builder()
                .baseUrl(tempBaseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()

            val tempApiService = tempRetrofit.create(ApiService::class.java)

            val response = tempApiService.checkHealth()

            response.isSuccessful
        } catch (e: Exception) {
            Log.e("Repository", "Dynamic health check failed for URL: $url", e)
            false
        }
    }


    override suspend fun createApplication(application: createApplication){
        Log.d("Repository", "Response")
        val response = apiService.createApplication(application)

        try {
            response.isSuccessful
        }catch (e : IllegalArgumentException){
            print(e)
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

    override fun getApplicationsAsFlow(userId: Int): Flow<List<Application>> {
        return applicationDao.getApplicationsAsFlow(userId)
    }



    override fun getPublicApplicationsAsFlow(): Flow<List<Application>> {
        return applicationDao.getPublicApplicationsAsFlow()
    }

    override suspend fun refreshPublicApplications() {
        try {
            val response = apiService.getApplications(
                isPublic = true,
                status = Status.APPROVED
            )

            if (response.isSuccessful && response.body() != null) {
                val publicApplications = response.body()!!

                applicationDao.clearAndUpsertPublic(publicApplications)
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
    data object NetworkError : LoginResult()
    data object GenericError : LoginResult()
}