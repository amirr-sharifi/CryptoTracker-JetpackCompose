package af.amir.cryptotracker.core.domain.util

enum class NetworkError : Error {
    NO_INTERNET, SERIALIZATION, TIMED_OUT, Unknown, BAD_REQUEST, FORBIDDEN, TOO_MANY_REQUEST, SERVER_ERROR, OFFLINE
}