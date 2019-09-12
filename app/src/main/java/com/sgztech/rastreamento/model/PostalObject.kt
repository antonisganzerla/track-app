package com.sgztech.rastreamento.model

data class PostalObject(
    val code: String,
    val isDelivered: String,
    val postedAt: String,
    val updatedAt: String,
    val tracks: List<Track>
)