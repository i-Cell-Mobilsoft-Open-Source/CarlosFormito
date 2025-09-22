package hu.icellmobilsoft.carlosformito.demo.ui.menu

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import hu.icellmobilsoft.carlosformito.core.api.model.FormFieldValidationStrategy
import hu.icellmobilsoft.carlosformito.demo.ui.custom.CustomFormScreen
import hu.icellmobilsoft.carlosformito.demo.ui.custom.CustomFormViewModel
import hu.icellmobilsoft.carlosformito.demo.ui.email.ChangeEmailScreen
import hu.icellmobilsoft.carlosformito.demo.ui.email.ChangeEmailViewModel
import hu.icellmobilsoft.carlosformito.demo.ui.fieldsamples.SampleFormScreen
import hu.icellmobilsoft.carlosformito.demo.ui.fieldsamples.SamplesFormViewModel
import hu.icellmobilsoft.carlosformito.demo.ui.password.UpdatePasswordScreen
import hu.icellmobilsoft.carlosformito.demo.ui.password.UpdatePasswordViewModel
import hu.icellmobilsoft.carlosformito.demo.ui.phonenumber.SetPhoneNumberScreen
import hu.icellmobilsoft.carlosformito.demo.ui.phonenumber.SetPhoneNumberViewModel

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
                    Route.validationStrategy = validationStrategy
                    navController.navigate(Route.FieldSamples.route)
                },
                onNavigateToCustomFormFieldsSample = { validationStrategy ->
                    Route.validationStrategy = validationStrategy
                    navController.navigate(Route.CustomFormFieldsSample.route)
                },
                onNavigateToLongRunningValidationSample = { validationStrategy ->
                    Route.validationStrategy = validationStrategy
                    navController.navigate(Route.LongRunningValidationSample.route)
                },
                onNavigateToMatchValidationSample = { validationStrategy ->
                    Route.validationStrategy = validationStrategy
                    navController.navigate(Route.EqualsToValidationSample.route)
                },
                onNavigateToConnectedFieldValidationSample = { validationStrategy ->
                    Route.validationStrategy = validationStrategy
                    navController.navigate(Route.ConnectedFieldValidationSample.route)
                }
            )
        }
        composable(
            route = Route.FieldSamples.route,
        ) { backstackEntry ->
            val viewModel: SamplesFormViewModel = viewModel {
                SamplesFormViewModel(Route.validationStrategy)
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
        ) { backstackEntry ->
            val viewModel: CustomFormViewModel = viewModel {
                CustomFormViewModel(Route.validationStrategy)
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
        ) { backstackEntry ->
            val viewModel: ChangeEmailViewModel = viewModel {
                ChangeEmailViewModel(Route.validationStrategy)
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
            route = Route.EqualsToValidationSample.route,
        ) { backstackEntry ->
            val viewModel: UpdatePasswordViewModel = viewModel {
                UpdatePasswordViewModel(Route.validationStrategy)
            }

            UpdatePasswordScreen(
                title = "EqualsTo validation sample",
                viewModel = viewModel,
                onBackPressed = {
                    navController.navigateUp()
                }
            )
        }
        composable(
            route = Route.ConnectedFieldValidationSample.route,
        ) { backstackEntry ->
            val viewModel: SetPhoneNumberViewModel = viewModel {
                SetPhoneNumberViewModel(Route.validationStrategy)
            }

            SetPhoneNumberScreen(
                title = "Connected fields validation sample",
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
    data object EqualsToValidationSample : Route("EqualsToValidationSample")
    data object ConnectedFieldValidationSample : Route("ConnectedFieldValidationSample")

    companion object {
        var validationStrategy: FormFieldValidationStrategy = FormFieldValidationStrategy.Manual
    }
}
