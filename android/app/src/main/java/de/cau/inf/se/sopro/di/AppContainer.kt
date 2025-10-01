package de.cau.inf.se.sopro.di

import android.content.Context
import de.cau.inf.se.sopro.data.AuthInterceptor
import de.cau.inf.se.sopro.data.DefRepository
import de.cau.inf.se.sopro.data.Repository
import de.cau.inf.se.sopro.data.TokenManager
import de.cau.inf.se.sopro.network.api.ApiService
import de.cau.inf.se.sopro.persistence.LocDatabase
import de.cau.inf.se.sopro.persistence.dao.ApplicantDao
import de.cau.inf.se.sopro.persistence.dao.ApplicationDao
import de.cau.inf.se.sopro.persistence.dao.FormDao
import de.cau.inf.se.sopro.persistence.dao.BlockDao
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


interface AppContainer {
    val repository : Repository
    val tokenManager: TokenManager
}

class DefaultAppContainer(private val context: Context) : AppContainer {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // This is the default URL of the web backend running locally on the host.
    private val BASE_URL_LOCALHOST = "http://localhost:8000"

    // The Android App running within the emulator can access the web backend
    // running locally on the same host via the loopback address 10.0.2.2
    //private val BASE_URL_LOOPBACK_FOR_EMULATOR = "http://134.245.1.240:1203"
    private val BASE_URL_LOOPBACK_FOR_EMULATOR = "http://10.0.2.2:8083/"

    // Set the currently used web backend URL
    private val USED_URL = BASE_URL_LOOPBACK_FOR_EMULATOR


    override val tokenManager: TokenManager by lazy {
        TokenManager(context)
    }


    private val applicantDao: ApplicantDao = LocDatabase.getDatabase(context).applicantDao()
    private val applicationDao: ApplicationDao = LocDatabase.getDatabase(context).applicationDao()
    private val formDao: FormDao = LocDatabase.getDatabase(context).formDao()
    private val authInterceptor = AuthInterceptor(tokenManager)


    // Create instance of OkHttpClient with interceptor that adds
    // credentials for HTTP Basic Authentication to each request
    var authClient: OkHttpClient =
        OkHttpClient().newBuilder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()
        private val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(authClient)
            .baseUrl(USED_URL)
            .build()
        private val api: ApiService = retrofit.create(ApiService::class.java)

    override val repository: Repository by lazy {
        DefRepository( api,applicantDao,applicationDao,formDao,tokenManager)
    }


}

