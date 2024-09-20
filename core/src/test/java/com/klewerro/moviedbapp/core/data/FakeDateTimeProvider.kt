package com.klewerro.moviedbapp.core.data

import com.klewerro.moviedbapp.core.domain.contract.DateTimeProvider

class FakeDateTimeProvider : DateTimeProvider {
    var time = 10901L

    override fun currentTimeInMillis(): Long = time
}
