package af.amir.cryptotracker.core.presentation

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Locale

fun Double.formatPrice(): String {
    val formatter = NumberFormat.getNumberInstance(Locale.getDefault()).apply {
        minimumFractionDigits = 2
        maximumFractionDigits= 2
    }
    return formatter.format(this)
}

fun BigDecimal.formatPrice(): String {
    val formatter = NumberFormat.getNumberInstance(Locale.getDefault()).apply {
        minimumFractionDigits = 2
        maximumFractionDigits= 2
    }
    return formatter.format(this)
}

fun BigDecimal.formatToShortClean(): String {
    val absValue = this.abs()

    val trillion = BigDecimal("1000000000000")
    val billion = BigDecimal("1000000000")
    val million = BigDecimal("1000000")
    val thousand = BigDecimal("1000")

    val (number, suffix) = when {
        absValue >= trillion -> this.divide(trillion) to "T"
        absValue >= billion -> this.divide(billion) to "B"
        absValue >= million -> this.divide(million) to "M"
        absValue >= thousand -> this.divide(thousand) to "K"
        else -> return this.setScale(2, RoundingMode.HALF_UP).toPlainString()
    }

    val formatted = number
        .setScale(2, RoundingMode.HALF_UP)
        .toPlainString()
        .replace(".00", "")
        .replace(".0", "")

    return formatted + suffix
}

