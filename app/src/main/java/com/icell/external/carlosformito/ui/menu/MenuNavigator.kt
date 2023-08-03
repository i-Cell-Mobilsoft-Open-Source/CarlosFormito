package com.icell.external.carlosformito.ui.menu

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.icell.external.carlosformito.ui.custom.CustomFormScreen
import com.icell.external.carlosformito.ui.custom.CustomFormViewModel
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
                },
                onNavigateToCustomFormFields = {
                    navController.navigate(Route.CustomFormFields.route)
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
        composable(Route.CustomFormFields.route) {
            val viewModel: CustomFormViewModel = viewModel()

            CustomFormScreen(
                viewModel = viewModel,
                onBackPressed = {
                    navController.navigateUp()
                }
            )
        }
    }
}

sealed class Route(val route: String) {

    data object SamplesMenu : Route("MenuRoute")

    data object FieldSamples : Route("FieldSamples")

    data object CustomFormFields : Route("CustomFormFields")
}
