package com.icell.external.carlosformito.material3demo.ui.menu

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.icell.external.carlosformito.core.api.model.FormFieldValidationStrategy
import com.icell.external.carlosformito.material3demo.ui.fieldsamples.SampleFormScreen
import com.icell.external.carlosformito.material3demo.ui.fieldsamples.SamplesFormViewModel
import com.icell.external.carlosformito.material3demo.ui.fieldsamples.SamplesFormViewModelFactory
import com.icell.external.carlosformito.material3demo.ui.menu.Route.Companion.KEY_ARG_VALIDATION_STRATEGY

@Composable
fun MenuNavigator(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Route.MenuScreen.route
    ) {
        composable(Route.MenuScreen.route) {
            MenuScreen(
                onNavigateToFieldsSample = { validationStrategy ->
                    navController.navigate(Route.buildFieldSampleRoute(validationStrategy))
                }
            )
        }
        composable(
            route = Route.FieldSamples.route,
            arguments = listOf(
                navArgument(KEY_ARG_VALIDATION_STRATEGY) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val validationStrategy = FormFieldValidationStrategy.valueOf(
                requireNotNull(backStackEntry.arguments?.getString(KEY_ARG_VALIDATION_STRATEGY))
            )
            val viewModel: SamplesFormViewModel =
                viewModel(factory = SamplesFormViewModelFactory(validationStrategy))

            SampleFormScreen(
                title = "Built-in field samples",
                viewModel = viewModel,
                onBackPressed = {
                    navController.navigateUp()
                }
            )
        }
    }
}

sealed class Route(val route: String) {

    data object MenuScreen : Route("MenuRoute")
    data object FieldSamples : Route("$KEY_FIELD_SAMPLES_ROOT/{$KEY_ARG_VALIDATION_STRATEGY}")

    companion object {
        const val KEY_FIELD_SAMPLES_ROOT = "FieldSamples"
        const val KEY_ARG_VALIDATION_STRATEGY = "validationStrategy"

        fun buildFieldSampleRoute(validationStrategy: FormFieldValidationStrategy): String {
            return "$KEY_FIELD_SAMPLES_ROOT/${validationStrategy.name}"
        }
    }
}
