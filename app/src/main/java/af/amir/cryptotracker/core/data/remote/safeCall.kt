package af.amir.cryptotracker.core.data.remote

import af.amir.cryptotracker.core.domain.util.NetworkError
import af.amir.cryptotracker.core.domain.util.Result
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.JsonConvertException
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import kotlin.coroutines.coroutineContext

suspend inline fun <reified T> safeCall(execute: () -> HttpResponse): Result<T, NetworkError> {
    val response = try {
        execute()
    } catch (e : UnresolvedAddressException){
        return Result.Error(NetworkError.NO_INTERNET)
    } catch (e : SerializationException){
        return Result.Error(NetworkError.SERIALIZATION)
    } catch (e: JsonConvertException){
        return Result.Error(NetworkError.SERIALIZATION)
    }catch (e : Exception){
        coroutineContext.ensureActive()
        return Result.Error(NetworkError.Unknown)
    }

    return responseToResult(response)
}