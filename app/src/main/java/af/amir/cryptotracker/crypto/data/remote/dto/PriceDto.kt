package af.amir.cryptotracker.crypto.data.remote.dto

import af.amir.cryptotracker.crypto.data.remote.serializer.BigDecimalSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import java.math.BigDecimal

@Serializable
data class PriceDto(
    @Serializable(with = BigDecimalSerializer::class)
    val usd : BigDecimal? = null
)
