package af.amir.cryptotracker.crypto.data.remote

import af.amir.cryptotracker.core.data.remote.constructUrl
import af.amir.cryptotracker.core.data.remote.safeCall
import af.amir.cryptotracker.core.domain.util.NetworkError
import af.amir.cryptotracker.core.domain.util.Result
import af.amir.cryptotracker.core.domain.util.map
import af.amir.cryptotracker.crypto.data.mapper.toDomain
import af.amir.cryptotracker.crypto.data.remote.dto.CoinDetailDto
import af.amir.cryptotracker.crypto.data.remote.dto.CoinDto
import af.amir.cryptotracker.crypto.data.remote.dto.OhlcDto
import af.amir.cryptotracker.crypto.domain.Coin
import af.amir.cryptotracker.crypto.domain.CoinDataSource
import af.amir.cryptotracker.crypto.domain.CoinDetail
import af.amir.cryptotracker.crypto.domain.OhlcPoint
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import java.math.BigDecimal

class RemoteCoinDataSource(private val httpClient: HttpClient) : CoinDataSource {
    override suspend fun getCoins(page: Int, perPage: Int): Result<List<Coin>, NetworkError> {
        return safeCall<List<CoinDto>> {
            httpClient.get(urlString = constructUrl("/coins/markets")) {
                parameter("vs_currency", "usd")
                parameter("order", "market_cap_desc")
                parameter("per_page", "$perPage")
                parameter("page", "$page")
                parameter("sparkline", true)
                parameter("price_change_percentage", "24h")
            }
        }.map { coinListDto ->
            coinListDto.map { it.toDomain() }
        }
    }

    override suspend fun getCoinDetail(id: String): Result<CoinDetail, NetworkError> {
        return safeCall<CoinDetailDto> {
            httpClient.get(urlString = constructUrl("/coins/$id"))
        }.map { dto ->
            dto.toDomain()
        }
    }

    override suspend fun getCoinOhlcData(
        id: String,
        inDays: Int,
    ): Result<List<OhlcPoint>, NetworkError> {
        return safeCall<List<OhlcDto>> {
            httpClient.get(urlString = constructUrl("coins/$id/ohlc?vs_currency=usd&days=$inDays"))
        }.map { ohlc ->
            ohlc.map {
                it.toDomain()
            }
        }
    }
}