package com.sgztech.rastreamento.model

data class Evento (
    val cidade: String,
    val codigo: String,
    val comentario: String,
    val data: String,
    val descricao: String,
    val hora: String,
    val local: String,
    val tipo: String,
    val status: String,
    val uf: String,
    val destino: List<Destino>
)