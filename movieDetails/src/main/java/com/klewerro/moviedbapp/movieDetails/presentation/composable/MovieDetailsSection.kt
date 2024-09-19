package com.klewerro.moviedbapp.movieDetails.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.klewerro.moviedbapp.core.domain.Movie
import com.klewerro.moviedbapp.core.presentation.LocalSpacing

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MovieDetailsSection(movie: Movie, modifier: Modifier = Modifier) {
    val spacing = LocalSpacing.current

    Column(
        modifier = modifier
            .padding(
                start = spacing.spaceSmall,
                top = spacing.spaceSmall,
                end = spacing.spaceSmall
            )
            .background(
                shape = RoundedCornerShape(
                    topStart = spacing.radiusSmall,
                    topEnd = spacing.radiusSmall
                ),
                color = MaterialTheme.colorScheme.primaryContainer
            )
            .padding(
                start = spacing.spaceNormal,
                top = spacing.spaceNormal,
                end = spacing.spaceNormal,
                bottom = spacing.spaceLarge
            )
    ) {
        Row {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(spacing.spaceSmall),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = movie.title,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    style = MaterialTheme.typography.headlineLarge
                )
                if (movie.originalTitle != movie.title) {
                    Text(
                        text = "(${movie.originalTitle})",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.headlineLarge,
                        fontStyle = FontStyle.Italic
                    )
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                VoteProgressIndicator(movie.voteAverage, modifier = Modifier.size(52.dp))
                Spacer(modifier = Modifier.height(spacing.spaceExtraSmall))
                Text(
                    text = "(${movie.voteCount})",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Text(
            text = movie.releaseYear,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(spacing.spaceNormal))
        Text(
            text = movie.overview,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
