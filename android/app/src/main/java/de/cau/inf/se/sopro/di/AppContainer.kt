package de.cau.inf.se.sopro.di

import android.content.Context
import kotlinx.serialization.json.Json
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory


interface AppContainer {

}

class DefaultAppContainer(private val context: Context) : AppContainer {

    // This is the default URL of the web backend running locally on the host.
    private val BASE_URL_LOCALHOST = "http://localhost:8080"

    // The Android App running within the emulator can access the web backend
    // running locally on the same host via the loopback address 10.0.2.2
    private val BASE_URL_LOOPBACK_FOR_EMULATOR = "http://10.0.2.2:8080"

    // Set the currently used web backend URL
    private val USED_URL = BASE_URL_LOOPBACK_FOR_EMULATOR


    // Create instance of OkHttpClient with interceptor that adds
    // credentials for HTTP Basic Authentication to each request
    var authClient: OkHttpClient =
        OkHttpClient().newBuilder().addInterceptor(Interceptor { chain: Interceptor.Chain? ->
            val originalRequest: Request = chain!!.request()
            val builder: Request.Builder = originalRequest.newBuilder().header(
                "Authorization",
                Credentials.basic("admin", "password")
            )
            val newRequest: Request = builder.build()
            chain.proceed(newRequest)
        }).build()


    /**
     * Use the Retrofit builder to build a retrofit object using a kotlinx.serialization converter
     * and a custom OkHttpClient instance
     */
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .client(authClient)
        .baseUrl(USED_URL)
        .build()
    


}
