package de.cau.inf.se.sopro.data

/*
class Repository{
    private val apiservice : ApiService
    //private val databasedao : DatabaseDao


    fun main() {
        val server = MockWebServer()
        server.start()

        // Enqueue fake response
        server.enqueue(
            MockResponse()
                .setBody("""{"id": 1}""")
                .setResponseCode(201)
        )

        // Build Retrofit pointing to the mock server
        val retrofit = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)

        runBlocking {
            val post = api.createApplicant(Usertype.APPLICANT,"max ratjen","password")
            println("Mocked Response: $post")
        }

        server.shutdown()
    }
}

 */