package af.amir.cryptotracker.crypto.presentation.coin_detail.component

import af.amir.cryptotracker.crypto.domain.OhlcPoint

enum class TimeUnit(val mills: Long, val label: String) {
    HOUR(3_600_000L, "hour"),
    DAY(86_400_000L, "day")
}

fun resolveTimeUnit(daysRange: Int) =
    when {
        (daysRange <= 1) -> TimeUnit.HOUR
        else -> TimeUnit.DAY
    }

fun List<OhlcPoint>.toVicoData(timeUnit: TimeUnit): VicoOhlcData {
    return VicoOhlcData(
        x = map { (it.timestamp / timeUnit.mills).toDouble() },
        opening = map { it.open },
        closing = map { it.close },
        low = map { it.low },
        high = map { it.high }
    )
}

data class VicoOhlcData(
    val x: List<Double>,
    val opening: List<Double>,
    val closing: List<Double>,
    val low: List<Double>,
    val high: List<Double>,
)
