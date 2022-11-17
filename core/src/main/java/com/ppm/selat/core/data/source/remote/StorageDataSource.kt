package com.ppm.selat.core.data.source.remote

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.ppm.selat.core.data.source.remote.network.FirebaseResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class StorageDataSource (
    private val firebaseStorage: FirebaseStorage,
) {
    suspend fun uploadProfilePicture(filePath: Uri, uid: String): Flow<FirebaseResponse<String>> {
        return flow {
            try {
                val storageRef = firebaseStorage.reference
                val profilePictRef = storageRef.child("profilePicture/$uid.png")
                val uploadTask = profilePictRef.putFile(filePath).await()
                val downloadUrl = uploadTask.storage.downloadUrl.await()
                emit(FirebaseResponse.Success(downloadUrl.toString()))
            } catch (e: StorageException) {
                emit(FirebaseResponse.Error(e.message.toString()))
            }
        }
    }

    suspend fun deleteProfilePicture(photoUrl: String, uid: String) : Flow<FirebaseResponse<Boolean>>{
        return flow {
            try {
                firebaseStorage.reference.storage.getReferenceFromUrl(photoUrl).delete().await();
                emit(FirebaseResponse.Success(true))
            } catch (e: StorageException) {
                emit(FirebaseResponse.Error(e.message.toString()))
            }
        }
    }
}