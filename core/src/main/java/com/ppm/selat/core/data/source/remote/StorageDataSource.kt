package com.ppm.selat.core.data.source.remote

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.ppm.selat.core.data.source.remote.response.FirebaseResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

class StorageDataSource (
    private val firebaseStorage: FirebaseStorage,
    private val firebaseAuth: FirebaseAuth
) {
    suspend fun uploadProfilePicture(filePath: Uri): Flow<FirebaseResponse<String>> {
        return flow {
            try {
                val storageRef = firebaseStorage.reference
                val profilePictRef = storageRef.child("profilePicture/${firebaseAuth.currentUser?.uid.toString()}.png")
                val uploadTask = profilePictRef.putFile(filePath).await()
                val downloadUrl = uploadTask.storage.downloadUrl.await()
                emit(FirebaseResponse.Success(downloadUrl.toString()))
            } catch (e: StorageException) {
                emit(FirebaseResponse.Error(e.message.toString()))
            }
        }
    }
}