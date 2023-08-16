package com.icell.external.carlosformito.ui.menu

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.icell.external.carlosformito.api.model.FormFieldValidationStrategy
import com.icell.external.carlosformito.ui.custom.CustomFormScreen
import com.icell.external.carlosformito.ui.custom.CustomFormViewModel
import com.icell.external.carlosformito.ui.email.ChangeEmailScreen
import com.icell.external.carlosformito.ui.email.ChangeEmailViewModel
import com.icell.external.carlosformito.ui.fieldsamples.SampleFormScreen
import com.icell.external.carlosformito.ui.fieldsamples.SamplesFormViewModel
import com.icell.external.carlosformito.ui.fieldsamples.util.SamplesFormViewModelFactory
import com.icell.external.carlosformito.ui.menu.Route.Companion.KEY_ARG_VALIDATION_STRATEGY
import com.icell.external.carlosformito.ui.password.SetPasswordScreen
import com.icell.external.carlosformito.ui.password.SetPasswordViewModel

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
                onNavigateToFieldSamples = { validationStrategy ->
                    navController.navigate(Route.buildFieldSampleRoute(validationStrategy))
                },
                onNavigateToCustomFormFieldsSample = {
                    navController.navigate(Route.CustomFormFieldsSample.route)
                },
                onNavigateToLongRunningValidationSample = {
                    navController.navigate(Route.LongRunningValidationSample.route)
                },
                onNavigateToInterdependentFieldsSample = {
                    navController.navigate(Route.InterdependentFieldsSample.route)
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
        composable(Route.InterdependentFieldsSample.route) {
            val viewModel: SetPasswordViewModel = viewModel()

            SetPasswordScreen(
                title = "Interdependent fields sample",
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

    data object FieldSamples : Route("$KEY_FIELD_SAMPLES_ROOT/{$KEY_ARG_VALIDATION_STRATEGY}")

    data object CustomFormFieldsSample : Route("CustomFormFieldsSample")

    data object LongRunningValidationSample : Route("LongRunningValidationSample")

    data object InterdependentFieldsSample : Route("InterdependentFieldsSample")

    companion object {
        const val KEY_FIELD_SAMPLES_ROOT = "FieldSamples"
        const val KEY_ARG_VALIDATION_STRATEGY = "validationStrategy"

        fun buildFieldSampleRoute(validationStrategy: FormFieldValidationStrategy): String {
            return "$KEY_FIELD_SAMPLES_ROOT/${validationStrategy.name}"
        }
    }
}
