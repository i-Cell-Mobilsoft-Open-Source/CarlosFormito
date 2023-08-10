package com.icell.external.carlosformito.ui.menu

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.icell.external.carlosformito.ui.custom.CustomFormScreen
import com.icell.external.carlosformito.ui.custom.CustomFormViewModel
import com.icell.external.carlosformito.ui.email.ChangeEmailScreen
import com.icell.external.carlosformito.ui.email.ChangeEmailViewModel
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
                onNavigateToCustomFormFieldsSample = {
                    navController.navigate(Route.CustomFormFieldsSample.route)
                },
                onNavigateToLongRunningValidationSample = {
                    navController.navigate(Route.LongRunningValidationSample.route)
                }
            )
        }
        composable(Route.FieldSamples.route) {
            val viewModel: SamplesFormViewModel = viewModel()

            SampleFormScreen(
                title = "Built-in field samples",
                viewModel = viewModel,
                onBackPressed = {
                    navController.navigateUp()
                }
            )
        }
        composable(Route.CustomFormFieldsSample.route) {
            val viewModel: CustomFormViewModel = viewModel()

            CustomFormScreen(
                title = "Custom field samples",
                viewModel = viewModel,
                onBackPressed = {
                    navController.navigateUp()
                }
            )
        }
        composable(Route.LongRunningValidationSample.route) {
            val viewModel: ChangeEmailViewModel = viewModel()

            ChangeEmailScreen(
                title = "Long running validation sample",
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

    data object CustomFormFieldsSample : Route("CustomFormFieldsSample")

    data object LongRunningValidationSample : Route("LongRunningValidationSample")
}
