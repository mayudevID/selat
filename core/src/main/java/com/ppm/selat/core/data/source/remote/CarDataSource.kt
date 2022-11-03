package com.ppm.selat.core.data.source.remote

import com.google.firebase.firestore.*
import com.ppm.selat.core.data.source.remote.response.FirebaseResponse
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await

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

    fun getAvailableCar(carId: String) : Flow<Int> {
        return firestore.collection("cars").document(carId).snapshotFlow().map {
            it["available"].toString().toInt()
        }
    }

    private fun DocumentReference.snapshotFlow(): Flow<DocumentSnapshot> = callbackFlow {
        val listenerRegistration = addSnapshotListener { value, error ->
            if (error != null) {
                close()
                return@addSnapshotListener
            }
            if (value != null)
                trySend(value)
        }
        awaitClose {
            listenerRegistration.remove()
        }
    }

}