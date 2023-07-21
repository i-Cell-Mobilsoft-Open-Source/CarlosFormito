package com.icell.external.carlosformito.ui.menu

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.icell.external.carlosformito.ui.fieldsamples.SampleFormScreen
import com.icell.external.carlosformito.ui.fieldsamples.SamplesFormViewModel

@Composable
fun MenuNavigator(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Route.SamplesMenu.route
    ) {
        composable(Route.SamplesMenu.route) {
            MenuScreen(
                onNavigateToFieldSamples = {
                    navController.navigate(Route.FieldSamples.route)
                }
            )
        }
        composable(Route.FieldSamples.route) {
            val viewModel: SamplesFormViewModel = viewModel()

            SampleFormScreen(
                viewModel = viewModel,
                onBackPressed = {
                    navController.navigateUp()
                }
            )
        }
    }
}

sealed class Route(val route: String) {

    object SamplesMenu : Route("MenuRoute")

    object FieldSamples : Route("FieldSamples")
}
