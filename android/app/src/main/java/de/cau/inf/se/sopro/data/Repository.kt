package de.cau.inf.se.sopro.data

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import de.cau.inf.se.sopro.model.applicant.Applicant
import de.cau.inf.se.sopro.model.applicant.Usertype
import de.cau.inf.se.sopro.model.application.Application
import de.cau.inf.se.sopro.model.application.Form
import de.cau.inf.se.sopro.model.application.Status
import de.cau.inf.se.sopro.network.api.ApiService
import de.cau.inf.se.sopro.network.api.CreateApplicantRequest
import de.cau.inf.se.sopro.persistence.dao.ApplicantDao
import de.cau.inf.se.sopro.persistence.dao.ApplicationDao
import de.cau.inf.se.sopro.persistence.dao.FormDao
import kotlinx.serialization.json.Json
import java.time.LocalDateTime
import java.util.Date

interface Repository{
    suspend fun checkHealth() : Boolean

    suspend fun authenticateLogin(username: String,
                                  password: String) : LoginResult
    suspend fun createApplicant(username: String,
                                email: String,
                                password: String,
                                role: Usertype): Boolean
    suspend fun getForms(): List<Form>?
    suspend fun getApplications(
      createdAt: Date,
      formId: Int,
      status : Status,
      applicantId: Int
    ): List<Application>?
    suspend fun createApplication(application: Application)
    suspend fun updateApplication(application: Application)
    suspend fun getFormByTitle(title: String): Form?
}
class DefRepository(private val apiService : ApiService, private val applicantDao: ApplicantDao,
                    private val applicationDao: ApplicationDao, private val formDao: FormDao) : Repository{
    //Localdatabase only

    override suspend fun getFormByTitle(title: String): Form?{
        val response = formDao.getFormByName(title)
        return response
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

    override suspend fun createApplication(application: Application){
        applicationDao.saveApplication(application)
        val response = apiService.createApplication(application)
        try {
            response.isSuccessful
        }catch (e : IllegalArgumentException){ //is not an IllegalArgumentException
            print(e)
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun authenticateLogin(username: String, password: String): LoginResult {
        return try {
            val response = apiService.authenticateLogin(username = username, password = password)

            if (response.isSuccessful) {
                val jwt = Json.encodeToString(response.body())
                applicantDao.saveJwt(
                    Applicant(
                        1,
                        username,
                        password,
                        LocalDateTime.now(),
                        Usertype.APPLICANT,
                        jwt = jwt
                    )
                )
                LoginResult.Success
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
                Log.d("Repository", "User created successfully with ID: $newUserId")

                applicantDao.saveApplicant(Applicant(newUserId, username, password, LocalDateTime.now(), role))

                return true
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

    override suspend fun getApplications(
        createdAt: Date,
        formId: Int,
        status : Status,
        applicantId: Int
    ): List<Application>? {
        val response = apiService.getApplications(createdAt,formId,status,applicantId)
        return response.body()  //da bin ich mir nicht sicher + wir brauchen eine absicherung falls der andere fall eintritt
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
}

sealed class LoginResult {
    data object Success : LoginResult()
    data object UserNotFound : LoginResult()
    data object WrongPassword : LoginResult()
    data object GenericError : LoginResult()
}