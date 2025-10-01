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
    val urlManager: UrlManager
}

class DefaultAppContainer(private val context: Context) : AppContainer {

    override val urlManager: UrlManager by lazy {
        UrlManager(context)
    }

    override val tokenManager: TokenManager by lazy {
        TokenManager(context)
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val authInterceptor = AuthInterceptor(tokenManager)

    private val authClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(authInterceptor)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .client(authClient)
        .baseUrl(urlManager.getUrl())
        .build()

    private val api: ApiService = retrofit.create(ApiService::class.java)

    private val applicantDao: ApplicantDao = LocDatabase.getDatabase(context).applicantDao()
    private val applicationDao: ApplicationDao = LocDatabase.getDatabase(context).applicationDao()
    private val formDao: FormDao = LocDatabase.getDatabase(context).formDao()

    override val repository: Repository by lazy {
        DefRepository(api, applicantDao, applicationDao, formDao, tokenManager)

    }
}

