package af.amir.cryptotracker.crypto.presentation.coin_detail.component

import com.patrykandpatrick.vico.core.cartesian.CartesianMeasuringContext
import com.patrykandpatrick.vico.core.cartesian.axis.Axis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import java.text.SimpleDateFormat
import java.util.Locale

class TimeAxisFormatter(val timeUnit: TimeUnit) :
    CartesianValueFormatter {

    private val hourFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    private val dayFormat = SimpleDateFormat("dd MMM", Locale.getDefault())

    override fun format(
        context: CartesianMeasuringContext,
        value: Double,
        verticalAxisPosition: Axis.Position.Vertical?,
    ): CharSequence {
        val timestamp = (value * timeUnit.mills).toLong()
        return when(timeUnit){
            TimeUnit.HOUR ->hourFormat.format(timestamp)
            TimeUnit.DAY -> dayFormat.format(timestamp)
        }
    }
}