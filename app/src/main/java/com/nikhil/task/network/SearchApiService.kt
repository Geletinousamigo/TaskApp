package com.nikhil.task.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface SearchApiService {

    @POST("/SearchQuery")
    suspend fun getSearchQueryResult(
        @Body searchRequestBody: SearchRequestBody
    ): Response<List<SearchResponseItem>>
}