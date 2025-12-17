package af.amir.cryptotracker.crypto.domain

import af.amir.cryptotracker.core.domain.util.NetworkError
import af.amir.cryptotracker.core.domain.util.Result

interface CoinDataSource {

    suspend fun getCoins(page: Int, perPage: Int): Result<List<Coin>, NetworkError>
    suspend fun getCoinDetail(id: String): Result<CoinDetail, NetworkError>
    suspend fun getCoinOhlcData(id: String, inDays: Int = 1): Result<List<OhlcPoint>, NetworkError>
}