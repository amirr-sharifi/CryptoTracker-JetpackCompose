package af.amir.cryptotracker.crypto.data.remote.dto

import af.amir.cryptotracker.crypto.data.remote.serializer.BigDecimalSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class MarketDataDto(
    @SerialName("current_price")
    val currentPrice : PriceDto,
    val ath : PriceDto,
    val atl : PriceDto,
    @SerialName("market_cap")
    val marketCap : PriceDto,
    @SerialName("total_volume")
    val totalVolume: PriceDto,
    @SerialName("price_change_percentage_24h")
    val priceChangePercentage24h : Double,
    @SerialName("circulating_supply")
    @Serializable(with = BigDecimalSerializer::class)
    val circulatingSupply: BigDecimal? = null,
    @SerialName("max_supply")
    @Serializable(with = BigDecimalSerializer::class)
    val maxSupply : BigDecimal? = null,
    @SerialName("max_supply_infinite")
    val maxSupplyInfinite: Boolean,
)
