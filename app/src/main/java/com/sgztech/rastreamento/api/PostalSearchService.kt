package com.sgztech.rastreamento.api

import com.sgztech.rastreamento.model.PostalSearch
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PostalSearchService {

    @GET("track/{code}")
    fun findObject(
        @Path("code")
        code: String
    ): Call<PostalSearch>
}