package com.example.routineapp.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun PomodoroBar(
    modifier: Modifier = Modifier,
    workMinutes: Int = 25,
    restMinutes: Int = 5
) {
    var isRunning by remember { mutableStateOf(false) }
    var isRest by remember { mutableStateOf(false) }
    var secondsLeft by remember { mutableStateOf(workMinutes * 60) }

    // Temporizador simple
    LaunchedEffect(isRunning, secondsLeft) {
        while (isRunning && secondsLeft > 0) {
            delay(1000)
            secondsLeft--
        }
        if (isRunning && secondsLeft == 0) {
            // Cambiar fase
            isRest = !isRest
            secondsLeft = (if (isRest) restMinutes else workMinutes) * 60
        }
    }

    val mm = (secondsLeft / 60).toString().padStart(2, '0')
    val ss = (secondsLeft % 60).toString().padStart(2, '0')
    val fase = if (isRest) "Descanso" else "Foco"

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.secondaryContainer,
        tonalElevation = 3.dp,
        shadowElevation = 3.dp
    ) {
        Row(
            Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("$mm:$ss", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(fase, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSecondaryContainer)
            Spacer(Modifier.weight(1f))
            FilledTonalButton(
                onClick = { isRunning = !isRunning },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(if (isRunning) "Pausar" else "Iniciar")
            }
            OutlinedButton(
                onClick = {
                    isRunning = false
                    isRest = false
                    secondsLeft = workMinutes * 60
                },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Reiniciar")
            }
        }
    }
}
