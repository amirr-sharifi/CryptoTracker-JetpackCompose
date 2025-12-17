package af.amir.cryptotracker.crypto.presentation.coin_list

import af.amir.cryptotracker.core.data.helper.CheckInternetConnection
import af.amir.cryptotracker.core.domain.util.NetworkError
import af.amir.cryptotracker.core.domain.util.onError
import af.amir.cryptotracker.core.domain.util.onSuccess
import af.amir.cryptotracker.crypto.data.remote.CoinPaginator
import af.amir.cryptotracker.crypto.domain.Coin
import af.amir.cryptotracker.crypto.domain.CoinDataSource
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CoinListViewModel(
    private val coinDataSource: CoinDataSource,
    private val checkConnection: CheckInternetConnection,
) : ViewModel() {

    private val _events = Channel<CoinListEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private val _state = MutableStateFlow<CoinListState>(CoinListState.Loading)

    val state = _state
        .onStart {
            loadFirstPage()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CoinListState.Loading)

    private val paginator = CoinPaginator(
        onLoadUpdated = { isLoading ->
            if (isLoading)
                _state.update { currentState ->
                    if (currentState is CoinListState.Content)
                        currentState.copy(paginationState = PaginationState.Loading)
                    else currentState
                }
        },
        onRequest = {
            if (state.value is CoinListState.Content && (state.value as CoinListState.Content).paginationState == PaginationState.EndReached) null
            else
                coinDataSource.getCoins(page = it, perPage = 20)
        },
        onSuccess = { coins ->
            val canLoadMore = coins.isNotEmpty()

            _state.update { currentState ->
                if (currentState is CoinListState.Content)
                    currentState.copy(
                        paginationState = if (canLoadMore) PaginationState.Idle else PaginationState.EndReached,
                        coinList = currentState.coinList + coins
                    )
                else CoinListState.Content(
                    paginationState = if (canLoadMore) PaginationState.Idle else PaginationState.EndReached,
                    coinList = coins
                )
            }
        },
        onError = { error ->
            _state.update { currentState ->
                if (currentState is CoinListState.Content)
                    currentState.copy(
                        paginationState = PaginationState.Error(error),
                    )
                else currentState
            }
        },
        onFirstLoading = { isLoading ->
            if (isLoading) _state.value = CoinListState.Loading
        },
        onFirstLoadError = { error ->
            _state.value = CoinListState.Error(error)
            viewModelScope.launch { _events.send(CoinListEvent.Error(error)) }
        }
    )

    private suspend fun loadNextPage() {
        paginator.loadNextItems()
    }

    private suspend fun loadFirstPage() {
            if (!checkConnection.isConnected()) {
                val error = NetworkError.OFFLINE
                _state.value = CoinListState.Error(error)
                viewModelScope.launch { _events.send(CoinListEvent.Error(error)) }
            } else {
                paginator.loadFirstItems()
            }
    }


    fun onAction(action: CoinListAction) {
        when (action) {
            CoinListAction.LoadNextPage -> viewModelScope.launch { loadNextPage() }
            CoinListAction.RetryButtonClick -> viewModelScope.launch { loadFirstPage() }
            is CoinListAction.CoinClick -> {
                viewModelScope.launch { _events.send(CoinListEvent.NavigateToDetailScreen(action.id)) }
            }

            CoinListAction.PaginationRetry -> viewModelScope.launch { loadNextPage() }
        }
    }
}