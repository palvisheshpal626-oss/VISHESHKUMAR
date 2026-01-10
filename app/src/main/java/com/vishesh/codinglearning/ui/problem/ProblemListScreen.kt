package com.vishesh.codinglearning.ui.problem

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vishesh.codinglearning.R
import com.vishesh.codinglearning.data.model.Difficulty
import com.vishesh.codinglearning.data.model.Problem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProblemListScreen(
    viewModel: ProblemViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToProblemDetail: (Problem) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.problems),
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Difficulty filter
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = uiState.selectedDifficulty == null,
                    onClick = { viewModel.filterByDifficulty(null) },
                    label = { Text("All") }
                )
                FilterChip(
                    selected = uiState.selectedDifficulty == Difficulty.EASY,
                    onClick = { viewModel.filterByDifficulty(Difficulty.EASY) },
                    label = { Text(stringResource(R.string.easy)) }
                )
                FilterChip(
                    selected = uiState.selectedDifficulty == Difficulty.MEDIUM,
                    onClick = { viewModel.filterByDifficulty(Difficulty.MEDIUM) },
                    label = { Text(stringResource(R.string.medium)) }
                )
                FilterChip(
                    selected = uiState.selectedDifficulty == Difficulty.HARD,
                    onClick = { viewModel.filterByDifficulty(Difficulty.HARD) },
                    label = { Text(stringResource(R.string.hard)) }
                )
            }
            
            // Problem list
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.problems) { problem ->
                    ProblemCard(
                        problem = problem,
                        onClick = {
                            if (!problem.isLocked) {
                                viewModel.selectProblem(problem)
                                onNavigateToProblemDetail(problem)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ProblemCard(
    problem: Problem,
    onClick: () -> Unit
) {
    val difficultyColor = when (problem.difficulty) {
        Difficulty.EASY -> Color(0xFF4CAF50)
        Difficulty.MEDIUM -> Color(0xFFFFA726)
        Difficulty.HARD -> Color(0xFFF44336)
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !problem.isLocked, onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (problem.isLocked) Color.LightGray else Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = problem.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = problem.difficulty.name,
                    fontSize = 14.sp,
                    color = difficultyColor,
                    fontWeight = FontWeight.Medium
                )
            }
            
            when {
                problem.isSolved -> {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Solved",
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(24.dp)
                    )
                }
                problem.isLocked -> {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Locked",
                        tint = Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}
