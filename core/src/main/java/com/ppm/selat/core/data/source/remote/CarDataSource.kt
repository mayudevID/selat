package com.ppm.selat.core.data.source.remote

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.ppm.selat.core.data.source.remote.response.FirebaseResponse
import com.ppm.selat.core.domain.model.Car
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

class CarDataSource (private val firestore: FirebaseFirestore){
    suspend fun getAllCars() : Flow<FirebaseResponse<QuerySnapshot>>{
        return flow {
            try {
                val response = firestore.collection("cars").get().await()
                emit(FirebaseResponse.Success(response))
            } catch (e: FirebaseFirestoreException) {
                emit(FirebaseResponse.Error(e.message.toString()))
            }
        }
    }
}