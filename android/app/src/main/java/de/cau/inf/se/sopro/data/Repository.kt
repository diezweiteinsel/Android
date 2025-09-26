package de.cau.inf.se.sopro.data

import androidx.annotation.Nullable
import de.cau.inf.se.sopro.model.applicant.Usertype
import de.cau.inf.se.sopro.model.application.Application
import de.cau.inf.se.sopro.model.application.Form
import de.cau.inf.se.sopro.model.application.Status
import de.cau.inf.se.sopro.network.api.ApiService
import de.cau.inf.se.sopro.persistence.dao.ApplicantDao
import kotlinx.serialization.internal.throwMissingFieldException
import retrofit2.Call
import java.util.Date

interface Repository{
    suspend fun checkHealth() : Boolean

    suspend fun authenticateLogin(username: String,  password: String, jwt: String) : Boolean
    suspend fun createApplicant(username: String,
                                password: String, role: Usertype)
    suspend fun getForms(): List<Form>?
    suspend fun getApplications(
      createdAt: Date,
      formId: Int,
      status : Status,
      applicantId: Int
    ): List<Application>?
    suspend fun createApplication( application: Application)
    suspend fun updateApplication(application: Application)

}
class DefRepository( private val apiService : ApiService, private val applicantDao: ApplicantDao) : Repository{

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
        val response = apiService.createApplication(application)

    }

    override suspend fun authenticateLogin(username: String, password: String,jwt:String): Boolean {
        val response = apiService.authenticateLogin(username,password,jwt)
        applicantDao.saveJwt(response.body()!!)
        //if(jwt not in DB)
            //JWT in DB speichern
        //else{
        //tuhe nichts  }
        return response.isSuccessful
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
    ): List<Application>? {
        val response = apiService.getApplications(createdAt,formId,status,applicantId)
        return response.body()  //da bin ich mir nicht sicher + wir brauchen eine absicherung falls der andere fall eintritt

    }

    override suspend fun getForms(): List<Form>? {  //convert forms into objects method will call this
        val forms = apiService.getForms()
        return forms.body()
    }

    //private val databaseDao : DatabaseDao

}