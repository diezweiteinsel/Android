package de.cau.inf.se.sopro.data

import de.cau.inf.se.sopro.persistence.dao.ApplicantDao
import okhttp3.Interceptor
import okhttp3.Response

//every time when we use the okHttpClient or Retrofit, okHttp iterates through our interceptors
class AuthInterceptor (private val applicantDao: ApplicantDao) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response { //an interceptor chain represents the chain of Interceptors and the actual nw call
        val request = chain.request()
        val jwt = applicantDao.getJwt(applicantDao.getUserId()) //getting the jwt token from our Database
        val authenticatedRequest = request.newBuilder() //building the actual request with the token that will be in the header of API-Calls
            .header("Authorization", "Bearer $jwt")
            .build()
        return chain.proceed(authenticatedRequest)
    }
}