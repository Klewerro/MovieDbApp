package com.klewerro.moviedbapp.movies.presentation.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import retrofit2.HttpException
import java.io.IOException
import com.klewerro.moviedbapp.core.R as RCore

@Composable
fun communicationErrorMessage(throwable: Throwable): String = when (throwable) {
    is HttpException -> handleHttpError(throwable)
    is NullPointerException -> stringResource(RCore.string.serialization_error_message)
    is IOException -> stringResource(RCore.string.missing_internet_connection_message)
    is IllegalStateException -> stringResource(RCore.string.fetched_data_empty_message)
    else -> stringResource(RCore.string.unknown_communication_error_message)
}

@Composable
fun handleHttpError(httpException: HttpException): String = when {
    httpException.code() in 401..403 -> stringResource(
        RCore.string.authentication_error_message,
        httpException.code()
    )

    httpException.code() in 400..499 -> stringResource(
        RCore.string.client_error_message,
        httpException.code()
    )

    httpException.code() in 500..599 -> stringResource(
        RCore.string.server_error_message,
        httpException.code()
    )

    else -> stringResource(RCore.string.unknown_communication_error_message)
}
