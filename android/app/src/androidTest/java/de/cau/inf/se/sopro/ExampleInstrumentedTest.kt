package de.cau.inf.se.sopro

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import de.cau.inf.se.sopro.network.api.ApiService
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

/*
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @get:Rule
    val server= MockWebServer()

    @Before
    fun setup() {
        server.start()
        server.enqueue(
            MockResponse()
                .setResponseCode(201)
                .setBody("""{"id": 1}""")

        )

    }
    @Test
    fun createApplicantTest() {
        val baseUrl = server.url("/")
        // Build Retrofit pointing to the mock server
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        val api = retrofit.create(ApiService::class.java)

        runBlocking {
            val post = api.createApplicant("max ratjen","password", role = )
            println("Mocked Response: $post")
        }
    }
    @After
    fun tearDown() {
        server.shutdown()
    }

}

 */