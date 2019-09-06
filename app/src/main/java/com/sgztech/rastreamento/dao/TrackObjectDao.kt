package com.sgztech.rastreamento.dao

import androidx.room.*
import com.sgztech.rastreamento.model.TrackObject

@Dao
interface TrackObjectDao {

    @Query("SELECT * FROM TRACKOBJECT")
    fun all(): List<TrackObject>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(vararg trackObject: TrackObject)

    @Update
    fun update(trackObject: TrackObject)

    @Delete
    fun delete(trackObject: TrackObject)
}