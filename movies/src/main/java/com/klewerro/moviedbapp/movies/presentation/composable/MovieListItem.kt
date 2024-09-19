package com.klewerro.moviedbapp.movies.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.klewerro.moviedbapp.core.domain.Movie
import com.klewerro.moviedbapp.core.presentation.LocalSpacing
import com.klewerro.moviedbapp.core.ui.theme.MovieDbAppTheme
import com.klewerro.moviedbapp.core.util.testData.MovieTestData
import com.klewerro.moviedbapp.core.R as RCore

@Composable
fun MovieListItem(movie: Movie, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(contentColor = Color.Transparent)
    ) {
        val spacing = LocalSpacing.current

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
//            // Preview purposes
//            Image(
//                painter = painterResource(R.drawable.small),
//                contentDescription = "Cover",
//                contentScale = ContentScale.Crop,
//                modifier = Modifier.fillMaxSize()
//            )
            AsyncImage(
                model = movie.poster500Url,
                contentDescription = stringResource(RCore.string.movie_poster),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black
                            ),
                            startY = 400f,
                            endY = 700f
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .padding(spacing.spaceNormal)
                        .align(Alignment.BottomStart)
                ) {
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                    Text(text = movie.releaseYear, color = Color.White)
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun MovieListItemPreview() {
    MovieDbAppTheme {
        MovieListItem(
            movie = MovieTestData.movie1,
            modifier = Modifier.size(180.dp, 270.dp)
        )
    }
}
