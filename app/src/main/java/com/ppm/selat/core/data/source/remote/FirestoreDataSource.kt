package com.ppm.selat.core.data.source.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.ppm.selat.core.data.Resource
import com.ppm.selat.core.domain.model.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
) {
    suspend fun getUserDataFromFirestore(uid: String): Flow<Resource<DocumentSnapshot>> {
        return flow {
            emit(Resource.Loading())
            try {
                val user = firestore.collection("users").document(uid).get().await()
                emit(Resource.Success(user))
            } catch (e: FirebaseFirestoreException) {
                emit(Resource.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun createUserDataToFirestore(user: UserData): Flow<Resource<Boolean>>{
        return flow {
            emit(Resource.Loading())
            try {
                val userData = hashMapOf(
                    "name" to user.name,
                    "email" to user.email,
                    "photoUrl" to user.photoUrl
                )
                firestore.collection("users").document(user.id as String).set(userData).await()
                emit(Resource.Success(true))
            } catch (e: FirebaseFirestoreException) {
                emit(Resource.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun uploadProfilePicture() {

    }

    suspend fun updateName(name: String, uid: String): Flow<Resource<Boolean>>{
        return flow {
            emit(Resource.Loading())
            try {
                val nameData = mapOf(
                    "name" to name,
                )
                firestore.collection("users").document(uid.toString()).update(nameData).await()
                emit(Resource.Success(true))
            } catch (e: FirebaseFirestoreException) {
                emit(Resource.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

//    Future<void> updatePhoto(String url) async
//    {
//        firestore.collection('users').document(currentUser.id).update(
//            {
//                "photoUrl": url,
//            },
//        );
//    }
}