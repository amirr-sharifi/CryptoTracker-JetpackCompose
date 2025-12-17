package af.amir.cryptotracker.crypto.data.remote.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.jsonPrimitive
import java.math.BigDecimal

object BigDecimalSerializer : KSerializer<BigDecimal> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("BigDecimal", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): BigDecimal {
        require(decoder is JsonDecoder){"This serializer requires JsonDecoder"}
        val jsonElement = decoder.decodeJsonElement()
        val stringRepresentation = jsonElement.jsonPrimitive.content
        return BigDecimal(stringRepresentation)
    }

    override fun serialize(encoder: Encoder, value: BigDecimal) {
        return encoder.encodeString(value.toPlainString())
    }
}