package com.icell.external.carlosformito.demo.ui.menu

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.icell.external.carlosformito.core.api.model.FormFieldValidationStrategy
import com.icell.external.carlosformito.demo.ui.custom.CustomFormScreen
import com.icell.external.carlosformito.demo.ui.custom.CustomFormViewModel
import com.icell.external.carlosformito.demo.ui.email.ChangeEmailScreen
import com.icell.external.carlosformito.demo.ui.email.ChangeEmailViewModel
import com.icell.external.carlosformito.demo.ui.fieldsamples.SampleFormScreen
import com.icell.external.carlosformito.demo.ui.fieldsamples.SamplesFormViewModel
import com.icell.external.carlosformito.demo.ui.menu.Route.Companion.KEY_ARG_VALIDATION_STRATEGY
import com.icell.external.carlosformito.demo.ui.password.SetPasswordFormManager
import com.icell.external.carlosformito.demo.ui.password.SetPasswordScreen
import com.icell.external.carlosformito.demo.ui.password.SetPasswordViewModel

@Suppress("LongMethod")
@Composable
fun MenuNavigator(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Route.SamplesMenu.route,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween()
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween()
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween()
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween()
            )
        }
    ) {
        composable(Route.SamplesMenu.route) {
            MenuScreen(
                viewModel = viewModel<MenuViewModel>(),
                onNavigateToFieldSamples = { validationStrategy ->
                    navController.navigate("${Route.KEY_FIELD_SAMPLES_ROOT}/${validationStrategy.name}")
                },
                onNavigateToCustomFormFieldsSample = { validationStrategy ->
                    navController.navigate("${Route.KEY_CUSTOM_FORM_SAMPLES_ROOT}/${validationStrategy.name}")
                },
                onNavigateToLongRunningValidationSample = { validationStrategy ->
                    navController.navigate("${Route.KEY_LONG_RUNNING_SAMPLE_ROOT}/${validationStrategy.name}")
                },
                onNavigateToInterdependentFieldsSample = { validationStrategy ->
                    navController.navigate("${Route.KEY_INTERDEPENDENT_FIELDS_SAMPLE_ROOT}/${validationStrategy.name}")
                }
            )
        }
        composable(
            route = Route.FieldSamples.route,
            arguments = listOf(
                navArgument(KEY_ARG_VALIDATION_STRATEGY) { type = NavType.StringType }
            )
        ) { backstackEntry ->
            val validationStrategy = enumValueOf<FormFieldValidationStrategy>(
                requireNotNull(backstackEntry.arguments?.getString(KEY_ARG_VALIDATION_STRATEGY))
            )
            val viewModel: SamplesFormViewModel = viewModel {
                SamplesFormViewModel(validationStrategy)
            }

            SampleFormScreen(
                title = "Built-in field samples",
                viewModel = viewModel,
                onBackPressed = {
                    navController.navigateUp()
                }
            )
        }
        composable(
            route = Route.CustomFormFieldsSample.route,
            arguments = listOf(
                navArgument(KEY_ARG_VALIDATION_STRATEGY) { type = NavType.StringType }
            )
        ) { backstackEntry ->
            val validationStrategy = enumValueOf<FormFieldValidationStrategy>(
                requireNotNull(backstackEntry.arguments?.getString(KEY_ARG_VALIDATION_STRATEGY))
            )
            val viewModel: CustomFormViewModel = viewModel {
                CustomFormViewModel(validationStrategy)
            }

            CustomFormScreen(
                title = "Custom field samples",
                viewModel = viewModel,
                onBackPressed = {
                    navController.navigateUp()
                }
            )
        }
        composable(
            route = Route.LongRunningValidationSample.route,
            arguments = listOf(
                navArgument(KEY_ARG_VALIDATION_STRATEGY) { type = NavType.StringType }
            )
        ) { backstackEntry ->
            val validationStrategy = enumValueOf<FormFieldValidationStrategy>(
                requireNotNull(backstackEntry.arguments?.getString(KEY_ARG_VALIDATION_STRATEGY))
            )
            val viewModel: ChangeEmailViewModel = viewModel {
                ChangeEmailViewModel(validationStrategy)
            }

            ChangeEmailScreen(
                title = "Long running validation sample",
                viewModel = viewModel,
                onBackPressed = {
                    navController.navigateUp()
                }
            )
        }
        composable(
            route = Route.InterdependentFieldsSample.route,
            arguments = listOf(
                navArgument(KEY_ARG_VALIDATION_STRATEGY) { type = NavType.StringType }
            )
        ) { backstackEntry ->
            val validationStrategy = enumValueOf<FormFieldValidationStrategy>(
                requireNotNull(backstackEntry.arguments?.getString(KEY_ARG_VALIDATION_STRATEGY))
            )
            val viewModel: SetPasswordViewModel = viewModel {
                SetPasswordViewModel(SetPasswordFormManager(validationStrategy))
            }

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

    data object FieldSamples :
        Route("$KEY_FIELD_SAMPLES_ROOT/{$KEY_ARG_VALIDATION_STRATEGY}")

    data object CustomFormFieldsSample :
        Route("$KEY_CUSTOM_FORM_SAMPLES_ROOT/{$KEY_ARG_VALIDATION_STRATEGY}")

    data object LongRunningValidationSample :
        Route("$KEY_LONG_RUNNING_SAMPLE_ROOT/{$KEY_ARG_VALIDATION_STRATEGY}")

    data object InterdependentFieldsSample :
        Route("$KEY_INTERDEPENDENT_FIELDS_SAMPLE_ROOT/{$KEY_ARG_VALIDATION_STRATEGY}")

    companion object {
        const val KEY_FIELD_SAMPLES_ROOT = "FieldSamples"
        const val KEY_CUSTOM_FORM_SAMPLES_ROOT = "CustomFormFieldsSample"
        const val KEY_LONG_RUNNING_SAMPLE_ROOT = "LongRunningValidationSample"
        const val KEY_INTERDEPENDENT_FIELDS_SAMPLE_ROOT = "InterdependentFieldsSample"
        const val KEY_ARG_VALIDATION_STRATEGY = "validationStrategy"
    }
}
