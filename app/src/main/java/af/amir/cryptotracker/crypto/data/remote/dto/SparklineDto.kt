package af.amir.cryptotracker.crypto.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class SparklineDto(
    val price: List<Double>
)