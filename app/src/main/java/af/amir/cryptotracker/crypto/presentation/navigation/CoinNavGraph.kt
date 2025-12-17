package af.amir.cryptotracker.crypto.presentation.navigation

import af.amir.cryptotracker.crypto.presentation.coin_detail.CoinDetailScreen
import af.amir.cryptotracker.crypto.presentation.coin_list.CoinListScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun CoinNavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
) {

    NavHost(
        navHostController,
        modifier = modifier,
        startDestination = NavDestination.CoinList.route
    ) {
        composable(route = NavDestination.CoinList.route) {
            CoinListScreen { coinId ->
                navHostController.navigate(NavDestination.CoinDetail.createRoute(coinId))
            }
        }

        composable(
            route = NavDestination.CoinDetail.route,
            arguments = listOf(navArgument("coinId") {
                type = NavType.StringType
                defaultValue = ""
            })
        ) {navBackStackEntry ->
            val coinId = navBackStackEntry.arguments?.getString("coinId") ?: ""
            CoinDetailScreen(coinId = coinId){
                navHostController.navigateUp()
            }
        }

    }
}