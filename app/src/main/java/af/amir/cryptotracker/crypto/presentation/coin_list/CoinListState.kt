package af.amir.cryptotracker.crypto.presentation.coin_list

import af.amir.cryptotracker.core.domain.util.NetworkError
import af.amir.cryptotracker.crypto.domain.Coin

sealed interface CoinListState {

    data object Loading : CoinListState
    data class Error(val error: NetworkError) : CoinListState
    data class Content(
        val coinList: List<Coin> = emptyList(),
        val paginationState: PaginationState = PaginationState.Idle,
    ) : CoinListState
}

sealed interface PaginationState {

    data object Idle : PaginationState
    data object Loading : PaginationState
    data object EndReached : PaginationState
    data class Error(val error : NetworkError) : PaginationState

}
