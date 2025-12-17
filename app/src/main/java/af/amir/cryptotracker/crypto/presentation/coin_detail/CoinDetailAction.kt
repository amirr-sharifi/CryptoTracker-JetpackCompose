package af.amir.cryptotracker.crypto.presentation.coin_detail

sealed interface CoinDetailAction {
    class OhlcTimeRangeChange(val ohlcDayRange: OhlcDayRange) : CoinDetailAction
    object BackClick : CoinDetailAction
    object RetryLoadCoinDetail : CoinDetailAction
    object RetryLoadChart : CoinDetailAction

}