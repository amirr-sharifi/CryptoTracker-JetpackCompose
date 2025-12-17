package af.amir.cryptotracker.core.presentation

import af.amir.cryptotracker.R
import af.amir.cryptotracker.core.domain.util.NetworkError
import android.content.Context
import androidx.core.content.ContextCompat

fun NetworkError.toString(context : Context) : String{
    val resId = when(this){
        NetworkError.NO_INTERNET -> R.string.error_no_internet
        NetworkError.SERIALIZATION -> R.string.error_serialization
        NetworkError.TIMED_OUT -> R.string.error_request_timeout
        NetworkError.Unknown -> R.string.error_unknown
        NetworkError.BAD_REQUEST -> R.string.error_bad_request
        NetworkError.FORBIDDEN -> R.string.error_forbidden
        NetworkError.TOO_MANY_REQUEST -> R.string.error_too_many_requests
        NetworkError.SERVER_ERROR -> R.string.error_unknown
        NetworkError.OFFLINE -> R.string.error_offline
    }

    return ContextCompat.getString(context,resId)
}