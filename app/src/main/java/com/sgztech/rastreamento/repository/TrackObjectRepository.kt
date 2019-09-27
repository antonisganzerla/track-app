package com.sgztech.rastreamento.repository

import androidx.lifecycle.LiveData
import com.sgztech.rastreamento.dao.TrackObjectDao
import com.sgztech.rastreamento.model.TrackObject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TrackObjectRepository(private val dao: TrackObjectDao) {

    fun insert(trackObject: TrackObject) {
        GlobalScope.launch {
            dao.add(trackObject)
        }
    }

    fun delete(trackObject: TrackObject) {
        GlobalScope.launch {
            dao.delete(trackObject)
        }
    }

    fun getAll(idUser: String): LiveData<List<TrackObject>> {
        return dao.allByUser(idUser)
    }
}