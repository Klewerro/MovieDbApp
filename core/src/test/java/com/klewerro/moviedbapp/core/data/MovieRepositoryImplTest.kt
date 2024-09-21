package com.klewerro.moviedbapp.core.data

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.klewerro.moviedbapp.core.domain.LikeResult
import com.klewerro.moviedbapp.core.fake.FakeLikedMovieDao
import com.klewerro.moviedbapp.core.util.testData.MovieTestData
import io.mockk.spyk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MovieRepositoryImplTest {

    private lateinit var fakeLikedMovieDao: FakeLikedMovieDao
    private lateinit var fakeDateTimeProvider: FakeDateTimeProvider
    private lateinit var movieRepositoryImpl: MovieRepositoryImpl

    @BeforeEach
    fun setUp() {
        fakeLikedMovieDao = FakeLikedMovieDao()
        fakeDateTimeProvider = FakeDateTimeProvider()
        movieRepositoryImpl = MovieRepositoryImpl(
            movieApi = spyk(),
            likedMovieDao = fakeLikedMovieDao,
            dateTimeProvider = fakeDateTimeProvider
        )
    }

    @Test
    fun `likeMovie if movie is not liked add likedMovie using Dao`() {
        val movie = MovieTestData.movie1.copy(id = 50)
        runBlocking {
            val itemCountBeforeOperation = fakeLikedMovieDao.savedLikedMovies.value.size
            movieRepositoryImpl.likeMovie(movie)
            val itemCountAfterOperation = fakeLikedMovieDao.savedLikedMovies.value.size
            assertThat(itemCountAfterOperation).isEqualTo(itemCountBeforeOperation + 1)
        }
    }

    @Test
    fun `likeMovie if movie is not liked return LikeResult LIKED`() {
        val movie = MovieTestData.movie1.copy(id = 50)
        runBlocking {
            val result = movieRepositoryImpl.likeMovie(movie)
            assertThat(result).isEqualTo(LikeResult.LIKED)
        }
    }

    @Test
    fun `likeMovie if movie is liked removing likedMovie using Dao`() {
        val movie = MovieTestData.movie1.copy(id = 1)
        runBlocking {
            val itemCountBeforeOperation = fakeLikedMovieDao.savedLikedMovies.value.size
            movieRepositoryImpl.likeMovie(movie)
            val itemCountAfterOperation = fakeLikedMovieDao.savedLikedMovies.value.size
            assertThat(itemCountAfterOperation).isEqualTo(itemCountBeforeOperation - 1)
        }
    }

    @Test
    fun `likeMovie if movie is not liked return LikeResult LIKE_REMOVED`() {
        val movie = MovieTestData.movie1.copy(id = 1)
        runBlocking {
            val result = movieRepositoryImpl.likeMovie(movie)
            assertThat(result).isEqualTo(LikeResult.LIKE_REMOVED)
        }
    }

    @Test
    fun `likeMovie if movie is liked and Dao return error return LikeResult ERROR`() {
        val movie = MovieTestData.movie1.copy(id = 1)
        fakeLikedMovieDao.isError = true
        runBlocking {
            val result = movieRepositoryImpl.likeMovie(movie)
            assertThat(result).isEqualTo(LikeResult.ERROR)
        }
    }

    @Test
    fun `likeMovie if movie is not liked and Dao return error return LikeResult ERROR`() {
        val movie = MovieTestData.movie1.copy(id = 50)
        fakeLikedMovieDao.isError = true
        runBlocking {
            val result = movieRepositoryImpl.likeMovie(movie)
            assertThat(result).isEqualTo(LikeResult.ERROR)
        }
    }
}
