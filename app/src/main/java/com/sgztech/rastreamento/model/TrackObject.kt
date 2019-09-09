package com.sgztech.rastreamento.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity()
class TrackObject (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val code: String,
    val idUser: String
)