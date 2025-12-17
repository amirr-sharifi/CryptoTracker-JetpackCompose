package af.amir.cryptotracker.crypto.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoinDto(
    @SerialName("current_price")
    val currentPrice: Double,
    val id: String,
    @SerialName("image")
    val imageUrl: String,
    @SerialName("market_cap_rank")
    val rank: Int,
    val name: String,
    @SerialName("price_change_percentage_24h")
    val priceChangePercentage24h: Double?,
    @SerialName("sparkline_in_7d")
    val sparkline: SparklineDto,
    val symbol: String,
)