package af.amir.cryptotracker.crypto.presentation.coin_detail

import af.amir.cryptotracker.core.domain.util.NetworkError

sealed interface CoinDetailEvent {
    object NavigateUp : CoinDetailEvent
    data class Error(val error : NetworkError) : CoinDetailEvent
}