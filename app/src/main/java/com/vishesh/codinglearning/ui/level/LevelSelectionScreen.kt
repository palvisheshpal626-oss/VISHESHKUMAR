package com.vishesh.codinglearning.ui.level

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vishesh.codinglearning.R
import com.vishesh.codinglearning.data.model.Level
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelSelectionScreen(
    viewModel: LevelViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToMcq: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showSkipDialog by remember { mutableStateOf<Level?>(null) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.select_level),
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // Coin display
                    Row(
                        modifier = Modifier.padding(end = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "💰", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${uiState.coins}",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6B6BFF),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(uiState.levels) { level ->
                LevelCard(
                    level = level,
                    onClick = {
                        if (!level.isLocked) {
                            onNavigateToMcq(level.number)
                        } else {
                            showSkipDialog = level
                        }
                    }
                )
            }
        }
    }
    
    // Skip level dialog
    showSkipDialog?.let { level ->
        SkipLevelDialog(
            level = level,
            coins = uiState.coins,
            onDismiss = { showSkipDialog = null },
            onSkip = {
                val scope = rememberCoroutineScope()
                scope.launch {
                    val hasEnoughCoins = uiState.coins >= 5
                    viewModel.skipLevel(level.number, hasEnoughCoins)
                    showSkipDialog = null
                    // TODO: Show rewarded ad
                    // After ad, call viewModel.onRewardedAdCompleted(level.number)
                }
            }
        )
    }
}

@Composable
fun LevelCard(
    level: Level,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        level.isCompleted -> Color(0xFF4CAF50)
        level.isLocked -> Color(0xFF9E9E9E)
        else -> Color(0xFF6B6BFF)
    }
    
    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable(onClick = onClick),
        shape = CircleShape,
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when {
                level.isCompleted -> {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Completed",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
                level.isLocked -> {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Locked",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                else -> {
                    Text(
                        text = "${level.number}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun SkipLevelDialog(
    level: Level,
    coins: Int,
    onDismiss: () -> Unit,
    onSkip: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(R.string.skip_level))
        },
        text = {
            val message = if (coins >= 5) {
                "Skip to Level ${level.number}?\nCost: 5 coins + Watch 1 ad"
            } else {
                "Skip to Level ${level.number}?\nWatch 2 ads to skip"
            }
            Text(text = message)
        },
        confirmButton = {
            TextButton(onClick = onSkip) {
                Text("Skip")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
