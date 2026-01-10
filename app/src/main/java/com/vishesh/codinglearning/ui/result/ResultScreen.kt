package com.vishesh.codinglearning.ui.result

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vishesh.codinglearning.R

@Composable
fun ResultScreen(
    correctAnswers: Int,
    totalQuestions: Int,
    onContinue: () -> Unit
) {
    val accuracy = (correctAnswers.toFloat() / totalQuestions.toFloat()) * 100
    val coinsEarned = correctAnswers
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF6B6BFF),
                        Color(0xFF8E54E9)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Result icon
            Text(
                text = if (accuracy >= 70) "🎉" else "📚",
                fontSize = 80.sp
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Title
            Text(
                text = stringResource(R.string.result_title),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Results card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.95f)
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ResultRow(
                        label = "Total Questions",
                        value = totalQuestions.toString()
                    )
                    
                    Divider(modifier = Modifier.padding(vertical = 12.dp))
                    
                    ResultRow(
                        label = "Correct Answers",
                        value = correctAnswers.toString(),
                        valueColor = Color(0xFF4CAF50)
                    )
                    
                    Divider(modifier = Modifier.padding(vertical = 12.dp))
                    
                    ResultRow(
                        label = "Accuracy",
                        value = String.format("%.1f%%", accuracy),
                        valueColor = Color(0xFF6B6BFF)
                    )
                    
                    Divider(modifier = Modifier.padding(vertical = 12.dp))
                    
                    ResultRow(
                        label = "Coins Earned",
                        value = "💰 $coinsEarned",
                        valueColor = Color(0xFFFFA726)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Motivational message
            Text(
                text = when {
                    accuracy >= 90 -> "Excellent! Keep it up! 🌟"
                    accuracy >= 70 -> "Great job! 👍"
                    accuracy >= 50 -> "Good effort! Keep learning! 💪"
                    else -> "Don't give up! Practice more! 📖"
                },
                fontSize = 18.sp,
                color = Color.White.copy(alpha = 0.9f),
                fontWeight = FontWeight.Medium
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Continue button
            Button(
                onClick = {
                    // TODO: Show interstitial ad
                    onContinue()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF6B6BFF)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.continue_btn),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ResultRow(
    label: String,
    value: String,
    valueColor: Color = Color.Black
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            color = Color.Gray
        )
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = valueColor
        )
    }
}
