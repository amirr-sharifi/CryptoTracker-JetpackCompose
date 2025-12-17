package af.amir.cryptotracker.crypto.domain

import java.math.BigDecimal


data class CoinDetail(
    val id : String,
    val symbol : String,
    val name : String,
    val description: String,
    val imgUrl : String,
    val rank : Int,
    val currentPrice : BigDecimal?,
    val allTimeHigh :BigDecimal?,
    val allTimeLow : BigDecimal?,
    val marketCap :  BigDecimal?,
    val totalVolume : BigDecimal?,
    val priceChangePercentage24h : Double,
    val circulatingSupply : BigDecimal?,
    val maxSupply : BigDecimal?,
    val maxSupplyInfinite: Boolean,
)
