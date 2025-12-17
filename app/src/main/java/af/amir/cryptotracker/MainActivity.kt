package af.amir.cryptotracker

import af.amir.cryptotracker.crypto.domain.CoinDataSource
import af.amir.cryptotracker.crypto.presentation.coin_list.CoinListScreen
import af.amir.cryptotracker.crypto.presentation.navigation.CoinNavGraph
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import af.amir.cryptotracker.ui.theme.CryptoTrackerTheme
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CryptoTrackerTheme {
                val navController = rememberNavController()
                 CoinNavGraph(modifier = Modifier.fillMaxSize(), navHostController = navController)
            }
        }
    }
}
