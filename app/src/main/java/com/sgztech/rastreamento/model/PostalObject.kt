package com.sgztech.rastreamento.model

data class PostalObject(
    val categoria: String,
    val erro: String,
    val nome: String,
    val numero: String,
    val sigla: String,
    val evento: List<Evento>
)