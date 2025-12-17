package af.amir.cryptotracker.crypto.data.remote.serializer

import af.amir.cryptotracker.crypto.data.remote.dto.OhlcDto
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object OhlcDtoSerializer : KSerializer<OhlcDto> {
    @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
    override val descriptor: SerialDescriptor
        get() = buildSerialDescriptor("OhlcDto", StructureKind.LIST)

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): OhlcDto {
        val composite = decoder.beginStructure(descriptor)
        var timestamp = 0L
        var open = 0.0
        var high = 0.0
        var low = 0.0
        var close = 0.0

        var index = 0
        if (composite.decodeSequentially()){
            timestamp = composite.decodeLongElement(descriptor,0)
            open = composite.decodeDoubleElement(descriptor,1)
            high = composite.decodeDoubleElement(descriptor,2)
            low = composite.decodeDoubleElement(descriptor,3)
            close = composite.decodeDoubleElement(descriptor,4)
        }else{
            while (true) {
                when (val i = composite.decodeElementIndex(descriptor)) {
                    CompositeDecoder.DECODE_DONE -> break
                    0 -> timestamp = composite.decodeLongElement(descriptor, i)
                    1 -> open = composite.decodeDoubleElement(descriptor, i)
                    2 -> high = composite.decodeDoubleElement(descriptor, i)
                    3 -> low = composite.decodeDoubleElement(descriptor, i)
                    4 -> close = composite.decodeDoubleElement(descriptor, i)
                    else -> error("Unexpected Error!! $i")
                }
                index++
            }
        }
        composite.endStructure(descriptor)
        return OhlcDto(timestamp, open, high, low, close)
    }

    override fun serialize(encoder: Encoder, value: OhlcDto) {
        error("!!")
    }
}