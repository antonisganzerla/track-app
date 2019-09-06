package com.sgztech.rastreamento.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["code"], unique = true)])
class TrackObject (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val code: String
)