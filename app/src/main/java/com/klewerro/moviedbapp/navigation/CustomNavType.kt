package com.klewerro.moviedbapp.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.klewerro.moviedbapp.core.domain.Movie
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object CustomNavType {
    val UserType = object : NavType<Movie>(isNullableAllowed = false) {
        // Probably for compatibility reasons for fragment impl
        override fun get(bundle: Bundle, key: String): Movie? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): Movie = Json.decodeFromString(Uri.decode(value))

        override fun serializeAsValue(value: Movie): String = Uri.encode(Json.encodeToString(value))

        // Probably for compatibility reasons for fragment impl
        override fun put(bundle: Bundle, key: String, value: Movie) {
            bundle.putString(key, Json.encodeToString(value))
        }
    }
}
