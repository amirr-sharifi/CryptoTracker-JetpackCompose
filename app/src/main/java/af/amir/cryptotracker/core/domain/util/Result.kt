package af.amir.cryptotracker.core.domain.util

typealias DomainError = Error

sealed interface Result<out D, out E : DomainError> {
    data class Success<out D>(val data: D) : Result<D, Nothing>
    data class Error<out E : DomainError>(val error: E) : Result<Nothing, E>
}

inline fun <D, E : Error, R> Result<D, E>.map(map: (data: D) -> R): Result<R, E> {
    return when (this) {
        is Result.Error -> Result.Error(error)
        is Result.Success -> Result.Success(map(data))
    }
}

inline fun <D, E : Error> Result<D, E>.onSuccess(action: (D) -> Unit): Result<D, E> {
    return when (this) {
        is Result.Error -> Result.Error(error)
        is Result.Success -> {
            action(data)
            Result.Success(data)
        }
    }
}

inline fun <D, E : Error> Result<D, E>.onError(action: (E) -> Unit): Result<D, E> {
    return when (this) {
        is Result.Error -> {
            action(error)
            Result.Error(error)
        }
        is Result.Success -> Result.Success(data)

    }
}