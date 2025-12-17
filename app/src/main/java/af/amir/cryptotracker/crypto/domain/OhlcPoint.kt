package af.amir.cryptotracker.crypto.domain

data class OhlcPoint(
    val timestamp: Long,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double
)