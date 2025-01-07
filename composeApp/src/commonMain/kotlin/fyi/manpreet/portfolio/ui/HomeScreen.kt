package fyi.manpreet.portfolio.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random.Default.nextInt

@Composable
fun HomeScreen(
    apps: List<Apps>,
    snackbarHostState: SnackbarHostState,
    onAppClick: (AppType) -> Unit,
) {

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->

        LazyVerticalStaggeredGrid(
            modifier = Modifier.padding(padding),
            columns = StaggeredGridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalItemSpacing = 8.dp
        ) {
            items(
                items = apps,
            ) { app ->

                Box(
                    modifier = Modifier
                        .height(height = nextInt(from = 80, until = 200).dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(nextInt()).copy(alpha = 0.5f))
                        .clickable { onAppClick(app.type) },
                ) {

                    Column(
                        modifier = Modifier.fillMaxSize().padding(4.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {

                        Text(
                            text = app.title,
                            color = Color.White,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                        )

                        Text(
                            text = app.subtitle,
                            modifier = Modifier.padding(top = 4.dp),
                            color = Color.White,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            letterSpacing = 0.15.sp,
                        )
                    }
                }
            }
        }
    }
}