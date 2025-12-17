package af.amir.cryptotracker.core.data.remote

const val BASE_URL = "https://api.coingecko.com/api/v3/"

fun constructUrl(url: String): String {
    return when {
        url == BASE_URL -> BASE_URL
        url.contains(BASE_URL) -> url
        url.startsWith("/") -> BASE_URL + url.drop(1)
        else -> BASE_URL + url
    }
}