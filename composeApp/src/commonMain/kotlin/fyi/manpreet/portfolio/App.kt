package fyi.manpreet.portfolio

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fyi.manpreet.portfolio.navigation.CaptureComposableDestination
import fyi.manpreet.portfolio.navigation.HomeDestination
import fyi.manpreet.portfolio.navigation.StarFieldDestination
import fyi.manpreet.portfolio.ui.AppType
import fyi.manpreet.portfolio.ui.HomeScreen
import fyi.manpreet.portfolio.ui.HomeViewModel
import fyi.manpreet.portfolio.ui.apps.capturecomposable.CaptureComposable
import fyi.manpreet.portfolio.ui.apps.starfield.StarField
import org.jetbrains.compose.ui.tooling.preview.Preview

import portfolio.composeapp.generated.resources.Res
import portfolio.composeapp.generated.resources.compose_multiplatform

@Composable
@Preview
fun App(
    navController: NavHostController = rememberNavController(),
    viewModel: HomeViewModel = HomeViewModel(),
) {

    val apps = viewModel.apps.collectAsStateWithLifecycle()

    MaterialTheme {

        NavHost(
            navController = navController,
            startDestination = HomeDestination,
        ) {

            composable<HomeDestination> {
                HomeScreen(
                    apps = apps.value,
                    snackbarHostState = viewModel.snackbarHostState,
                    onAppClick = { type ->
                        when (type) {
                            AppType.EMPTY -> {}
                            AppType.STARFIELD -> navController.navigate(StarFieldDestination)
                            AppType.CAPTURE_COMPOSABLE -> navController.navigate(CaptureComposableDestination)
                            AppType.COMPOSABLE_MEME -> viewModel.showSnackBar(type)
                            AppType.BRIGHT_START -> viewModel.showSnackBar(type)
                        }
                    },
                )
            }

            composable<StarFieldDestination> {
                StarField()
            }

            composable<CaptureComposableDestination> {
                CaptureComposable()
            }
        }
    }
}