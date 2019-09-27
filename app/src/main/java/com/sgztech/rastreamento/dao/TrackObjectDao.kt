package com.sgztech.rastreamento.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sgztech.rastreamento.model.TrackObject

@Dao
interface TrackObjectDao {

    @Query("SELECT * FROM TrackObject WHERE idUser LIKE :idUser")
    fun allByUser(idUser: String): LiveData<List<TrackObject>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(vararg trackObject: TrackObject)

    @Update
    fun update(trackObject: TrackObject)

    @Delete
    fun delete(trackObject: TrackObject)
}