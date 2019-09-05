package com.sgztech.rastreamento.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInitializer {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://correios.postmon.com.br/webservice/buscaEventos/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun service(): PostalSearchService {
        return retrofit.create(PostalSearchService::class.java)
    }
}