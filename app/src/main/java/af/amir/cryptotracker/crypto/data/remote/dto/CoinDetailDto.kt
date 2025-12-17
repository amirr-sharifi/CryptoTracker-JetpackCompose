package af.amir.cryptotracker.crypto.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoinDetailDto(
    val id : String,
    val symbol : String,
    val name : String,
    val description : DescriptionDto,
    val image : ImageDto,
    @SerialName("market_cap_rank")
    val marketCapRank : Int,
    @SerialName("market_data")
    val marketData : MarketDataDto

)
