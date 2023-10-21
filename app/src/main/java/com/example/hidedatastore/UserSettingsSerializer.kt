package com.example.hidedatastore

import androidx.datastore.core.Serializer
import com.example.hidedatastore.ui.theme.CryptoManager
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream



@Serializable
data class UserSettings(
    @Contextual
    val username:String?=null,
    val password:String?=null
)
class UserSettingsSerializer (private val cryptoManager: CryptoManager):Serializer<UserSettings>{

    override val defaultValue: UserSettings
        get() = UserSettings()

    override suspend fun readFrom(input: InputStream): UserSettings {
        val decryptedBytes=cryptoManager.decrypt(input)


        return try {
            Json.decodeFromString(UserSettings.serializer(),decryptedBytes.decodeToString())
        }catch (e:SerializationException){
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: UserSettings, output: OutputStream){
        cryptoManager.encrypt(bytes = Json.encodeToString( UserSettings.serializer(),t).encodeToByteArray(), outputStream = output)
    }
}