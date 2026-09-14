package com.example.model

import java.text.NumberFormat
import java.util.Locale

enum class CustomerType(val displayName: String, val defaultRate: Double) {
    NEW_CUSTOMER("New Customer", 0.75),
    MEDIUM("Medium", 0.80),
    GOOD("Good", 0.85),
    GOOD_LOYAL("Good Loyal", 0.90)
}

enum class StnkStatus(val displayName: String, val rateAdjustment: Double) {
    ATAS_NAMA_SENDIRI("Atas Nama Sendiri", 0.0),
    ORANG_LAIN("Orang Lain", -0.05)
}

data class CustomerRateConfig(
    val newCustomerPercent: Int = 75,
    val mediumPercent: Int = 80,
    val goodPercent: Int = 85,
    val goodLoyalPercent: Int = 90
) {
    fun getRateFor(customerType: CustomerType): Double {
        return when (customerType) {
            CustomerType.NEW_CUSTOMER -> newCustomerPercent / 100.0
            CustomerType.MEDIUM -> mediumPercent / 100.0
            CustomerType.GOOD -> goodPercent / 100.0
            CustomerType.GOOD_LOYAL -> goodLoyalPercent / 100.0
        }
    }

    fun getPercentFor(customerType: CustomerType): Int {
        return when (customerType) {
            CustomerType.NEW_CUSTOMER -> newCustomerPercent
            CustomerType.MEDIUM -> mediumPercent
            CustomerType.GOOD -> goodPercent
            CustomerType.GOOD_LOYAL -> goodLoyalPercent
        }
    }
}

data class MarketPriceItem(
    val id: String,
    val modelName: String,
    val year: Int,
    val otrPrice: Long
)

data class ClopCustomerData(
    val contractNumber: String,
    val engineNumber: String,
    val customerName: String,
    val motorcycleModel: String,
    val year: Int,
    val otrEstimate: Long,
    val customerType: CustomerType,
    val stnkStatus: StnkStatus,
    val remainingInstallment: Long
)

object CurrencyHelper {
    private val localeID = Locale("in", "ID")

    fun formatRupiah(amount: Long): String {
        val formatter = NumberFormat.getCurrencyInstance(localeID).apply {
            maximumFractionDigits = 0
            minimumFractionDigits = 0
        }
        return formatter.format(amount).replace("Rp", "Rp ")
    }

    fun parseNumber(rawString: String): Long {
        val cleanString = rawString.replace(Regex("[^0-9]"), "")
        return cleanString.toLongOrNull() ?: 0L
    }

    fun formatNumber(number: Long): String {
        val formatter = NumberFormat.getNumberInstance(localeID)
        return formatter.format(number)
    }
}

