package com.sgztech.rastreamento.api

import com.sgztech.rastreamento.model.PostalSearch
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PostalSearchService {

    @GET("?")
    fun findObject(
        @Query("objetos")
        code: String
    ): Call<PostalSearch>
}