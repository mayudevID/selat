package com.ppm.selat.core.data.source.remote.response

sealed class FirebaseResponse<out R> {
    data class Success<out T>(val data: T) : FirebaseResponse<T>()
    data class Error(val errorMessage: String) : FirebaseResponse<Nothing>()
    object Empty : FirebaseResponse<Nothing>()
}