package hu.icellmobilsoft.carlosformito.material3demo.ui.menu

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import hu.icellmobilsoft.carlosformito.core.api.model.FormFieldValidationStrategy
import hu.icellmobilsoft.carlosformito.material3demo.ui.contextaware.ContextAwareValidationSampleScreen
import hu.icellmobilsoft.carlosformito.material3demo.ui.contextaware.ContextAwareValidationViewModel
import hu.icellmobilsoft.carlosformito.material3demo.ui.fieldsamples.SampleFormScreen
import hu.icellmobilsoft.carlosformito.material3demo.ui.fieldsamples.SamplesFormViewModel

@Composable
fun MenuNavigator(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Route.MenuScreen.route,
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
        composable(Route.MenuScreen.route) {
            MenuScreen(
                viewModel = viewModel<MenuViewModel>(),
                onNavigateToFieldsSample = { validationStrategy ->
                    Route.validationStrategy = validationStrategy
                    navController.navigate(Route.FieldSamples.route)
                },
                onContextAwareFieldValidationSample = { validationStrategy ->
                    Route.validationStrategy = validationStrategy
                    navController.navigate(Route.ContextAwareValidationSample.route)
                }
            )
        }

        composable(Route.FieldSamples.route) { backStackEntry ->
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

        composable(Route.ContextAwareValidationSample.route) {
            val viewModel: ContextAwareValidationViewModel = viewModel {
                ContextAwareValidationViewModel(Route.validationStrategy)
            }
            ContextAwareValidationSampleScreen(
                title = "Context-aware validation sample",
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
    data object FieldSamples : Route("FieldSamples")
    data object ContextAwareValidationSample : Route("ContextAwareValidationSample")

    companion object {
        var validationStrategy: FormFieldValidationStrategy = FormFieldValidationStrategy.Manual
    }
}
