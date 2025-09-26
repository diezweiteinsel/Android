package de.cau.inf.se.sopro.data

import de.cau.inf.se.sopro.model.applicant.Applicant
import de.cau.inf.se.sopro.persistence.dao.ApplicantDao
import okhttp3.Interceptor
import okhttp3.Response


class AuthInterceptor (private val applicantDao: ApplicantDao) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val jwt = applicantDao.getJwt(applicantDao.getUserId())
        val authenticatedRequest = request.newBuilder()
            .header("Authorization", "Bearer $jwt")
            .build()
        return chain.proceed(authenticatedRequest)
    }
}