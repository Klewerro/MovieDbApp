package com.klewerro.moviedbapp.di

import android.app.Application
import androidx.room.Room
import com.klewerro.moviedbapp.core.data.MovieRepositoryImpl
import com.klewerro.moviedbapp.core.data.local.LikedMovieDao
import com.klewerro.moviedbapp.core.data.local.MovieDatabase
import com.klewerro.moviedbapp.core.data.remote.MovieApi
import com.klewerro.moviedbapp.core.data.remote.TheMovieDbAuthInterceptor
import com.klewerro.moviedbapp.core.domain.contract.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMovieRepository(movieApi: MovieApi): MovieRepository = MovieRepositoryImpl(movieApi)

    @Provides
    @Singleton
    fun provideMovieApi(client: OkHttpClient): MovieApi = Retrofit.Builder()
        .client(client)
        .baseUrl(MovieApi.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create()

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(TheMovieDbAuthInterceptor())
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }
        )
        .build()

    @Provides
    @Singleton
    fun provideLikedMovieDao(movieDatabase: MovieDatabase) = movieDatabase.likedMovieDao

    @Provides
    @Singleton
    fun provideDatabase(app: Application): MovieDatabase = Room.databaseBuilder(
        app,
        MovieDatabase::class.java,
        "movie_db"
    ).build()
}
