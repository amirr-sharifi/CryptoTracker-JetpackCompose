package af.amir.cryptotracker.crypto.presentation.coin_detail

import af.amir.cryptotracker.core.domain.util.NetworkError
import af.amir.cryptotracker.crypto.domain.CoinDetail
import af.amir.cryptotracker.crypto.domain.OhlcPoint
import java.math.BigDecimal

sealed interface CoinDetailState {
    data object Loading : CoinDetailState
    data class Error(val error: NetworkError) : CoinDetailState
    data class Content(
        val coin: CoinDetail = emptyCoinDetail,
        val ohlcDayRange: OhlcDayRange = OhlcDayRange.DAY1,
        val ohlcChartState: OhlcChartState = OhlcChartState.Loading,
    ) : CoinDetailState
}


sealed interface OhlcChartState {
    data object Loading : OhlcChartState
    data class Error(val error: NetworkError) : OhlcChartState
    data class Content(
        val ohlcPoints: List<OhlcPoint> = emptyList(),
    ) : OhlcChartState
}


enum class OhlcDayRange(val range: Int) {
    DAY1(1), DAY7(7), DAY30(30), DAY90(90)
}

internal val emptyCoinDetail = CoinDetail(
    "",
    "",
    "",
    "",
    "",
    1,
    BigDecimal.ZERO,
    BigDecimal.ZERO,
    BigDecimal.ZERO,
    BigDecimal.ZERO,
    BigDecimal.ZERO,
    0.0,
    BigDecimal.ZERO,
    BigDecimal.ZERO,
    false,
)
