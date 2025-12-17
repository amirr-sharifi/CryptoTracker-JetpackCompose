package af.amir.cryptotracker.di

import af.amir.cryptotracker.core.data.helper.CheckInternetConnection
import af.amir.cryptotracker.core.data.remote.HttpClientFactory
import af.amir.cryptotracker.crypto.data.remote.RemoteCoinDataSource
import af.amir.cryptotracker.crypto.domain.CoinDataSource
import af.amir.cryptotracker.crypto.presentation.coin_detail.CoinDetailViewModel
import af.amir.cryptotracker.crypto.presentation.coin_list.CoinListViewModel
import io.ktor.client.engine.cio.CIO
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single { HttpClientFactory.create(CIO.create()) }
    singleOf(::RemoteCoinDataSource).bind<CoinDataSource>()
    single { CheckInternetConnection(androidContext()) }
    viewModelOf(::CoinListViewModel)
    viewModel { (coinId: String) ->
        CoinDetailViewModel(coinId = coinId, coinDataSource = get())
    }
}