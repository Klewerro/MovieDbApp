package com.klewerro.moviedbapp.presentation

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import com.klewerro.moviedbapp.core.domain.LikeResult
import com.klewerro.moviedbapp.core.domain.contract.MovieRepository
import com.klewerro.moviedbapp.core.util.testData.MovieTestData
import com.klewerro.moviedbapp.util.MainCoroutineExtension
import com.klewerro.moviedbapp.util.TestDispatchers
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

@Suppress("ktlint:standard:max-line-length")
class LikeMovieViewModelTest {

    @RelaxedMockK
    private lateinit var movieRepositoryMock: MovieRepository
    private lateinit var likeMovieViewModel: LikeMovieViewModel

    private val movie = MovieTestData.movie1

    companion object {
        @JvmField
        @RegisterExtension
        val mainCoroutineExtension = MainCoroutineExtension()
    }

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        val testDispatchers = TestDispatchers(mainCoroutineExtension.testDispatcher)
        likeMovieViewModel = LikeMovieViewModel(movieRepositoryMock, testDispatchers)
    }

    @Test
    fun `By default after creation isMovieLiked in state is null`() = runTest {
        likeMovieViewModel.likeMovieState.test {
            val firstEmission = awaitItem()
            assertThat(firstEmission.isMovieLiked).isNull()
        }
    }

    @Test
    fun `On ObserveMovieLikeStatus event viewModel instantly getting information about movie liked value`() {
        coEvery { movieRepositoryMock.observeMovieLikedStatus(any()) } returns flowOf(false)
        runTest {
            likeMovieViewModel.likeMovieState.test {
                skipItems(1)
                likeMovieViewModel.onEvent(LikeMovieEvent.ObserveMovieLikeStatus(movie.id))
                val secondItem = awaitItem()

                assertThat(secondItem.isMovieLiked).isNotNull()
            }
        }
    }

    @Test
    fun `On LikeMovie event repository likeMovie is called once, and if return LikeResult LIKED, state likeChanged value is set to Liked with movie title`() {
        coEvery { movieRepositoryMock.observeMovieLikedStatus(any()) } returns flow {
            emit(false)
            delay(1_000)
            emit(true)
        }
        runTest {
            likeMovieViewModel.likeMovieState.test {
                likeMovieViewModel.onEvent(LikeMovieEvent.ObserveMovieLikeStatus(movie.id))
                likeMovieViewModel.onEvent(LikeMovieEvent.LikeMovie(movie))
                skipItems(1)
                val secondItem = awaitItem()
                val thirdItem = awaitItem()

                val expectedLikeChanged = LikeChanged.Liked(movie.title)
                assertThat(secondItem.likeChanged).isEqualTo(expectedLikeChanged)
                assertThat(thirdItem.isMovieLiked).isEqualTo(true)
                coVerify(exactly = 1) { movieRepositoryMock.likeMovie(any()) }
            }
        }
    }

    @Test
    fun `On LikeMovie event repository likeMovie is called once, and if return LikeResult LIKE_REMOVED, state likeChanged value is set to LikeRemoved with movie title`() {
        coEvery { movieRepositoryMock.observeMovieLikedStatus(any()) } returns flow {
            emit(true)
            delay(1_000)
            emit(false)
        }
        coEvery { movieRepositoryMock.likeMovie(movie) } returns LikeResult.LIKE_REMOVED
        runTest {
            likeMovieViewModel.likeMovieState.test {
                likeMovieViewModel.onEvent(LikeMovieEvent.ObserveMovieLikeStatus(movie.id))
                likeMovieViewModel.onEvent(LikeMovieEvent.LikeMovie(movie))
                skipItems(1)
                val secondItem = awaitItem()
                val thirdItem = awaitItem()

                val expectedLikeChanged = LikeChanged.LikeRemoved(movie.title)
                assertThat(secondItem.likeChanged).isEqualTo(expectedLikeChanged)
                assertThat(thirdItem.isMovieLiked).isEqualTo(false)
                coVerify(exactly = 1) { movieRepositoryMock.likeMovie(any()) }
            }
        }
    }

    @Test
    fun `On StopObservingMovieLikeStatus event isMovieLikedStatus is set to null`() {
        coEvery { movieRepositoryMock.observeMovieLikedStatus(any()) } returns flowOf(true)
        runTest {
            likeMovieViewModel.likeMovieState.test {
                skipItems(1)
                likeMovieViewModel.onEvent(LikeMovieEvent.ObserveMovieLikeStatus(movie.id))
                val item2 = awaitItem()
                likeMovieViewModel.onEvent(LikeMovieEvent.StopObservingMovieLikeStatus)
                val item3 = awaitItem()
                assertThat(item2.isMovieLiked).isEqualTo(true)
                assertThat(item3.isMovieLiked).isNull()
            }
        }
    }
}
