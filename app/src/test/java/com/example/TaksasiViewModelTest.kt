package com.example

import com.example.model.CustomerRateConfig
import com.example.model.CustomerType
import com.example.model.StnkStatus
import com.example.ui.TaksasiViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TaksasiViewModelTest {

    private lateinit var viewModel: TaksasiViewModel

    @Before
    fun setUp() {
        viewModel = TaksasiViewModel()
    }

    @Test
    fun testCustomerTypesDefaultRates() {
        viewModel.onOtrMotorChanged("10000000") // 10 Juta
        viewModel.onSisaAngsuranChanged("0")
        viewModel.onMateraiCountChanged(0)

        // New Customer -> 75%
        viewModel.onCustomerTypeSelected(CustomerType.NEW_CUSTOMER)
        assertEquals(75, viewModel.uiState.value.plafondPercentage)
        assertEquals(7_500_000L, viewModel.uiState.value.maksimalPinjaman)
        assertEquals(7_500_000L, viewModel.uiState.value.totalDanaCairBersih)

        // Medium -> 80%
        viewModel.onCustomerTypeSelected(CustomerType.MEDIUM)
        assertEquals(80, viewModel.uiState.value.plafondPercentage)
        assertEquals(8_000_000L, viewModel.uiState.value.maksimalPinjaman)

        // Good -> 85%
        viewModel.onCustomerTypeSelected(CustomerType.GOOD)
        assertEquals(85, viewModel.uiState.value.plafondPercentage)
        assertEquals(8_500_000L, viewModel.uiState.value.maksimalPinjaman)

        // Good Loyal -> 90%
        viewModel.onCustomerTypeSelected(CustomerType.GOOD_LOYAL)
        assertEquals(90, viewModel.uiState.value.plafondPercentage)
        assertEquals(9_000_000L, viewModel.uiState.value.maksimalPinjaman)
    }

    @Test
    fun testMateraiCalculation() {
        viewModel.onOtrMotorChanged("10000000") // 10 Juta
        viewModel.onCustomerTypeSelected(CustomerType.NEW_CUSTOMER) // 75% -> 7.5 Juta
        viewModel.onSisaAngsuranChanged("0")

        // 1 Materai = Rp 10.000
        viewModel.onMateraiCountChanged(1)
        assertEquals(1, viewModel.uiState.value.materaiCount)
        assertEquals(10_000L, viewModel.uiState.value.biayaMaterai)
        assertEquals(7_490_000L, viewModel.uiState.value.totalDanaCairBersih)

        // 2 Materai = Rp 20.000
        viewModel.incrementMaterai()
        assertEquals(2, viewModel.uiState.value.materaiCount)
        assertEquals(20_000L, viewModel.uiState.value.biayaMaterai)
        assertEquals(7_480_000L, viewModel.uiState.value.totalDanaCairBersih)

        // Stepper decrement
        viewModel.decrementMaterai()
        assertEquals(1, viewModel.uiState.value.materaiCount)
        assertEquals(10_000L, viewModel.uiState.value.biayaMaterai)
        assertEquals(7_490_000L, viewModel.uiState.value.totalDanaCairBersih)
    }

    @Test
    fun testTotalDanaCairWithInstallmentAndMaterai() {
        viewModel.onOtrMotorChanged("20000000") // 20 Juta
        viewModel.onCustomerTypeSelected(CustomerType.GOOD) // 85% -> 17 Juta
        viewModel.onSisaAngsuranChanged("2000000") // 2 Juta
        viewModel.onMateraiCountChanged(2) // 20.000

        // Total Dana Cair = 17.000.000 - 2.000.000 - 20.000 = 14.980.000
        assertEquals(17_000_000L, viewModel.uiState.value.maksimalPinjaman)
        assertEquals(2_000_000L, viewModel.uiState.value.pelunasanKontrakLama)
        assertEquals(20_000L, viewModel.uiState.value.biayaMaterai)
        assertEquals(14_980_000L, viewModel.uiState.value.totalDanaCairBersih)
    }

    @Test
    fun testManualCustomerRateConfigUpdate() {
        viewModel.onOtrMotorChanged("10000000")
        viewModel.onCustomerTypeSelected(CustomerType.GOOD_LOYAL)

        // Custom config: Good Loyal = 95%, New Customer = 70%
        val customConfig = CustomerRateConfig(
            newCustomerPercent = 70,
            mediumPercent = 78,
            goodPercent = 88,
            goodLoyalPercent = 95
        )
        viewModel.updateCustomerRateConfig(customConfig)

        assertEquals(95, viewModel.uiState.value.plafondPercentage)
        assertEquals(9_500_000L, viewModel.uiState.value.maksimalPinjaman)

        viewModel.onCustomerTypeSelected(CustomerType.NEW_CUSTOMER)
        assertEquals(70, viewModel.uiState.value.plafondPercentage)
        assertEquals(7_000_000L, viewModel.uiState.value.maksimalPinjaman)
    }

    @Test
    fun testMarketPriceManagement() {
        val initialCount = viewModel.uiState.value.marketPrices.size
        viewModel.addMarketPrice("Kawasaki Ninja ZX-25R", 2024, 115_000_000L)
        assertEquals(initialCount + 1, viewModel.uiState.value.marketPrices.size)

        val newItem = viewModel.uiState.value.marketPrices.first()
        assertEquals("Kawasaki Ninja ZX-25R", newItem.modelName)
        assertEquals(115_000_000L, newItem.otrPrice)

        viewModel.applyMarketPriceToCalculator(newItem)
        assertEquals(115_000_000L, viewModel.uiState.value.otrMotorValue)
    }

    @Test
    fun testBahanClopSearch() {
        // Search by contract number
        viewModel.onSearchQueryChanged("CTR-2024-001")
        viewModel.searchBahanClop()

        assertTrue(viewModel.uiState.value.searchSuccess)
        assertEquals("Budi Santoso", viewModel.uiState.value.matchedCustomer?.customerName)
        assertEquals(CustomerType.GOOD_LOYAL, viewModel.uiState.value.customerType)
        assertEquals(StnkStatus.ATAS_NAMA_SENDIRI, viewModel.uiState.value.stnkStatus)
    }
}
