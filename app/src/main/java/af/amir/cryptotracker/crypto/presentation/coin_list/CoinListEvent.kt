package af.amir.cryptotracker.crypto.presentation.coin_list

import af.amir.cryptotracker.core.domain.util.NetworkError

sealed interface CoinListEvent {
    data class Error(val error: NetworkError) : CoinListEvent
    class NavigateToDetailScreen(val id: String) : CoinListEvent
}