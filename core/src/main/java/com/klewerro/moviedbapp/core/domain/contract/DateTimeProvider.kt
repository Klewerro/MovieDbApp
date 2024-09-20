package com.klewerro.moviedbapp.core.domain.contract

interface DateTimeProvider {
    fun currentTimeInMillis(): Long
}
