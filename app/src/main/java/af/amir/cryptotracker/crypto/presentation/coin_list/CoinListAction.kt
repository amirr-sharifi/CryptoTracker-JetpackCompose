package af.amir.cryptotracker.crypto.presentation.coin_list

sealed interface CoinListAction {
    object LoadNextPage:CoinListAction
    data class CoinClick(val id : String) : CoinListAction
    object RetryButtonClick : CoinListAction
    object PaginationRetry : CoinListAction {

    }
}