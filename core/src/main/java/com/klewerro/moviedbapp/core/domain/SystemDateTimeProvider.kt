package com.klewerro.moviedbapp.core.domain

import com.klewerro.moviedbapp.core.domain.contract.DateTimeProvider

class SystemDateTimeProvider : DateTimeProvider {
    override fun currentTimeInMillis(): Long = System.currentTimeMillis()
}
