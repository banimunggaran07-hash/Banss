package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.R
import com.example.model.CurrencyHelper
import com.example.ui.components.CekBahanClopCard
import com.example.ui.components.FormTaksasiCard
import com.example.ui.components.HighlightResultCard
import com.example.ui.components.PengaturanDialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaksasiScreen(
    viewModel: TaksasiViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.testTag("top_app_bar"),
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Calculate,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = stringResource(R.string.title_top_app_bar),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    // Tombol Menu Pengaturan
                    IconButton(
                        onClick = {
                            viewModel.setSettingsOpen(true)
                        },
                        modifier = Modifier.testTag("btn_menu_pengaturan")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(R.string.action_settings),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Tombol Reset Form
                    IconButton(
                        onClick = {
                            viewModel.onSearchQueryChanged("")
                            viewModel.onOtrMotorChanged("25000000")
                            viewModel.onSisaAngsuranChanged("0")
                            viewModel.onMateraiCountChanged(1)
                            scope.launch {
                                snackbarHostState.showSnackbar("Formulir taksasi direset ke nilai default")
                            }
                        },
                        modifier = Modifier.testTag("btn_reset_simulation")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reset Form",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 600.dp)
                ) {
                    // Section 1: Card Cek Bahan Clop
                    CekBahanClopCard(
                        query = uiState.searchQuery,
                        onQueryChange = viewModel::onSearchQueryChanged,
                        onSearchClick = viewModel::searchBahanClop,
                        searchMessage = uiState.searchMessage,
                        searchSuccess = uiState.searchSuccess,
                        matchedCustomer = uiState.matchedCustomer,
                        sampleContracts = viewModel.getSampleList(),
                        onSelectSample = viewModel::selectSampleContract
                    )
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 600.dp)
                ) {
                    // Section 2: Card Form Taksasi
                    FormTaksasiCard(
                        otrMotorInput = uiState.otrMotorInput,
                        onOtrMotorChange = viewModel::onOtrMotorChanged,
                        selectedCustomerType = uiState.customerType,
                        onCustomerTypeSelected = viewModel::onCustomerTypeSelected,
                        selectedStnkStatus = uiState.stnkStatus,
                        onStnkStatusSelected = viewModel::onStnkStatusSelected,
                        sisaAngsuranInput = uiState.sisaAngsuranInput,
                        onSisaAngsuranChange = viewModel::onSisaAngsuranChanged,
                        customerRateConfig = uiState.customerRateConfig
                    )
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 600.dp)
                ) {
                    // Section 3: Highlight Result Card dengan Kolom Input Materai & Total Dana Cair
                    HighlightResultCard(
                        plafondPercentage = uiState.plafondPercentage,
                        maksimalPinjaman = uiState.maksimalPinjaman,
                        pelunasanKontrakLama = uiState.pelunasanKontrakLama,
                        materaiCount = uiState.materaiCount,
                        biayaMaterai = uiState.biayaMaterai,
                        onMateraiCountChange = viewModel::onMateraiCountChanged,
                        onIncrementMaterai = viewModel::incrementMaterai,
                        onDecrementMaterai = viewModel::decrementMaterai,
                        totalDanaCairBersih = uiState.totalDanaCairBersih,
                        selectedTenorMonths = uiState.selectedTenorMonths,
                        estimasiAngsuranBulanan = uiState.estimasiAngsuranBulanan,
                        onTenorSelected = viewModel::onTenorSelected,
                        onAjukanSekarangClick = {
                            viewModel.setSuccessDialogOpen(true)
                        }
                    )
                }
            }
        }
    }

    // Menu Pengaturan Dialog (Harga Pasar, Bahan Clop, % Customer)
    PengaturanDialog(
        isOpen = uiState.isSettingsOpen,
        onDismiss = { viewModel.setSettingsOpen(false) },
        selectedTab = uiState.selectedSettingsTab,
        onTabSelected = viewModel::setSettingsTab,
        marketPrices = uiState.marketPrices,
        onAddMarketPrice = { model, year, price ->
            viewModel.addMarketPrice(model, year, price)
            scope.launch { snackbarHostState.showSnackbar("Data harga pasar motor berhasil ditambahkan") }
        },
        onUpdateMarketPrice = { id, model, year, price ->
            viewModel.updateMarketPrice(id, model, year, price)
            scope.launch { snackbarHostState.showSnackbar("Data harga pasar motor berhasil diperbarui") }
        },
        onDeleteMarketPrice = { id ->
            viewModel.deleteMarketPrice(id)
            scope.launch { snackbarHostState.showSnackbar("Data harga pasar motor telah dihapus") }
        },
        onApplyMarketPrice = { item ->
            viewModel.applyMarketPriceToCalculator(item)
            scope.launch { snackbarHostState.showSnackbar("OTR ${item.modelName} diterapkan ke form!") }
        },
        onResetMarketPrices = {
            viewModel.resetMarketPricesToDefault()
            scope.launch { snackbarHostState.showSnackbar("Daftar harga pasar direset ke default") }
        },
        clopDatabase = uiState.clopDatabase,
        onAddClopCustomer = { customer ->
            viewModel.addClopCustomer(customer)
            scope.launch { snackbarHostState.showSnackbar("Data nasabah bahan clop berhasil ditambahkan") }
        },
        onUpdateClopCustomer = { customer ->
            viewModel.updateClopCustomer(customer)
            scope.launch { snackbarHostState.showSnackbar("Data nasabah bahan clop diperbarui") }
        },
        onDeleteClopCustomer = { contractNumber ->
            viewModel.deleteClopCustomer(contractNumber)
            scope.launch { snackbarHostState.showSnackbar("Data bahan clop telah dihapus") }
        },
        onSelectClopCustomer = { customer ->
            viewModel.selectSampleContract(customer)
            scope.launch { snackbarHostState.showSnackbar("Data nasabah ${customer.customerName} dimuat ke formulir!") }
        },
        onResetClopDatabase = {
            viewModel.resetClopDatabaseToDefault()
            scope.launch { snackbarHostState.showSnackbar("Database bahan clop direset ke default") }
        },
        customerRateConfig = uiState.customerRateConfig,
        onSaveRateConfig = { newConfig ->
            viewModel.updateCustomerRateConfig(newConfig)
            scope.launch { snackbarHostState.showSnackbar("Pengaturan % customer berhasil disimpan & diterapkan!") }
        },
        onResetRateConfig = {
            viewModel.resetCustomerRateConfigToDefault()
            scope.launch { snackbarHostState.showSnackbar("Pengaturan % customer direset ke default") }
        }
    )

    // Submission Confirmation Dialog
    if (uiState.showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.setSuccessDialogOpen(false) },
            icon = {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(32.dp)
                    )
                }
            },
            title = {
                Text(
                    text = "Konfirmasi Pengajuan Pinjaman",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Ringkasan simulasi aplikasi pinjaman multiguna Anda siap diproses:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        modifier = Modifier.padding(vertical = 6.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            ResumeRow("Nilai OTR", CurrencyHelper.formatRupiah(uiState.otrMotorValue))
                            ResumeRow("Plafond Max", "${uiState.plafondPercentage}% (${uiState.customerType.displayName})")
                            ResumeRow("Maks. Pinjaman", CurrencyHelper.formatRupiah(uiState.maksimalPinjaman))
                            if (uiState.pelunasanKontrakLama > 0) {
                                ResumeRow("Pelunasan Lama", "- ${CurrencyHelper.formatRupiah(uiState.pelunasanKontrakLama)}")
                            }
                            if (uiState.materaiCount > 0) {
                                ResumeRow("Biaya Materai (${uiState.materaiCount} lbr)", "- ${CurrencyHelper.formatRupiah(uiState.biayaMaterai)}")
                            }
                            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                            ResumeRow(
                                label = "Total Dana Cair",
                                value = CurrencyHelper.formatRupiah(uiState.totalDanaCairBersih),
                                isHighlight = true
                            )
                            ResumeRow("Tenor", "${uiState.selectedTenorMonths} Bulan")
                            ResumeRow("Est. Angsuran", "${CurrencyHelper.formatRupiah(uiState.estimasiAngsuranBulanan)} / bln")
                        }
                    }

                    Text(
                        text = "Tim analis kredit kami akan segera menghubungi nasabah untuk verifikasi dokumen kendaraan & pencairan dana.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.setSuccessDialogOpen(false)
                        scope.launch {
                            snackbarHostState.showSnackbar("Pengajuan pinjaman berhasil dikirim ke sistem!")
                        }
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.testTag("btn_konfirmasi_pengajuan")
                ) {
                    Text("Kirim Aplikasi")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { viewModel.setSuccessDialogOpen(false) },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Tutup")
                }
            }
        )
    }
}

@Composable
private fun ResumeRow(
    label: String,
    value: String,
    isHighlight: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = if (isHighlight) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = value,
            style = if (isHighlight) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.bodySmall,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.SemiBold,
            color = if (isHighlight) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}
