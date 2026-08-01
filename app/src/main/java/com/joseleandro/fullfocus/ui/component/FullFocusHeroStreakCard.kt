package com.joseleandro.fullfocus.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme

@Composable
fun FullFocusHeroStreakCard(
    modifier: Modifier = Modifier,
    streak: Int,
    highestStreak: Int
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.streak)
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        restartOnPlay = false,
        isPlaying = true,
        speed = 1f
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = .1f)
        ),
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Hero Visual
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary.copy(
                                alpha = 0.1f
                            ),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    LottieAnimation(
                        modifier = Modifier
                            .size(72.dp)
                            .graphicsLayer {
                                scaleX = 1.2f
                                scaleY = 1.2f
                            },
                        composition = composition,
                        progress = { progress },
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Streak Atual",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = streak.toString(),
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = " dias",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 4.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Record Badge
            StatBadge(
                modifier = Modifier.fillMaxWidth(),
                iconRes = R.drawable.streamline_sharp_star_badge_solid,
                label = "Melhor Sequência",
                value = "$highestStreak dias",
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
private fun StatBadge(
    modifier: Modifier = Modifier,
    iconRes: Int,
    label: String,
    value: String,
    color: Color
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = .1f)
        ),
        shape = MaterialTheme.shapes.extraLarge,
    ) {
        Row(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = color
            )
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FullFocusHeroStreakCardPreview() {
    FullFocusTheme(darkTheme = false, dynamicColor = false) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FullFocusHeroStreakCard(
                streak = 12,
                highestStreak = 21
            )
        }
    }
}
