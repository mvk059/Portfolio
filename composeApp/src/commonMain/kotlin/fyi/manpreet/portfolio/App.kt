package fyi.manpreet.portfolio

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fyi.manpreet.portfolio.navigation.CaptureComposableDestination
import fyi.manpreet.portfolio.navigation.ExpandableTextDestination
import fyi.manpreet.portfolio.navigation.FilterChipDropdownDestination
import fyi.manpreet.portfolio.navigation.HomeDestination
import fyi.manpreet.portfolio.navigation.StarFieldDestination
import fyi.manpreet.portfolio.ui.AppType
import fyi.manpreet.portfolio.ui.HomeScreen
import fyi.manpreet.portfolio.ui.HomeViewModel
import fyi.manpreet.portfolio.ui.apps.capturecomposable.CaptureComposable
import fyi.manpreet.portfolio.ui.apps.expandable_text.ExpandableText
import fyi.manpreet.portfolio.ui.apps.filterchip_dropdown.FilterChipDropdown
import fyi.manpreet.portfolio.ui.apps.starfield.StarField
import org.jetbrains.compose.ui.tooling.preview.Preview

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
                            AppType.EXPANDABLE_TEXT -> navController.navigate(ExpandableTextDestination)
                            AppType.FILTER_CHIP_DROPDOWN -> navController.navigate(FilterChipDropdownDestination)
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

            composable<ExpandableTextDestination> {
                val sampleText = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."
                ExpandableText(
                    text = sampleText,
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                )
            }

            composable<FilterChipDropdownDestination> {
                FilterChipDropdown()
            }
        }
    }
}