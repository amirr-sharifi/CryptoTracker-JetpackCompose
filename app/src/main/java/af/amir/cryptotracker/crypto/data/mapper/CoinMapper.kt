package af.amir.cryptotracker.crypto.data.mapper

import af.amir.cryptotracker.crypto.data.remote.dto.CoinDetailDto
import af.amir.cryptotracker.crypto.data.remote.dto.CoinDto
import af.amir.cryptotracker.crypto.data.remote.dto.OhlcDto
import af.amir.cryptotracker.crypto.domain.Coin
import af.amir.cryptotracker.crypto.domain.CoinDetail
import af.amir.cryptotracker.crypto.domain.OhlcPoint

fun CoinDto.toDomain(): Coin {
    return Coin(
        id = this.id,
        name = this.name,
        symbol = this.symbol,
        currentPrice = this.currentPrice,
        imageUrl = this.imageUrl,
        rank = this.rank,
        priceChangePercentage24h = this.priceChangePercentage24h ?: 0.0,
        sparkline = this.sparkline.price
    )
}


fun CoinDetailDto.toDomain(): CoinDetail {
    return CoinDetail(
        id = this.id,
        symbol = this.symbol,
        name = this.name,
        description = this.description.en,
        imgUrl = this.image.large,
        rank = this.marketCapRank,
        currentPrice = marketData.currentPrice.usd,
        allTimeHigh = marketData.ath.usd,
        allTimeLow = marketData.atl.usd,
        marketCap = marketData.marketCap.usd,
        totalVolume = marketData.totalVolume.usd,
        priceChangePercentage24h = marketData.priceChangePercentage24h,
        circulatingSupply = marketData.circulatingSupply,
        maxSupply = marketData.maxSupply,
        maxSupplyInfinite = marketData.maxSupplyInfinite,
    )
}

fun OhlcDto.toDomain() = OhlcPoint(
    timestamp, open, high, low, close
)

