package de.cau.inf.se.sopro.data

import okhttp3.Interceptor
import okhttp3.Response

//every time when we use the okHttpClient or Retrofit, okHttp iterates through our interceptors
class AuthInterceptor(private val tokenManager: TokenManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val jwt = tokenManager.getJwt()

        if (jwt != null) {
            val authenticatedRequest = chain.request().newBuilder()
                .header("Authorization", "Bearer $jwt")
                .build()
            return chain.proceed(authenticatedRequest)
        }
        return chain.proceed(chain.request())
    }
}