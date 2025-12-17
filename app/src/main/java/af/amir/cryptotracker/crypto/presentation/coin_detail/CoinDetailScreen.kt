package af.amir.cryptotracker.crypto.presentation.coin_detail

import af.amir.cryptotracker.core.presentation.ObserveAsEvents
import af.amir.cryptotracker.core.presentation.toString
import af.amir.cryptotracker.crypto.presentation.coin_detail.component.CoinDetailContent
import af.amir.cryptotracker.crypto.presentation.components.ErrorButton
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinDetailScreen(
    coinId: String,
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
) {
    val viewModel: CoinDetailViewModel = koinViewModel(parameters = { parametersOf(coinId) })
    val state by viewModel.state.collectAsStateWithLifecycle()
    val events = viewModel.events



    CoinDetailScreenStateless(
        modifier = modifier,
        state = state,
        events = events,
        onAction = viewModel::onAction,
        onNavigateUp = onNavigateUp
    )


}


@ExperimentalMaterial3Api
@Composable
fun CoinDetailScreenStateless(
    modifier: Modifier = Modifier,
    state: CoinDetailState,
    events: Flow<CoinDetailEvent>,
    onAction: (CoinDetailAction) -> Unit,
    onNavigateUp: () -> Unit,
) {

    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }
    ObserveAsEvents(events) { event ->
        when (event) {
            CoinDetailEvent.NavigateUp -> onNavigateUp()
            is CoinDetailEvent.Error -> {
                snackBarHostState.currentSnackbarData?.dismiss()
                snackBarHostState.showSnackbar(
                    event.error.toString(context),
                    duration = SnackbarDuration.Long
                )
            }
        }
    }


    Scaffold(modifier.fillMaxSize(), topBar = {
        TopAppBar(
            title = { if (state is CoinDetailState.Content) Text(state.coin.name) },
            navigationIcon = {
                IconButton(onClick = { onAction(CoinDetailAction.BackClick) }) {
                    Icon(
                        Icons.Rounded.ArrowBack,
                        null
                    )
                }
            })
    },
        snackbarHost = { SnackbarHost(snackBarHostState) }) { innerPadding ->

        when (state) {
            is CoinDetailState.Error -> Box(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) { ErrorButton(null) { onAction(CoinDetailAction.RetryLoadCoinDetail) } }

            CoinDetailState.Loading -> Box(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }

            is CoinDetailState.Content -> CoinDetailContent(
                modifier = modifier.padding(innerPadding),
                state = state,
                onAction = onAction
            )
        }

    }


}

