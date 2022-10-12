package com.ppm.selat.core.data.source.local

import android.content.SharedPreferences
import com.google.firebase.firestore.auth.User
import com.ppm.selat.core.data.Resource
import com.ppm.selat.core.domain.model.UserData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.serialization.json.Json
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserLocalDataSource @Inject constructor(private val sharedPreferences: SharedPreferences) {

    companion object {
        private const val DATA_USER = "DATA_USER"
    }

    private var userDataStream = Channel<UserData>()

    val userData = UserData(
        id = "NULL", name = "NULL", email = "NULL", photoUrl = "NULL"
    )

    init {
        val data = sharedPreferences.getString(DATA_USER, null)
        if (data != null) {
            flow<UserData> { userDataStream.send(Json.decodeFromString(UserData.serializer(), data))}
        } else {
            flow<UserData> { userDataStream.send(userData) }
        }
    }

    fun getDataStream() : Flow<UserData> {
        return userDataStream.receiveAsFlow()
    }

    fun saveUserData(newUserData: UserData) : Flow<Resource<Boolean>> {
        return flow {
            try {
                val editor = sharedPreferences.edit()
                editor.putString(DATA_USER, Json.encodeToString(UserData.serializer(), newUserData) )
                editor.commit()
                userDataStream.send(newUserData)
                emit(Resource.Success(true))
            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString()))
            }

        }
    }
}