package af.amir.cryptotracker.crypto.data.remote

import af.amir.cryptotracker.core.domain.util.NetworkError
import af.amir.cryptotracker.core.domain.util.Result
import af.amir.cryptotracker.core.domain.util.onError
import af.amir.cryptotracker.core.domain.util.onSuccess
import af.amir.cryptotracker.crypto.domain.Coin

class CoinPaginator(
    private val initialPage: Int = 1,
    private val onLoadUpdated: (Boolean) -> Unit,
    private val onRequest:suspend (page: Int) -> Result<List<Coin>, NetworkError>?,
    private val onSuccess: (items: List<Coin>) -> Unit,
    private val onError: (error: NetworkError) -> Unit,
    private val onFirstLoading: (Boolean) -> Unit,
    private val onFirstLoadError: (error: NetworkError) -> Unit,
) {

    private var currentPage = initialPage
    private var isMakingRequest = false

    suspend fun loadFirstItems(){
        if (isMakingRequest) return

        isMakingRequest = true
        onFirstLoading(true)

        val result = onRequest(initialPage)
        isMakingRequest = false
        if (result == null ) return
        result
            .onError {
                onFirstLoading(false)
                onFirstLoadError(it)
                return
            }
            .onSuccess {items->
                onFirstLoading(false)
                if (items.isNotEmpty()) currentPage++
                onSuccess(items)
            }
    }

    suspend fun loadNextItems() {
        if (isMakingRequest) return

        isMakingRequest = true
        onLoadUpdated(true)

        val result = onRequest(currentPage)
        isMakingRequest = false
        if (result == null ) return
        result
            .onError {
                onLoadUpdated(false)
                onError(it)
                return
            }
            .onSuccess {items->
                onLoadUpdated(false)
                if (items.isNotEmpty()) currentPage++
                onSuccess(items)
            }
    }


}