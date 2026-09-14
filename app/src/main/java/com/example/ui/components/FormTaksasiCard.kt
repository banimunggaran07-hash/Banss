package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.Money
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.TwoWheeler
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.model.CustomerRateConfig
import com.example.model.CustomerType
import com.example.model.StnkStatus

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FormTaksasiCard(
    otrMotorInput: String,
    onOtrMotorChange: (String) -> Unit,
    selectedCustomerType: CustomerType,
    onCustomerTypeSelected: (CustomerType) -> Unit,
    selectedStnkStatus: StnkStatus,
    onStnkStatusSelected: (StnkStatus) -> Unit,
    sisaAngsuranInput: String,
    onSisaAngsuranChange: (String) -> Unit,
    customerRateConfig: CustomerRateConfig = CustomerRateConfig(),
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("card_form_taksasi"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header Section
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Calculate,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = stringResource(R.string.title_section_taksasi),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Parameter penilaian taksasi motor dan histori angsuran",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 1. Input Text Field: Nilai OTR Motor (Rp)
            Text(
                text = stringResource(R.string.label_nilai_otr),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = otrMotorInput,
                onValueChange = onOtrMotorChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_nilai_otr_motor"),
                placeholder = { Text(stringResource(R.string.hint_nilai_otr)) },
                prefix = {
                    Text(
                        text = "Rp ",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.TwoWheeler,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                trailingIcon = {
                    if (otrMotorInput.isNotEmpty()) {
                        IconButton(onClick = { onOtrMotorChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Hapus OTR",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                )
            )

            // Quick OTR presets
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                listOf("20.000.000", "25.000.000", "30.000.000", "35.000.000").forEach { preset ->
                    AssistChip(
                        onClick = { onOtrMotorChange(preset) },
                        label = {
                            Text(
                                text = preset.replace(".000.000", " Jt"),
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        border = null
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 2. Filter Chip Options: Tipe Customer
            Text(
                text = stringResource(R.string.label_tipe_customer),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            val nRate = customerRateConfig.newCustomerPercent
            val mRate = customerRateConfig.mediumPercent
            val gRate = customerRateConfig.goodPercent
            val lRate = customerRateConfig.goodLoyalPercent
            Text(
                text = "Plafond: New ($nRate%) | Medium ($mRate%) | Good ($gRate%) | Good Loyal ($lRate%)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("filter_chips_tipe_customer")
            ) {
                CustomerType.values().forEach { type ->
                    val isSelected = selectedCustomerType == type
                    val configuredPercent = customerRateConfig.getPercentFor(type)
                    FilterChip(
                        selected = isSelected,
                        onClick = { onCustomerTypeSelected(type) },
                        label = {
                            Text(
                                text = "${type.displayName} ($configuredPercent%)",
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        leadingIcon = if (isSelected) {
                            {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        } else {
                            {
                                Icon(
                                    imageVector = Icons.Outlined.Person,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 3. Filter Chip Options: Status Kepemilikan STNK
            Text(
                text = stringResource(R.string.label_status_stnk),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "STNK Atas Nama Sendiri mendapat penawaran plafond optimal",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("filter_chips_status_stnk")
            ) {
                StnkStatus.values().forEach { status ->
                    val isSelected = selectedStnkStatus == status
                    FilterChip(
                        selected = isSelected,
                        onClick = { onStnkStatusSelected(status) },
                        label = {
                            Text(
                                text = status.displayName,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        leadingIcon = if (isSelected) {
                            {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        } else null,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            selectedLeadingIconColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 4. Input Text Field: Sisa Angsuran Kontrak Lama (Rp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.label_sisa_angsuran),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                AssistChip(
                    onClick = { onSisaAngsuranChange("0") },
                    label = {
                        Text(
                            text = "Lunas (Rp 0)",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                    ),
                    border = null
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = sisaAngsuranInput,
                onValueChange = onSisaAngsuranChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_sisa_angsuran"),
                placeholder = { Text(stringResource(R.string.hint_sisa_angsuran)) },
                prefix = {
                    Text(
                        text = "Rp ",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Money,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                },
                trailingIcon = {
                    if (sisaAngsuranInput.isNotEmpty()) {
                        IconButton(onClick = { onSisaAngsuranChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Hapus Angsuran",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                )
            )
        }
    }
}
