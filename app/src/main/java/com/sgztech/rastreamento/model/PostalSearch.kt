package com.sgztech.rastreamento.model

data class PostalSearch (
    val TipoPesquisa: String,
    val TipoResultado: String,
    val objeto: List<PostalObject>,
    val qtd: String,
    val versao: String
)