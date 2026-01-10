package com.vishesh.codinglearning.ui.mcq

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vishesh.codinglearning.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun McqScreen(
    viewModel: McqViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToResult: (correct: Int, total: Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    if (uiState.showResult) {
        onNavigateToResult(uiState.correctAnswersCount, uiState.totalQuestions)
        return
    }
    
    val currentQuestion = uiState.currentQuestion
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Question ${uiState.currentQuestionIndex + 1} of ${uiState.totalQuestions}",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
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
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        if (currentQuestion == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    // Question card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Text(
                                text = currentQuestion.question,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Options
                    currentQuestion.options.forEachIndexed { index, option ->
                        OptionCard(
                            option = option,
                            isSelected = uiState.selectedAnswerIndex == index,
                            onClick = {
                                viewModel.onEvent(McqEvent.SelectAnswer(index))
                            }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Hint section
                    if (uiState.showHint) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFFFF9C4)
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "💡 Hint",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = uiState.hintText,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    } else {
                        OutlinedButton(
                            onClick = {
                                if (uiState.coins >= 2) {
                                    viewModel.onEvent(McqEvent.GetHint)
                                } else {
                                    viewModel.onEvent(McqEvent.WatchAdForHint)
                                    // TODO: Show rewarded ad
                                    // After ad, call viewModel.onHintAdCompleted()
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = if (uiState.coins >= 2) {
                                    stringResource(R.string.get_hint) + " (2 coins)"
                                } else {
                                    stringResource(R.string.get_hint) + " (Watch Ad)"
                                }
                            )
                        }
                    }
                }
                
                // Next button
                Button(
                    onClick = {
                        viewModel.onEvent(McqEvent.NextQuestion)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = uiState.selectedAnswerIndex != null,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6B6BFF)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (uiState.isLastQuestion) {
                            "Finish"
                        } else {
                            stringResource(R.string.next)
                        },
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun OptionCard(
    option: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .then(
                if (isSelected) {
                    Modifier.border(
                        width = 2.dp,
                        color = Color(0xFF6B6BFF),
                        shape = RoundedCornerShape(12.dp)
                    )
                } else {
                    Modifier
                }
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                Color(0xFFE8EAF6)
            } else {
                Color.White
            }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = option,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
            if (isSelected) {
                Text(
                    text = "✓",
                    fontSize = 20.sp,
                    color = Color(0xFF6B6BFF),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
