package com.klewerro.moviedbapp.movies.presentation.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import com.klewerro.moviedbapp.core.presentation.LocalSpacing
import com.klewerro.moviedbapp.core.ui.theme.MovieDbAppTheme
import com.klewerro.moviedbapp.core.R as RCore

@Composable
fun ErrorMessageRefreshCard(
    errorLoadState: LoadState,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer
        ),
        modifier = modifier
            .padding(spacing.spaceNormal)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing.spaceNormal),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val errorMsg =
                (errorLoadState as LoadState.Error).error.message
            Text(
                text = errorMsg ?: "Unknown error.",
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            Spacer(modifier = Modifier.height(spacing.spaceNormal))
            OutlinedButton(
                onClick = {
                    onRetryClick()
                },
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.error)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Retry fetch items",
                        tint = MaterialTheme.colorScheme.onErrorContainer
                    )
                    Spacer(modifier = Modifier.width(spacing.spaceSmall))
                    Text(
                        stringResource(RCore.string.retry),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun ErrorMessageRefreshCardPreview() {
    MovieDbAppTheme {
        ErrorMessageRefreshCard(
            LoadState.Error(Exception("Exception message")),
            {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}
