package com.joseleandro.fullfocus.data.local.preferences

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.joseleandro.fullfocus.data.local.preferences.model.Setting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object SettingsSerializer : Serializer<Setting> {

    override val defaultValue: Setting = Setting()

    override suspend fun readFrom(input: InputStream): Setting =
        try {
            Json.decodeFromString<Setting>(
                input.readBytes().decodeToString()
            )
        } catch (serialization: SerializationException) {
            throw CorruptionException("Unable to read Settings", serialization)
        }

    override suspend fun writeTo(t: Setting, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(t)
                    .encodeToByteArray()
            )
        }
    }
}

val Context.dataStore: DataStore<Setting> by dataStore(
    fileName = "settings.json",
    serializer = SettingsSerializer,
)