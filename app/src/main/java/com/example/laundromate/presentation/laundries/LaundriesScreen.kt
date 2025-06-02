package com.example.laundromate.presentation.laundries

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.laundromate.data.models.Laundry
import com.example.laundromate.data.models.WashingMachine
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaundriesScreen(
    viewModel: LaundriesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var showBookingDialog by remember { mutableStateOf(false) }
    var bookingMachineId by remember { mutableStateOf<Int?>(null) }

    var selectedMode by remember { mutableStateOf("normal") }
    var selectedTemperature by remember { mutableStateOf(30) }
    val temperatureOptions = listOf(
        "cold" to 0,
        "warm" to 30,
        "hot" to 60
    )
    val modeOptions = listOf("delicate", "normal", "heavy")

    val datePickerState = rememberDatePickerState()
    var selectedDate by remember { mutableStateOf<LocalDateTime?>(null) }
    var selectedHour by remember { mutableIntStateOf(10) }
    var selectedMinute by remember { mutableIntStateOf(0) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showHourDropdown by remember { mutableStateOf(false) }
    var showMinuteDropdown by remember { mutableStateOf(false) }

    val pullRefreshState = rememberPullToRefreshState()

    LaunchedEffect(uiState.bookingSuccess) {
        if (uiState.bookingSuccess) {
            Toast.makeText(context, "Booking successful!", Toast.LENGTH_SHORT).show()
            viewModel.clearBookingSuccess()
        }
    }

    PullToRefreshBox(
        isRefreshing = uiState.isLoading,
        onRefresh = { viewModel.loadLaundries() },
        state = pullRefreshState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 0.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Text(
                text = "LaundroMate",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 24.dp, bottom = 8.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Text(
                    text = "Find and book your washing machine easily.",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.padding(18.dp)
                )
            }

            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                uiState.error != null -> {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = uiState.error!!,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    viewModel.clearError()
                                    viewModel.loadLaundries()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                            ) {
                                Text("Retry", color = MaterialTheme.colorScheme.onError)
                            }
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        contentPadding = PaddingValues(bottom = 32.dp)
                    ) {
                        items(uiState.laundries) { laundry ->
                            LaundryCard(
                                laundry = laundry,
                                machines = uiState.washingMachines.filter { it.laundry == laundry.id },
                                onBookMachine = { machineId ->
                                    bookingMachineId = machineId
                                    showBookingDialog = true
                                }
                            )
                        }
                    }
                }
            }

            if (uiState.bookingSuccess) {
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp)
                ) {
                    Text("Booking successful!")
                }
            }
        }
    }

    if (showBookingDialog && bookingMachineId != null) {
        val now = LocalDateTime.now()
        val selectedDateTime = selectedDate?.withHour(selectedHour)?.withMinute(selectedMinute)
        val isDateTimeInFuture = selectedDateTime?.isAfter(now) == true

        AlertDialog(
            onDismissRequest = { showBookingDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Settings, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(8.dp))
                    Text("Booking Settings", style = MaterialTheme.typography.titleLarge)
                }
            },
            text = {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    Text("Mode:", style = MaterialTheme.typography.titleMedium)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        modeOptions.forEach { mode ->
                            FilterChip(
                                selected = selectedMode == mode,
                                onClick = { selectedMode = mode },
                                label = { Text(mode.replaceFirstChar { it.uppercase() }) },
                                leadingIcon = {
                                    if (selectedMode == mode) Icon(Icons.Default.Check, contentDescription = null)
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Temperature:", style = MaterialTheme.typography.titleMedium)
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        temperatureOptions.forEach { (label, value) ->
                            FilterChip(
                                selected = selectedTemperature == value,
                                onClick = { selectedTemperature = value },
                                label = { Text("$label (${value}°C)") },
                                leadingIcon = {
                                    if (selectedTemperature == value) Icon(Icons.Default.Check, contentDescription = null)
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Booking Time:", style = MaterialTheme.typography.titleMedium)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Button(
                            onClick = { showDatePicker = true },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                        ) {
                            Text(
                                selectedDate?.toLocalDate()
                                    ?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                                    ?: "Select Date",
                                color = MaterialTheme.colorScheme.onSecondary
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Box {
                            OutlinedButton(onClick = { showHourDropdown = true }) {
                                Text(String.format("%02d", selectedHour))
                            }
                            DropdownMenu(
                                expanded = showHourDropdown,
                                onDismissRequest = { showHourDropdown = false }
                            ) {
                                (0..23).forEach { hour ->
                                    DropdownMenuItem(
                                        text = { Text(String.format("%02d", hour)) },
                                        onClick = {
                                            selectedHour = hour
                                            showHourDropdown = false
                                        }
                                    )
                                }
                            }
                        }
                        Text(":", modifier = Modifier.padding(horizontal = 2.dp))
                        Box {
                            OutlinedButton(onClick = { showMinuteDropdown = true }) {
                                Text(String.format("%02d", selectedMinute))
                            }
                            DropdownMenu(
                                expanded = showMinuteDropdown,
                                onDismissRequest = { showMinuteDropdown = false }
                            ) {
                                listOf(0, 15, 30, 45).forEach { minute ->
                                    DropdownMenuItem(
                                        text = { Text(String.format("%02d", minute)) },
                                        onClick = {
                                            selectedMinute = minute
                                            showMinuteDropdown = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    if (selectedDate != null) {
                        Text(
                            "Selected: " +
                                    selectedDate!!.toLocalDate()
                                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) +
                                    " " + String.format("%02d:%02d", selectedHour, selectedMinute),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        if (!isDateTimeInFuture) {
                            Text(
                                "Please select a future date and time.",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (bookingMachineId != null && selectedDate != null && isDateTimeInFuture) {
                            val dateTime = selectedDate!!.withHour(selectedHour).withMinute(selectedMinute)
                            val isoDate = dateTime.atZone(ZoneId.systemDefault()).toInstant().toString()
                            viewModel.bookMachine(
                                machineId = bookingMachineId!!,
                                mode = selectedMode,
                                temperature = selectedTemperature,
                                bookedFor = isoDate
                            )
                            showBookingDialog = false
                            bookingMachineId = null
                            selectedDate = null
                        }
                    },
                    enabled = selectedDate != null && isDateTimeInFuture,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Book", color = MaterialTheme.colorScheme.onPrimary)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = {
                    showBookingDialog = false
                    bookingMachineId = null
                    selectedDate = null
                }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showDatePicker) {
        val today = Instant.now().atZone(ZoneId.systemDefault()).toLocalDate()
        val selectedPickerDate = datePickerState.selectedDateMillis?.let {
            Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
        }
        val isDateValid = selectedPickerDate == null || !selectedPickerDate.isBefore(today)

        AlertDialog(
            onDismissRequest = { showDatePicker = false },
            title = { Text("Select Booking Date") },
            text = {
                DatePicker(state = datePickerState)
            },
            confirmButton = {
                Button(
                    onClick = {
                        val millis = datePickerState.selectedDateMillis
                        if (millis != null) {
                            val date = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
                            selectedDate = date.atStartOfDay()
                        }
                        showDatePicker = false
                    },
                    enabled = isDateValid && datePickerState.selectedDateMillis != null
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun LaundryCard(
    laundry: Laundry,
    machines: List<WashingMachine>,
    onBookMachine: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = laundry.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = laundry.address,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 2.dp)
            )
            Text(
                text = "Price: ${laundry.pricePerKg}/kg",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(10.dp))

            Divider()

            Text(
                text = "Washing Machines:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 10.dp, bottom = 4.dp)
            )

            machines.forEach { machine ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = when (machine.status) {
                            "available" -> MaterialTheme.colorScheme.surfaceVariant
                            "in_use" -> MaterialTheme.colorScheme.errorContainer
                            else -> MaterialTheme.colorScheme.surface
                        }
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "Machine #${machine.id}",
                                style = MaterialTheme.typography.titleSmall
                            )
                            Text(
                                text = machine.status.replace('_', ' ').replaceFirstChar { it.uppercase() },
                                color = when (machine.status) {
                                    "available" -> Color(0xFF388E3C)
                                    "in_use" -> Color(0xFFD32F2F)
                                    else -> Color.Gray
                                },
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "Max load: ${machine.maxLoad}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                        Button(
                            onClick = { onBookMachine(machine.id) },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text("Book", color = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLaundryCard() {
    LaundryCard(
        laundry = Laundry(
            id = 1,
            name = "Sample Laundry",
            address = "123 Main St",
            pricePerKg = "5.0",
            dynamicPricing = false
        ),
        machines = listOf(
            WashingMachine(id = 1, status = "available", laundry = 1, maxLoad = "7kg"),
            WashingMachine(id = 2, status = "in_use", laundry = 1, maxLoad = "7kg")
        ),
        onBookMachine = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewLaundriesScreen() {
    Column {
        PreviewLaundryCard()
    }
}
