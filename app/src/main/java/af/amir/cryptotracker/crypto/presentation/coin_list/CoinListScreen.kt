package af.amir.cryptotracker.crypto.presentation.coin_list

import af.amir.cryptotracker.R
import af.amir.cryptotracker.core.presentation.ObserveAsEvents
import af.amir.cryptotracker.core.presentation.toString
import af.amir.cryptotracker.crypto.presentation.coin_list.components.CoinListContent
import af.amir.cryptotracker.crypto.presentation.coin_list.components.CoinUiItem
import af.amir.cryptotracker.crypto.presentation.components.ErrorButton
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel

@Composable
fun CoinListScreen(
    modifier: Modifier = Modifier,
    onNavigateToDetailScreen: (coinId: String) -> Unit,
) {
    val viewModel: CoinListViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    Log.e("CoinListState", "CoinListScreen: $state", )

    CoinListScreenStateless(
        modifier = modifier,
        state = state,
        events = viewModel.events,
        onAction = viewModel::onAction,
        onNavigateToDetailScreen = onNavigateToDetailScreen
    )


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinListScreenStateless(
    modifier: Modifier = Modifier,
    state: CoinListState,
    events: Flow<CoinListEvent>,
    onAction: (CoinListAction) -> Unit,
    onNavigateToDetailScreen: (coinId: String) -> Unit,
) {
    val context = LocalContext.current
    val snackBarState = remember { SnackbarHostState() }
    ObserveAsEvents(events) { event ->
        when (event) {
            is CoinListEvent.Error -> {
                val errorMessage = event.error.toString(context)
                snackBarState.currentSnackbarData?.dismiss()
                snackBarState.showSnackbar(errorMessage)
            }

            is CoinListEvent.NavigateToDetailScreen -> onNavigateToDetailScreen(event.id)
        }
    }
    Scaffold(
        modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        fontSize = MaterialTheme.typography.titleLarge.fontSize
                    )
                },
            )
        },
        snackbarHost = { SnackbarHost(snackBarState) }) { innerPadding ->

        when (state) {
            is CoinListState.Content -> CoinListContent(
                coinList = state.coinList,
                paginationState = state.paginationState,
                modifier = Modifier.padding(innerPadding),
                onAction = onAction
            )

            is CoinListState.Error -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding), contentAlignment = Alignment.Center) {
                    ErrorButton(null) {onAction(CoinListAction.RetryButtonClick) }
                }
            }
            CoinListState.Loading -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }


    }

}