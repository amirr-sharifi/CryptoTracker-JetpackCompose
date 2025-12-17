package af.amir.cryptotracker.crypto.presentation.coin_detail

import af.amir.cryptotracker.core.domain.util.NetworkError
import af.amir.cryptotracker.core.domain.util.onError
import af.amir.cryptotracker.core.domain.util.onSuccess
import af.amir.cryptotracker.crypto.domain.CoinDataSource
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


class CoinDetailViewModel(
    val coinId: String,
    val coinDataSource: CoinDataSource,
) : ViewModel() {

    private val _state = MutableStateFlow<CoinDetailState>(CoinDetailState.Loading)
    val state = _state.onStart {
        loadCoinDetail()
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CoinDetailState.Loading)

    private val _events = Channel<CoinDetailEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private suspend fun loadCoinDetail() {
        _state.value = CoinDetailState.Loading
        coinDataSource.getCoinDetail(coinId)
            .onSuccess { coin ->
                _state.value = CoinDetailState.Content(coin = coin)
                loadOhlcChartData()
            }
            .onError {
                _state.value = CoinDetailState.Error(it)
                _events.send(CoinDetailEvent.Error(it))
            }
    }


    private suspend fun loadOhlcChartData() {
        if (_state.value !is CoinDetailState.Content) return
        val currentState = _state.value as CoinDetailState.Content
        _state.update {
            currentState.copy(
                ohlcChartState = OhlcChartState.Loading
            )
        }
        coinDataSource.getCoinOhlcData(coinId, currentState.ohlcDayRange.range)
            .onSuccess { pointList ->
                _state.update {
                    currentState.copy(
                        ohlcChartState =
                        if (pointList.isEmpty())
                            OhlcChartState.Error(NetworkError.Unknown)
                        else
                            OhlcChartState.Content(pointList),
                    )
                }


            }
            .onError { error ->
                _state.update {
                    currentState.copy(
                        ohlcChartState = OhlcChartState.Error(error)
                    )
                }
                _events.send(CoinDetailEvent.Error(error))
            }
    }

    private fun onOhlcTimeRangeChange(ohlcDayRange: OhlcDayRange) {
        var shouldLoadChart = false
        viewModelScope.launch {
            updateContentState {
                shouldLoadChart = ohlcDayRange != it.ohlcDayRange
                it.copy(ohlcDayRange = ohlcDayRange)
            }
            if (shouldLoadChart) loadOhlcChartData()
        }
    }


    fun onAction(action: CoinDetailAction) {
        when (action) {
            CoinDetailAction.BackClick -> viewModelScope.launch { _events.send(CoinDetailEvent.NavigateUp) }
            CoinDetailAction.RetryLoadChart -> viewModelScope.launch {
                loadOhlcChartData()
            }

            CoinDetailAction.RetryLoadCoinDetail -> viewModelScope.launch {
                loadCoinDetail()
            }

            is CoinDetailAction.OhlcTimeRangeChange -> onOhlcTimeRangeChange(action.ohlcDayRange)
        }
    }

    private fun updateContentState(update: (CoinDetailState.Content) -> CoinDetailState.Content) {
        if (_state.value !is CoinDetailState.Content) return
        val currentState = _state.value as CoinDetailState.Content
        _state.update { update(currentState) }
    }

}