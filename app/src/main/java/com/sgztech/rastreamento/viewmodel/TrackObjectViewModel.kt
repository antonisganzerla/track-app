package com.sgztech.rastreamento.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.sgztech.rastreamento.model.TrackObject
import com.sgztech.rastreamento.repository.TrackObjectRepository

class TrackObjectViewModel(private val repository: TrackObjectRepository) : ViewModel() {

    fun insert(trackObject: TrackObject) {
        repository.insert(trackObject)
    }

    fun delete(trackObject: TrackObject) {
        repository.delete(trackObject)
    }

    fun getAll(idUser: String): LiveData<List<TrackObject>> {
        return repository.getAll(idUser)
    }
}