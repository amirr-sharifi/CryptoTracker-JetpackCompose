package af.amir.cryptotracker.crypto.presentation.coin_detail.component


import af.amir.cryptotracker.R
import af.amir.cryptotracker.core.presentation.formatPrice
import af.amir.cryptotracker.core.presentation.formatToShortClean
import af.amir.cryptotracker.crypto.presentation.coin_detail.CoinDetailAction
import af.amir.cryptotracker.crypto.presentation.coin_detail.CoinDetailState
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import java.util.Locale

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CoinDetailContent(
    state: CoinDetailState.Content,
    modifier: Modifier = Modifier,
    onAction: (CoinDetailAction) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {

        //Icon , name, symbol, price
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                GlideImage(
                    model = state.coin.imgUrl,
                    contentDescription = "",
                    loading = placeholder(R.drawable.ic_loading),
                    failure = placeholder(R.drawable.ic_failure),
                    modifier = Modifier
                        .size(80.dp)
                        .padding(8.dp),
                    contentScale = ContentScale.Crop
                )
                Column(verticalArrangement = Arrangement.Center, modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            modifier = Modifier.basicMarquee(),
                            text = state.coin.name,
                            maxLines = 1,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = state.coin.symbol.uppercase(Locale.getDefault()),
                            maxLines = 1,
                            modifier = Modifier.basicMarquee(),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                            fontSize = MaterialTheme.typography.bodySmall.fontSize
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "$" + state.coin.currentPrice?.formatPrice(),
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize
                        )
                        Spacer(Modifier.width(8.dp))
                        val pcp = state.coin.priceChangePercentage24h
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        color = if (pcp < 0) Color.Red else Color.Green,
                                        fontSize = MaterialTheme.typography.bodySmall.fontSize
                                    )
                                ) {
                                    append(pcp.formatPrice())
                                }
                                withStyle(
                                    style = SpanStyle(
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                                        fontSize = MaterialTheme.typography.bodySmall.fontSize
                                    )
                                ) {
                                    append(stringResource(R.string._24h))
                                }
                            }
                        )
                    }


                }
                Text(
                    state.coin.rank.toString(),
                    modifier = Modifier.padding(end = 8.dp),
                    fontSize = MaterialTheme.typography.titleLarge.fontSize
                )

            }
        }

        //Chart
        item {
            OhlcChartContainer(
                state = state.ohlcChartState,
                daysRange = state.ohlcDayRange,
                onRetry = {onAction(CoinDetailAction.RetryLoadChart)},
                onTimeRangeChange = {onAction(CoinDetailAction.OhlcTimeRangeChange(it))}
            )

            Spacer(Modifier.height(32.dp))
        }


        //MarketCap , totalVolume
        item {
            DetailCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(
                            stringResource(R.string.marketcap),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            ("$" + state.coin.marketCap?.formatToShortClean()),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Column(Modifier.weight(1f)) {
                        Text(
                            stringResource(R.string.total_volume),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            ("$" + state.coin.totalVolume?.formatToShortClean()),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                }

            }
        }

        //maxSupply , circulatingSupply
        item {
            DetailCard {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(
                            stringResource(R.string.max_supply),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        Spacer(Modifier.height(4.dp))
                        val maxSupply =
                            if (state.coin.maxSupplyInfinite) stringResource(R.string.infinite) else "$" + state.coin.maxSupply?.formatToShortClean()
                        Text(
                            maxSupply,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Column(Modifier.weight(1f)) {
                        Text(
                            stringResource(R.string.cicurlating_supply),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            ("$" + state.coin.circulatingSupply?.formatToShortClean()),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                }

            }
        }

        //ath,atl
        item {
            DetailCard {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(
                            stringResource(R.string.all_time_high),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            ("$" + state.coin.allTimeHigh?.formatToShortClean()),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Column(Modifier.weight(1f)) {
                        Text(
                            stringResource(R.string.all_time_low),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            ("$" + state.coin.allTimeLow?.formatToShortClean()),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                }

            }
        }

        item {
            DetailCard {
                Column {


                    Text(
                        stringResource(R.string.description),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        state.coin.description,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Justify
                    )

                }
            }
        }


    }
}


@Composable
private fun DetailCard(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Box(
        modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceContainer,
                shape = MaterialTheme.shapes.medium
            )
            .padding(8.dp)
    ) {

        content()

    }
    Spacer(Modifier.height(4.dp))
}