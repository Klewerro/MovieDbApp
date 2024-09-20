package com.klewerro.moviedbapp.core.util.testData

import com.klewerro.moviedbapp.core.domain.Movie

object MovieTestData {
    val movie1 = Movie(
        id = 1,
        title = "Ice Age",
        popularity = 83.45,
        backdropPath = "/backdropPath123.com",
        posterPath = "/posterPath123.com",
        genreIds = listOf(1, 2, 3, 4),
        originalLanguage = "en",
        originalTitle = "Ice Ace",
        overview = """With the impending ice age almost upon them, a mismatched trio of prehistoric 
            critters – Manny the woolly mammoth, Diego the saber-toothed tiger and Sid the giant sloth – 
            find an orphaned infant and decide to return it to its human parents. 
            Along the way, the unlikely allies become friends but, when enemies attack, 
            their quest takes on far nobler aims.""",
        releaseDate = "2002-03-10",
        video = false,
        voteAverage = 7.31,
        voteCount = 13_000,
        adult = false,
        isLiked = true
    )
    val movie2 = Movie(
        id = 2,
        title = "Mission Impossible",
        popularity = 40.1,
        backdropPath = "/backdropPath456.com",
        posterPath = "/posterPath456.com",
        genreIds = listOf(7, 8),
        originalLanguage = "en",
        originalTitle = "Mission Impossible",
        overview = """When Ethan Hunt, the leader of a crack espionage team whose perilous operation
            has gone awry with no explanation, discovers that a mole has penetrated the CIA, 
            he's surprised to learn that he's the prime suspect. To clear his name, 
            Hunt now must ferret out the real double agent and, in the process, even the score.""",
        releaseDate = "1996-05-22",
        video = false,
        voteAverage = 6.988,
        voteCount = 8712,
        adult = false,
        isLiked = false
    )
}
