package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.ClopCustomerData
import com.example.model.CurrencyHelper
import com.example.model.CustomerRateConfig
import com.example.model.CustomerType
import com.example.model.MarketPriceItem
import com.example.model.StnkStatus

@OptIn(ExperimentalLayoutApi::class, androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun PengaturanDialog(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    marketPrices: List<MarketPriceItem>,
    onAddMarketPrice: (String, Int, Long) -> Unit,
    onUpdateMarketPrice: (String, String, Int, Long) -> Unit,
    onDeleteMarketPrice: (String) -> Unit,
    onApplyMarketPrice: (MarketPriceItem) -> Unit,
    onResetMarketPrices: () -> Unit,
    clopDatabase: List<ClopCustomerData>,
    onAddClopCustomer: (ClopCustomerData) -> Unit,
    onUpdateClopCustomer: (ClopCustomerData) -> Unit,
    onDeleteClopCustomer: (String) -> Unit,
    onSelectClopCustomer: (ClopCustomerData) -> Unit,
    onResetClopDatabase: () -> Unit,
    customerRateConfig: CustomerRateConfig,
    onSaveRateConfig: (CustomerRateConfig) -> Unit,
    onResetRateConfig: () -> Unit
) {
    if (!isOpen) return

    var showMarketPriceFormDialog by remember { mutableStateOf(false) }
    var editingMarketPriceItem by remember { mutableStateOf<MarketPriceItem?>(null) }

    var showClopFormDialog by remember { mutableStateOf(false) }
    var editingClopItem by remember { mutableStateOf<ClopCustomerData?>(null) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .testTag("dialog_pengaturan"),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Header Dialog
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Menu Pengaturan Sistem",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Harga Pasar, Data Bahan Clop, & % Plafond",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("btn_close_pengaturan")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Tutup Pengaturan"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tab Navigation
                PrimaryTabRow(
                    selectedTabIndex = selectedTab,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { onTabSelected(0) },
                        text = { Text("Harga Pasar", fontWeight = FontWeight.SemiBold) },
                        icon = { Icon(Icons.Default.TwoWheeler, contentDescription = null, modifier = Modifier.size(18.dp)) },
                        modifier = Modifier.testTag("tab_harga_pasar")
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { onTabSelected(1) },
                        text = { Text("Bahan Clop", fontWeight = FontWeight.SemiBold) },
                        icon = { Icon(Icons.Default.Storage, contentDescription = null, modifier = Modifier.size(18.dp)) },
                        modifier = Modifier.testTag("tab_bahan_clop")
                    )
                    Tab(
                        selected = selectedTab == 2,
                        onClick = { onTabSelected(2) },
                        text = { Text("% Customer", fontWeight = FontWeight.SemiBold) },
                        icon = { Icon(Icons.Default.Percent, contentDescription = null, modifier = Modifier.size(18.dp)) },
                        modifier = Modifier.testTag("tab_persen_customer")
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tab Content
                Box(modifier = Modifier.weight(1f)) {
                    when (selectedTab) {
                        0 -> {
                            TabHargaPasarContent(
                                marketPrices = marketPrices,
                                onAddNewClick = {
                                    editingMarketPriceItem = null
                                    showMarketPriceFormDialog = true
                                },
                                onEditClick = { item ->
                                    editingMarketPriceItem = item
                                    showMarketPriceFormDialog = true
                                },
                                onDeleteClick = onDeleteMarketPrice,
                                onApplyClick = { item ->
                                    onApplyMarketPrice(item)
                                    onDismiss()
                                },
                                onResetDefault = onResetMarketPrices
                            )
                        }
                        1 -> {
                            TabBahanClopContent(
                                clopDatabase = clopDatabase,
                                onAddNewClick = {
                                    editingClopItem = null
                                    showClopFormDialog = true
                                },
                                onEditClick = { item ->
                                    editingClopItem = item
                                    showClopFormDialog = true
                                },
                                onDeleteClick = onDeleteClopCustomer,
                                onSelectClick = { item ->
                                    onSelectClopCustomer(item)
                                    onDismiss()
                                },
                                onResetDefault = onResetClopDatabase
                            )
                        }
                        2 -> {
                            TabPersenCustomerContent(
                                currentConfig = customerRateConfig,
                                onSave = { newConfig ->
                                    onSaveRateConfig(newConfig)
                                },
                                onReset = onResetRateConfig
                            )
                        }
                    }
                }
            }
        }
    }

    // Sub-Dialog Form Tambah/Edit Harga Pasar
    if (showMarketPriceFormDialog) {
        MarketPriceFormDialog(
            initialItem = editingMarketPriceItem,
            onDismiss = { showMarketPriceFormDialog = false },
            onSave = { modelName, year, otrPrice ->
                if (editingMarketPriceItem != null) {
                    onUpdateMarketPrice(editingMarketPriceItem!!.id, modelName, year, otrPrice)
                } else {
                    onAddMarketPrice(modelName, year, otrPrice)
                }
                showMarketPriceFormDialog = false
            }
        )
    }

    // Sub-Dialog Form Tambah/Edit Bahan Clop
    if (showClopFormDialog) {
        ClopCustomerFormDialog(
            initialItem = editingClopItem,
            onDismiss = { showClopFormDialog = false },
            onSave = { customer ->
                if (editingClopItem != null) {
                    onUpdateClopCustomer(customer)
                } else {
                    onAddClopCustomer(customer)
                }
                showClopFormDialog = false
            }
        )
    }
}

// ---------------- TAB 1: HARGA PASAR ----------------
@Composable
private fun TabHargaPasarContent(
    marketPrices: List<MarketPriceItem>,
    onAddNewClick: () -> Unit,
    onEditClick: (MarketPriceItem) -> Unit,
    onDeleteClick: (String) -> Unit,
    onApplyClick: (MarketPriceItem) -> Unit,
    onResetDefault: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Daftar Harga Pasar Motor (OTR)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Total ${marketPrices.size} motor terdaftar",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = onResetDefault,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Reset", style = MaterialTheme.typography.labelSmall)
                }

                Button(
                    onClick = onAddNewClick,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.testTag("btn_tambah_harga_pasar")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Tambah", style = MaterialTheme.typography.labelSmall)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(marketPrices, key = { it.id }) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = item.modelName,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = MaterialTheme.colorScheme.secondaryContainer
                                ) {
                                    Text(
                                        text = "${item.year}",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "OTR: ${CurrencyHelper.formatRupiah(item.otrPrice)}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Button(
                                onClick = { onApplyClick(item) },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                modifier = Modifier.height(34.dp)
                            ) {
                                Text("Gunakan", style = MaterialTheme.typography.labelSmall)
                            }

                            IconButton(
                                onClick = { onEditClick(item) },
                                modifier = Modifier.size(34.dp)
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit", modifier = Modifier.size(16.dp))
                            }

                            IconButton(
                                onClick = { onDeleteClick(item.id) },
                                modifier = Modifier.size(34.dp)
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Hapus",
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ---------------- TAB 2: DATA BAHAN CLOP ----------------
@Composable
private fun TabBahanClopContent(
    clopDatabase: List<ClopCustomerData>,
    onAddNewClick: () -> Unit,
    onEditClick: (ClopCustomerData) -> Unit,
    onDeleteClick: (String) -> Unit,
    onSelectClick: (ClopCustomerData) -> Unit,
    onResetDefault: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Database Bahan Clop Terdaftar",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${clopDatabase.size} data nasabah untuk pencarian",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = onResetDefault,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Reset", style = MaterialTheme.typography.labelSmall)
                }

                Button(
                    onClick = onAddNewClick,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.testTag("btn_tambah_bahan_clop")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Tambah", style = MaterialTheme.typography.labelSmall)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(clopDatabase, key = { it.contractNumber }) { customer ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.primaryContainer
                            ) {
                                Text(
                                    text = customer.contractNumber,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = MaterialTheme.colorScheme.tertiaryContainer
                                ) {
                                    Text(
                                        text = customer.customerType.displayName,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant
                                ) {
                                    Text(
                                        text = customer.stnkStatus.displayName,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = customer.customerName,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${customer.motorcycleModel} (${customer.year}) • Nosin: ${customer.engineNumber}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Estimasi OTR: ${CurrencyHelper.formatRupiah(customer.otrEstimate)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Sisa Angsuran: ${CurrencyHelper.formatRupiah(customer.remainingInstallment)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (customer.remainingInstallment > 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Button(
                                    onClick = { onSelectClick(customer) },
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                    modifier = Modifier.height(34.dp)
                                ) {
                                    Text("Pilih", style = MaterialTheme.typography.labelSmall)
                                }

                                IconButton(
                                    onClick = { onEditClick(customer) },
                                    modifier = Modifier.size(34.dp)
                                ) {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit", modifier = Modifier.size(16.dp))
                                }

                                IconButton(
                                    onClick = { onDeleteClick(customer.contractNumber) },
                                    modifier = Modifier.size(34.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Hapus",
                                        tint = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ---------------- TAB 3: PENGATURAN % MANUAL CUSTOMER ----------------
@Composable
private fun TabPersenCustomerContent(
    currentConfig: CustomerRateConfig,
    onSave: (CustomerRateConfig) -> Unit,
    onReset: () -> Unit
) {
    var newCustPercent by remember(currentConfig) { mutableFloatStateOf(currentConfig.newCustomerPercent.toFloat()) }
    var mediumPercent by remember(currentConfig) { mutableFloatStateOf(currentConfig.mediumPercent.toFloat()) }
    var goodPercent by remember(currentConfig) { mutableFloatStateOf(currentConfig.goodPercent.toFloat()) }
    var loyalPercent by remember(currentConfig) { mutableFloatStateOf(currentConfig.goodLoyalPercent.toFloat()) }
    var hasSavedFeedback by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column {
                    Text(
                        text = "Pengaturan % Manual Plafond Customer",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Sesuaikan persentase plafond dasar untuk masing-masing profil customer secara fleksibel (rentang 50% - 100%).",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Slider 1: New Customer
            item {
                RateSliderCard(
                    title = "New Customer",
                    description = "Nasabah baru yang belum memiliki riwayat kontrak",
                    value = newCustPercent,
                    onValueChange = { newCustPercent = it; hasSavedFeedback = false },
                    presets = listOf(70, 75, 80)
                )
            }

            // Slider 2: Medium
            item {
                RateSliderCard(
                    title = "Medium",
                    description = "Nasabah dengan riwayat kredit standar/cukup baik",
                    value = mediumPercent,
                    onValueChange = { mediumPercent = it; hasSavedFeedback = false },
                    presets = listOf(75, 80, 85)
                )
            }

            // Slider 3: Good
            item {
                RateSliderCard(
                    title = "Good",
                    description = "Nasabah repeat order dengan riwayat pembayaran lancar",
                    value = goodPercent,
                    onValueChange = { goodPercent = it; hasSavedFeedback = false },
                    presets = listOf(80, 85, 90)
                )
            }

            // Slider 4: Good Loyal
            item {
                RateSliderCard(
                    title = "Good Loyal",
                    description = "Nasabah prioritas loyal dengan riwayat sempurna",
                    value = loyalPercent,
                    onValueChange = { loyalPercent = it; hasSavedFeedback = false },
                    presets = listOf(85, 90, 95)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = {
                    onReset()
                    newCustPercent = 75f
                    mediumPercent = 80f
                    goodPercent = 85f
                    loyalPercent = 90f
                    hasSavedFeedback = false
                },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Default", style = MaterialTheme.typography.labelMedium)
            }

            Button(
                onClick = {
                    val updated = CustomerRateConfig(
                        newCustomerPercent = newCustPercent.toInt(),
                        mediumPercent = mediumPercent.toInt(),
                        goodPercent = goodPercent.toInt(),
                        goodLoyalPercent = loyalPercent.toInt()
                    )
                    onSave(updated)
                    hasSavedFeedback = true
                },
                modifier = Modifier
                    .weight(2f)
                    .height(48.dp)
                    .testTag("btn_simpan_persen_customer"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    imageVector = if (hasSavedFeedback) Icons.Default.Check else Icons.Default.Save,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (hasSavedFeedback) "Tersimpan & Diterapkan!" else "Simpan & Terapkan",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun RateSliderCard(
    title: String,
    description: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    presets: List<Int>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = "${value.toInt()}%",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Slider(
                value = value,
                onValueChange = onValueChange,
                valueRange = 50f..100f,
                steps = 49,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Preset:",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                presets.forEach { preset ->
                    FilterChip(
                        selected = value.toInt() == preset,
                        onClick = { onValueChange(preset.toFloat()) },
                        label = { Text("$preset%", style = MaterialTheme.typography.labelSmall) },
                        modifier = Modifier.height(28.dp)
                    )
                }
            }
        }
    }
}

// ---------------- DIALOG FORMS ----------------
@Composable
private fun MarketPriceFormDialog(
    initialItem: MarketPriceItem?,
    onDismiss: () -> Unit,
    onSave: (String, Int, Long) -> Unit
) {
    var modelName by remember { mutableStateOf(initialItem?.modelName ?: "") }
    var yearInput by remember { mutableStateOf(initialItem?.year?.toString() ?: "2023") }
    var otrInput by remember { mutableStateOf(if (initialItem != null) CurrencyHelper.formatNumber(initialItem.otrPrice) else "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (initialItem == null) "Tambah Harga Pasar Motor" else "Edit Harga Pasar Motor")
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = modelName,
                    onValueChange = { modelName = it },
                    label = { Text("Model Motor") },
                    placeholder = { Text("Contoh: Honda PCX 160 CBS") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = yearInput,
                    onValueChange = { yearInput = it.filter { char -> char.isDigit() }.take(4) },
                    label = { Text("Tahun") },
                    placeholder = { Text("2023") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = otrInput,
                    onValueChange = { input ->
                        val parsed = CurrencyHelper.parseNumber(input)
                        otrInput = if (parsed == 0L && input.isEmpty()) "" else CurrencyHelper.formatNumber(parsed)
                    },
                    label = { Text("Harga OTR Pasar (Rp)") },
                    placeholder = { Text("32.500.000") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val year = yearInput.toIntOrNull() ?: 2023
                    val price = CurrencyHelper.parseNumber(otrInput)
                    if (modelName.isNotBlank() && price > 0L) {
                        onSave(modelName, year, price)
                    }
                },
                enabled = modelName.isNotBlank() && CurrencyHelper.parseNumber(otrInput) > 0L
            ) {
                Text("Simpan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ClopCustomerFormDialog(
    initialItem: ClopCustomerData?,
    onDismiss: () -> Unit,
    onSave: (ClopCustomerData) -> Unit
) {
    var contractNumber by remember { mutableStateOf(initialItem?.contractNumber ?: "CTR-${System.currentTimeMillis() % 10000}") }
    var engineNumber by remember { mutableStateOf(initialItem?.engineNumber ?: "MH1JM${System.currentTimeMillis() % 10000}ID") }
    var customerName by remember { mutableStateOf(initialItem?.customerName ?: "") }
    var motorcycleModel by remember { mutableStateOf(initialItem?.motorcycleModel ?: "") }
    var yearInput by remember { mutableStateOf(initialItem?.year?.toString() ?: "2023") }
    var otrInput by remember { mutableStateOf(if (initialItem != null) CurrencyHelper.formatNumber(initialItem.otrEstimate) else "25.000.000") }
    var sisaInput by remember { mutableStateOf(if (initialItem != null) CurrencyHelper.formatNumber(initialItem.remainingInstallment) else "2.000.000") }
    var selectedType by remember { mutableStateOf(initialItem?.customerType ?: CustomerType.GOOD) }
    var selectedStnk by remember { mutableStateOf(initialItem?.stnkStatus ?: StnkStatus.ATAS_NAMA_SENDIRI) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (initialItem == null) "Tambah Data Bahan Clop" else "Edit Data Bahan Clop")
        },
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = contractNumber,
                        onValueChange = { contractNumber = it },
                        label = { Text("No. Kontrak") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
                item {
                    OutlinedTextField(
                        value = engineNumber,
                        onValueChange = { engineNumber = it },
                        label = { Text("No. Mesin") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
                item {
                    OutlinedTextField(
                        value = customerName,
                        onValueChange = { customerName = it },
                        label = { Text("Nama Nasabah") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
                item {
                    OutlinedTextField(
                        value = motorcycleModel,
                        onValueChange = { motorcycleModel = it },
                        label = { Text("Model Sepeda Motor") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
                item {
                    OutlinedTextField(
                        value = yearInput,
                        onValueChange = { yearInput = it.filter { char -> char.isDigit() }.take(4) },
                        label = { Text("Tahun") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
                item {
                    OutlinedTextField(
                        value = otrInput,
                        onValueChange = { input ->
                            val parsed = CurrencyHelper.parseNumber(input)
                            otrInput = if (parsed == 0L && input.isEmpty()) "" else CurrencyHelper.formatNumber(parsed)
                        },
                        label = { Text("Estimasi OTR (Rp)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
                item {
                    OutlinedTextField(
                        value = sisaInput,
                        onValueChange = { input ->
                            val parsed = CurrencyHelper.parseNumber(input)
                            sisaInput = if (parsed == 0L && input.isEmpty()) "" else CurrencyHelper.formatNumber(parsed)
                        },
                        label = { Text("Sisa Angsuran (Rp)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
                item {
                    Text("Tipe Customer:", style = MaterialTheme.typography.labelMedium)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        CustomerType.values().forEach { type ->
                            FilterChip(
                                selected = selectedType == type,
                                onClick = { selectedType = type },
                                label = { Text(type.displayName, style = MaterialTheme.typography.labelSmall) }
                            )
                        }
                    }
                }
                item {
                    Text("Status STNK:", style = MaterialTheme.typography.labelMedium)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        StnkStatus.values().forEach { stnk ->
                            FilterChip(
                                selected = selectedStnk == stnk,
                                onClick = { selectedStnk = stnk },
                                label = { Text(stnk.displayName, style = MaterialTheme.typography.labelSmall) }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val year = yearInput.toIntOrNull() ?: 2023
                    val otr = CurrencyHelper.parseNumber(otrInput)
                    val sisa = CurrencyHelper.parseNumber(sisaInput)
                    if (contractNumber.isNotBlank() && customerName.isNotBlank() && motorcycleModel.isNotBlank()) {
                        onSave(
                            ClopCustomerData(
                                contractNumber = contractNumber.trim(),
                                engineNumber = engineNumber.trim(),
                                customerName = customerName.trim(),
                                motorcycleModel = motorcycleModel.trim(),
                                year = year,
                                otrEstimate = otr,
                                customerType = selectedType,
                                stnkStatus = selectedStnk,
                                remainingInstallment = sisa
                            )
                        )
                    }
                },
                enabled = contractNumber.isNotBlank() && customerName.isNotBlank() && motorcycleModel.isNotBlank()
            ) {
                Text("Simpan Data")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}
