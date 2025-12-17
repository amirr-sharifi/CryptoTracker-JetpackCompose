package af.amir.cryptotracker.crypto.domain

data class Coin(
    val id: String,
    val name: String,
    val symbol: String,
    val imageUrl: String,
    val rank: Int,
    val currentPrice: Double,
    val priceChangePercentage24h: Double,
    val sparkline: List<Double>,
)