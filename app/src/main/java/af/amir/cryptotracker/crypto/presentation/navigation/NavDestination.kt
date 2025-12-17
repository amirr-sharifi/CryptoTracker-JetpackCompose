package af.amir.cryptotracker.crypto.presentation.navigation

sealed class NavDestination(val route: String) {
    data object CoinList : NavDestination("CoinList")
    data object CoinDetail : NavDestination("CoinDetail/{coinId}") {
        fun createRoute(coinId: String): String = "CoinDetail/$coinId"
    }
}