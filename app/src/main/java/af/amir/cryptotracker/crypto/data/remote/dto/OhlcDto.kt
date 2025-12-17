package af.amir.cryptotracker.crypto.data.remote.dto

import af.amir.cryptotracker.crypto.data.remote.serializer.OhlcDtoSerializer
import kotlinx.serialization.Serializable

@Serializable(with = OhlcDtoSerializer::class)
data class OhlcDto(
    val timestamp : Long,
    val open : Double,
    val high : Double,
    val low : Double,
    val close : Double,
)
