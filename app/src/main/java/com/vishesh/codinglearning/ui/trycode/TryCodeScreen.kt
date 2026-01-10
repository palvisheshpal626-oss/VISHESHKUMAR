package com.vishesh.codinglearning.ui.trycode

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vishesh.codinglearning.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TryCodeScreen(
    viewModel: TryCodeViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToProblems: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.try_code_title),
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
                .padding(16.dp)
        ) {
            // Instruction card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF3E5F5)
                )
            ) {
                Text(
                    text = "Edit the code below and click 'Run Code' to see the output. This connects theory to practice!",
                    modifier = Modifier.padding(12.dp),
                    fontSize = 14.sp
                )
            }
            
            // Code editor
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF263238)
                )
            ) {
                TextField(
                    value = uiState.code,
                    onValueChange = { viewModel.updateCode(it) },
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    textStyle = LocalTextStyle.current.copy(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 14.sp,
                        color = Color.White
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Run button
            Button(
                onClick = { viewModel.runCode() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !uiState.isRunning,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (uiState.isRunning) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White
                    )
                } else {
                    Text(
                        text = stringResource(R.string.run_code),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Output section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.8f),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                ) {
                    Text(
                        text = stringResource(R.string.output),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    
                    Text(
                        text = when {
                            uiState.error != null -> "Error: ${uiState.error}"
                            uiState.output.isNotEmpty() -> uiState.output
                            else -> "Output will appear here..."
                        },
                        fontFamily = FontFamily.Monospace,
                        fontSize = 14.sp,
                        color = if (uiState.error != null) Color.Red else Color.Black,
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Continue to problems button
            OutlinedButton(
                onClick = onNavigateToProblems,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Continue to Problems")
            }
        }
    }
}
