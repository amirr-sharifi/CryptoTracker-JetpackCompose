package af.amir.cryptotracker.crypto.presentation.coin_list.components

import af.amir.cryptotracker.R
import af.amir.cryptotracker.core.presentation.toString
import af.amir.cryptotracker.crypto.domain.Coin
import af.amir.cryptotracker.crypto.presentation.coin_list.CoinListAction
import af.amir.cryptotracker.crypto.presentation.coin_list.PaginationState
import af.amir.cryptotracker.crypto.presentation.components.ErrorButton
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun CoinListContent(
    coinList: List<Coin>,
    paginationState: PaginationState,
    pagingThreshold: Int = 3,
    modifier: Modifier = Modifier,
    onAction: (CoinListAction) -> Unit,
) {
    val context = LocalContext.current
    val scrollState = rememberLazyListState()
    val shouldLoadMore by remember {
        derivedStateOf {
            val layoutInfo = scrollState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            if (totalItems == 0) return@derivedStateOf false
            val lastIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index?:0

            lastIndex >= (totalItems - pagingThreshold)
        }
    }
    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) onAction(CoinListAction.LoadNextPage)
    }
    LazyColumn(
        modifier = modifier
            .fillMaxWidth(),
        state = scrollState,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        item {
            Text(
                text = stringResource(R.string.coins),
                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }

        items(coinList, key = { coin -> coin.id }) { coin ->
            CoinUiItem(
                modifier = Modifier.padding(horizontal = 4.dp), coin = coin
            ) { onAction(CoinListAction.CoinClick(coin.id)) }
        }

        item {
            when (paginationState) {
                PaginationState.Loading -> {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp), contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is PaginationState.Error -> {
                    ErrorButton(paginationState.error.toString(context)) { onAction(CoinListAction.PaginationRetry) }
                }

                else -> Unit
            }
        }


    }
}

