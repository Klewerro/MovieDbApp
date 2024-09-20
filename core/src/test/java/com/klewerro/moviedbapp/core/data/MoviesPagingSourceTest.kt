package com.klewerro.moviedbapp.core.data

import androidx.paging.PagingSource
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isNull
import com.klewerro.moviedbapp.core.fake.FakeLikedMovieDao
import com.klewerro.moviedbapp.core.fake.FakeMovieApi
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.HttpException
import java.io.IOException

class MoviesPagingSourceTest {

    private lateinit var fakeMovieApi: FakeMovieApi
    private lateinit var fakeLikedMovieDao: FakeLikedMovieDao
    private lateinit var sut: MoviesPagingSource

    @BeforeEach
    fun setUp() {
        fakeMovieApi = FakeMovieApi()
        fakeLikedMovieDao = FakeLikedMovieDao()
        sut = MoviesPagingSource(
            movieApi = fakeMovieApi,
            likedMovieDao = fakeLikedMovieDao
        )
    }

    @Test
    fun `load when api response is not empty return LoadResult Page`() {
        runTest {
            val loadResult = sut.load(PagingSource.LoadParams.Refresh(null, 60, false))
            assertThat(loadResult).isInstanceOf(PagingSource.LoadResult.Page::class)
        }
    }

    @Test
    fun `load when api response have next page return LoadResult with page 2`() {
        fakeMovieApi.setTotalPages(2)
        runTest {
            val loadResult = sut.load(PagingSource.LoadParams.Refresh(null, 60, false))
            assertThat(loadResult).isInstanceOf(PagingSource.LoadResult.Page::class)
            assertThat((loadResult as PagingSource.LoadResult.Page).nextKey).isEqualTo(2)
        }
    }

    @Test
    fun `load when api response have not next page return LoadResult with page 2`() {
        runTest {
            val loadResult = sut.load(PagingSource.LoadParams.Refresh(null, 60, false))
            assertThat(loadResult).isInstanceOf(PagingSource.LoadResult.Page::class)
            assertThat((loadResult as PagingSource.LoadResult.Page).nextKey).isNull()
        }
    }

    @Test
    fun `load when api response have not movieDtos return IllegalStateException LoadResult Error`() {
        fakeMovieApi.response = fakeMovieApi.response.copy(movieDtos = emptyList())
        runTest {
            val loadResult = sut.load(PagingSource.LoadParams.Refresh(null, 60, false))
            assertThat(loadResult).isInstanceOf(PagingSource.LoadResult.Error::class)

            val errorLoadResult = (loadResult as PagingSource.LoadResult.Error)
            assertThat(errorLoadResult.throwable).isInstanceOf(IllegalStateException::class)
        }
    }

    @Test
    fun `load when http client throw HttpException return HttpException LoadResult Error`() {
        fakeMovieApi.exception = mockk<HttpException>()
        runTest {
            val loadResult = sut.load(PagingSource.LoadParams.Refresh(null, 60, false))

            assertThat(loadResult).isInstanceOf(PagingSource.LoadResult.Error::class)
            val errorLoadResult = (loadResult as PagingSource.LoadResult.Error)
            assertThat(errorLoadResult.throwable).isInstanceOf(HttpException::class)
        }
    }

    @Test
    fun `load when http client throw HttpException return IOException LoadResult Error`() {
        fakeMovieApi.exception = mockk<IOException>()
        runTest {
            val loadResult = sut.load(PagingSource.LoadParams.Refresh(null, 60, false))

            assertThat(loadResult).isInstanceOf(PagingSource.LoadResult.Error::class)
            val errorLoadResult = (loadResult as PagingSource.LoadResult.Error)
            assertThat(errorLoadResult.throwable).isInstanceOf(IOException::class)
        }
    }
}
